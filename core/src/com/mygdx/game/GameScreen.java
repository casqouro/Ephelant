package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*  To Do
    2.  Animate the timer now that the assets are built
    3.  Pressing 'ESC' should pause everything + ask yes/no to quit
    4.  Everything should be fresh/reset after exiting/re-entering
        maybe a setup method which is called once upon entering, then reset?
    5.  There's a bug with extra letters appearing if the user starts a game,
        places some letters, escapes to menu, then starts again.
*/

public class GameScreen {
    Stage gameScreenStage;
    
    private final BitmapFont font;
    private final FreeTypeFontGenerator generator;
    private final FreeTypeFontGenerator.FreeTypeFontParameter parameter;              
    
    private String word; 
    private String user;
    private int[] state; 
    private boolean wordLoaded;    
    private boolean ready;
    private Map<Integer, String> shuffled;
    
    private final Label wordLabel;
    private final Label userLabel;    
    
    private final Sound error;
    private final Sound correct;
    
    Button newWordButton;
    Button readyButton;
    
    TextureRegionDrawable newWordTexture;
    TextureRegionDrawable readyTexture;
    TextureRegionDrawable notreadyTexture;
    
    private static final int CLEAR = 0;
    private static final int GRAY = 1;
    private static final int WHITE = 2;
    private static final int RED = 3;    
    private int position = 1;    
    
    public boolean exitGame = false;
    public boolean setupCalled = false;
    
