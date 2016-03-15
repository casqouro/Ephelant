package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ephelant extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;   
    private final int WORLD_WIDTH = 100;
    private final int WORLD_HEIGHT = 100;

    private Texture texture;
    private Sprite sprite;
    
    private static final int CLEAR = 0;
    private static final int GRAY = 1;
    private static final int WHITE = 2;
    private static final int RED = 3;
    
    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontParameter parameter;
    
    private String word; 
    private String user;
    private int[] state; 
    private boolean wordLoaded;    
    private boolean ready;
    private Map<Integer, String> shuffled;
    
    Stage stage;
    Button play;
    //Button ready;
    Button again;
    Button back;
    
    Label wordLabel;
    Label userLabel;
    
    private int screen = 1;
    private static final int MENU = 0;
    private static final int GAME = 1;
    
    int position = 1;
    
    private Sound error;
    private Sound correct;
    /* THINGS TO DO
    
        FINISH PROTOTYPING GAMEPLAY
            1.  Center the word so it's not easy to tell what to do.
    
        SCREENS
            1.  StartScreen class
            2.  GameScreen class
    
        BUGS
    
            1.  Handle relative positions!  Roundabout has two U's.  The user
                doesn't know which one I'm using.  The first U does NOT have a
                'd' to its left, but the second U DOES have a 'd' to its left.
    
                When a letter is selected I could probably troll the remaining
                unplaced letters and add ALL letters of a type to an array.
                Then I could execute the logic for each one until yes/no.
    */
    
        
    /*  The word selected is displayed.  When the user indicates 'ready', a 
        random starting letter is selected and displayed, then the word 
        disappears.  A random letter is selected and the player must place it
        left or right of the existing letter.
    
        Previously selected letters = GRAY;
        Current location from which to judge = RED;
        Letter to sort = WHITE;
    
        a letter is chosen as the starting position
        another is chosen as starting letter to sort        
    */
    
    
    @Override
    public void create() {
        Gdx.input.setInputProcessor(new KeyProcessor());
        texture = new Texture(new FileHandle("S:\\Users\\CANAMBRA\\Desktop\\test.jpg"));
        sprite = new Sprite(texture);
        sprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);
                        
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();        
        
        error = Gdx.audio.newSound(Gdx.files.internal("sounds//error.ogg"));
        correct = Gdx.audio.newSound(Gdx.files.internal("sounds//correct.ogg"));
        
        //camera = new OrthographicCamera(100, 100);
        //camera.position.set(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f, 0);
        //camera.update();
        
        batch = new SpriteBatch();        
        
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts//Raleway-Medium.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 40;
        font = generator.generateFont(parameter);
        font.getData().markupEnabled = true;
        generator.dispose();
        
        stage = new Stage();               
        LabelStyle fontStyle = new LabelStyle(font, Color.WHITE);
        userLabel = new Label("...", fontStyle);
        userLabel.setPosition(100, 100);      
        userLabel.setBounds(0, 100, w, 100);
        userLabel.setWrap(true);
        userLabel.setAlignment(Align.center);
        stage.addActor(userLabel);        
        wordLabel = new Label("...", fontStyle);
        wordLabel.setBounds(0, 200, w, 200);
        wordLabel.setWrap(true);        
        wordLabel.setAlignment(Align.center);
        stage.addActor(wordLabel);
                        
        word = "";
        user = "";
        state = new int[0];
        wordLoaded = false; 
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
        
        for (int a = 0; a < state.length; a++) {
            if (state[a] == GRAY) {
                updatedLabel += String.valueOf(word.charAt(a));
            }
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
    
    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);        
        
        batch.begin();
        batch.end();
        
        stage.act();
        stage.draw();
        
        /*
        if (screen == MENU) {
            batch.begin();
            font.draw(batch, "[WHITE]ephelant", 100, 500);
            font.draw(batch, "[VIOLET]ephelant", 101, 500);
            batch.end();
        }
        
        //userLabel.setText(word);
        
        if (screen == GAME) {
            //batch.setProjectionMatrix(camera.combined); 
            batch.begin();
            //sprite.draw(batch);
            font.draw(batch, word, 100, 500);
            
            if (ready) {            
                font.draw(batch, buildWord(user), 100, 400);
            }
            
            batch.end();
        }*/
    }
    
    private void handleLeft() {                       
        boolean valid = false;
        int redIndex = (int) shuffled.keySet().toArray()[position]; // Gets the index of the current to-Sort letter
        String redLetter = String.valueOf(user.charAt(position)); // Gets the current to-Sort letter
        int priorIndex = (int) shuffled.keySet().toArray()[position - 1]; // Gets the index of the last placed letter (from the SHUFFLED index)
        
        //System.out.println("redIndex: " + redIndex);
        //System.out.println("redLetter: " + redLetter);
        //System.out.println(priorIndex);

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
            //System.out.println(word.charAt(a));
        }
        
        if (!valid) {
            error.play();
        }              
    }
    
    private void handleRight() {
        boolean valid = false;
        int redIndex = (int) shuffled.keySet().toArray()[position]; // Gets the index of the current to-Sort letter
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
    
    private class KeyProcessor extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.LEFT:
                    if (ready) { handleLeft(); }
                    break;
                case Input.Keys.RIGHT:
                    if (ready) { handleRight(); }
                    break;
                case Input.Keys.SPACE:
                    // need a third state for whether or not in menu, otherwise player could reload this again and again
                    if (wordLoaded) {
                        shuffleLetters();
                        updateWordLabel();        
                        ready = true;
                    }                    
                    
                    if (!wordLoaded) {
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
    
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

// counter showing letters remaining?
// counter can have splash animations
// could even award points for speed - faster you go, bigger anim, more points
// the letter should pulsate
// the letter should pulsate faster and brighter with less remaining time
// at higher levels you have much less time