    public GameScreen() {
        gameScreenStage = new Stage();
        gameScreenStage.addListener(new gameInputListener());    
        
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts//Raleway-Medium.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        font = generator.generateFont(parameter);
        font.getData().markupEnabled = true;
        generator.dispose();        
        
        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);
        // background not working, needs to be researched
        fontStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(new FileHandle("images//black.png")))) {};
        wordLabel = new Label("...", fontStyle);
        wordLabel.setBounds(0, 400, Gdx.graphics.getWidth(), 200);
        wordLabel.setWrap(true);        
        wordLabel.setAlignment(Align.center);
        //gameScreenStage.addActor(wordLabel);        
        userLabel = new Label("...", fontStyle);
        userLabel.setPosition(100, 100);      
        userLabel.setBounds(0, 300, Gdx.graphics.getWidth(), 100);
        userLabel.setWrap(true);
        userLabel.setAlignment(Align.center);
        //gameScreenStage.addActor(userLabel);        

        word = "";
        user = "";
        state = new int[0];
        wordLoaded = false;        
        
        error = Gdx.audio.newSound(Gdx.files.internal("sounds//error.ogg"));
        correct = Gdx.audio.newSound(Gdx.files.internal("sounds//correct.ogg"));  
        
        newWordButton = new Button();
        readyButton = new Button();   
        newWordTexture = new TextureRegionDrawable(
                                                  new TextureRegion(
                                                  new Texture(
                                                  new FileHandle("images//newword.png"))));
        readyTexture = new TextureRegionDrawable(
                                                  new TextureRegion(
                                                  new Texture(
                                                  new FileHandle("images//ready.png"))));
        notreadyTexture = new TextureRegionDrawable(
                                                  new TextureRegion(
                                                  new Texture(
                                                  new FileHandle("images//notready.png"))));
              
        newWordButton.setStyle(new Button.ButtonStyle(newWordTexture, newWordTexture, newWordTexture));
        newWordButton.setBounds(50, 450, Gdx.graphics.getWidth() - 100, 100);
        newWordButton.addListener(new newWordListener()); 
               
        readyButton.setStyle(new Button.ButtonStyle(notreadyTexture, notreadyTexture, notreadyTexture));
        readyButton.setBounds(50, 300, Gdx.graphics.getWidth() - 100, 100);
        readyButton.addListener(new readyListener()); 
        
        gameScreenStage.addActor(newWordButton);
        gameScreenStage.addActor(readyButton);
    }
    
    public void setup() {
        gameScreenStage.clear();
        gameScreenStage.addListener(new gameInputListener());
        gameScreenStage.addActor(newWordButton);
        gameScreenStage.addActor(readyButton);
        word = "";
        user = "";
        userLabel.setText("");
        wordLoaded = false;
        ready = false;
        position = 1;
    }
    
    // Get a random word from the wordlist
    private void selectNewWord() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("..\\assets\\words\\wordlist.txt")); 
        Random rand = new Random();
        word = "";
        user = "";
        
        int total = Integer.parseInt(br.readLine());
        int size = rand.nextInt(Integer.parseInt(br.readLine())) + 1;
                
        // Traverse the list until you find the word
        if (size > 0 ) {
            while (size > 0) {
                word = br.readLine();
                String[] token;
                token = word.split(" ");            
                int available = Integer.parseInt((token[0]));

                if (available == 1) {
                    word = token[1];
                    wordLabel.setText(word);
                    size--;
                }
            }
        } else {
            System.out.println("The list is empty, and I should be doing something useful like handling an exception.");
        }
                
        state = new int[word.length()];
        
        for (int a = 0; a < state.length; a++) {
            changeLetterColor(a, CLEAR);
        }
    }  
    
    // Uses a Fisher-Yates shuffle on an int array, used to build a shuffled map.    
    private void shuffleLetters() {
        Random rand = new Random();
        shuffled = new LinkedHashMap<>();
        
        int[] place = new int[word.length()];
        for (int a = 0; a < place.length; a++) {
            int r = rand.nextInt(a + 1);
            place[a] = place[r];
            place[r] = a;
        }
        
        for (int a = 0; a < place.length; a++) {
            shuffled.put(place[a], String.valueOf(word.charAt(place[a])));
        }
        
        state[(int) shuffled.keySet().toArray()[0]] = GRAY;
        state[(int) shuffled.keySet().toArray()[1]] = RED;    
        
        user = "";
        for (int a = 0; a < shuffled.size(); a++) {
            user += (String) shuffled.values().toArray()[a];
        }
                
        updateUserLabel(); // should be done when "ready" button pressed, will be moved when Screen class built
        wordLabel.setText("");
    }    
    
    // Should happen after a player left/right movement
    private void updateUserLabel() {
        String updatedLabel = "[GRAY]";
        
        int whitey = (int) shuffled.keySet().toArray()[position - 1];
        
        for (int a = 0; a < state.length; a++) {
            if (a == whitey) {
                updatedLabel += "[WHITE]" + String.valueOf(word.charAt(a)) + "[GRAY]";
            } else if (state[a] == GRAY) {
                updatedLabel += String.valueOf(word.charAt(a));
            }  
        }
        
        // this could probably be pulled out and used for when we know the game is completed
        if (position == word.length()) {
            updatedLabel = "[WHITE]" + word;
        }
        
        userLabel.setText(updatedLabel);
    }

    // Should happen after a player left/right movement
    private void updateWordLabel() {
        if (position < word.length()) {
            wordLabel.setText("[RED]" + String.valueOf(user.charAt(position)));
        } else {
            wordLabel.setText(("[WHITE]") + word);
            ready = false;
        }
    }        
    
    private void changeLetterColor(int index, int color) {
        if (color == CLEAR) {
            state[index] = 0;            
        }        
        
        if (color == GRAY) {
            state[index] = 1;
        }
        
        if (color == WHITE) {
            state[index] = 2;
        }
        
        if (color == RED) {
            state[index] = 3;
            //position++;
        }        
    }    
    
    private String buildWord(String in) {
        String sub = in;
        String build = "";
                
        for (int a = 0; a < in.length(); a++) {
            if (state[a] == CLEAR) {
                build += "[CLEAR]" + sub.substring(a, a + 1);
            }            
            
            if (state[a] == WHITE) {
                build += "[WHITE]" + sub.substring(a, a + 1);
            }
                        
            if (state[a] == GRAY) {
                build += "[GRAY]" + sub.substring(a, a + 1);
            }
            
            if (state[a] == RED) {
                build += "[RED]" + sub.substring(a, a + 1);
            }   
        }
        
        return build;
    }    
    
    private void handleLeft() {                       
        boolean valid = false;
        String redLetter = String.valueOf(user.charAt(position)); // Gets the current to-Sort letter
        int priorIndex = (int) shuffled.keySet().toArray()[position - 1]; // Gets the index of the last placed letter (from the SHUFFLED index)
        
        for (int a = priorIndex - 1; a >= 0; a--) {
            if (redLetter.equals(String.valueOf(word.charAt(a))) && state[a] != GRAY) {
                valid = true;
                state[a] = GRAY;
                correct.play();
                position++;
                updateUserLabel();
                updateWordLabel();                  
                break;
            }            
        }
        
        if (!valid) {
            error.play();
        }              
    } 
    
    private void handleRight() {
        boolean valid = false;
        String redLetter = String.valueOf(user.charAt(position)); // Gets the current to-Sort letter
        int priorIndex = (int) shuffled.keySet().toArray()[position - 1]; // Gets the index of the last placed letter (from the SHUFFLED index)

        for (int a = priorIndex; a < word.length(); a++) {
            if (redLetter.equals(String.valueOf(word.charAt(a))) && state[a] != GRAY) {
                valid = true;
                state[a] = GRAY;
                correct.play();
                position++;
                updateUserLabel();
                updateWordLabel();                
                break;
            }            
        }
        
        if (!valid) {
            error.play();
        }        
    }    
    
    private class newWordListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            newWordButton.remove();
            gameScreenStage.addActor(wordLabel);
            readyButton.getStyle().up = (TextureRegionDrawable) readyTexture;
            readyButton.getStyle().down = (TextureRegionDrawable) readyTexture;
            readyButton.getStyle().checked = (TextureRegionDrawable) readyTexture;
            
            try {
                selectNewWord();
            } catch (IOException ex) {
                Logger.getLogger(Ephelant.class.getName()).log(Level.SEVERE, null, ex);
            }     
            
            wordLoaded = true;
        }
    }
    
    private class readyListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (wordLoaded) {
                readyButton.remove();
                gameScreenStage.addActor(userLabel);
                
                shuffleLetters();
                updateWordLabel();        
                ready = true;                
            }
        }        
    }
    
    private class gameInputListener extends InputListener {
        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch (keycode) {
                case Input.Keys.ESCAPE:
                    exitGame = true;
                    break;
                case Input.Keys.LEFT:
                    if (ready) { handleLeft(); }
                    break;
                case Input.Keys.RIGHT:
                    if (ready) { handleRight(); }
                    break;
                case Input.Keys.SPACE:
                    if (wordLoaded && !ready) {
                        readyButton.remove();
                        gameScreenStage.addActor(userLabel);                        
                        
                        shuffleLetters();
                        updateWordLabel();        
                        ready = true;
                    }                    
                    
                    if (!wordLoaded) {
                        newWordButton.remove();
                        gameScreenStage.addActor(wordLabel);
                        readyButton.getStyle().up = (TextureRegionDrawable) readyTexture;
                        readyButton.getStyle().down = (TextureRegionDrawable) readyTexture;
                        readyButton.getStyle().checked = (TextureRegionDrawable) readyTexture;                        
                        
                        try {
                            selectNewWord();
                        } catch (IOException ex) {
                            Logger.getLogger(Ephelant.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        wordLoaded = true;
                    }                    
                    break;
            }
            return true;
        }        
    }    
}