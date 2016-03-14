package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
    private String sort;
    private int sortIndex;
    
    private int[] state;
    private int position;
    
    private boolean wordLoaded;
    
    // Instead of randomly selecting a letter each turn...
    // Shuffle the word at the beginning, like with cards.
    // I know the algorithm and it'll simplify life.
    // Sort it as a map though.  The MAP index will be the randomized file.
    // The INTEGER value will be the sorted word index, String will be the char.

    // Need to figure the design.
    
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
        
        word = "";
        user = "";
        state = new int[0];
        position = 0;
        wordLoaded = false;        
    }
        
    // Get a random word from the wordlist
    private void startNewWord() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("..\\assets\\words\\wordlist.txt")); 
        Random rand = new Random();
        word = "";
        user = "";
        position = 0;
        
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
                    user = token[1];
                    size--;
                }
            }
        } else {
            System.out.println("The list is empty, and I should be doing something useful like handling an exception.");
        }
        
        state = new int[word.length()];
        for (int a = 0; a < state.length; a++) {
            changeLetterColor(a, WHITE);
        }
    }    
    
    public void selectFirstLetter() {
            Random rand = new Random();
            int letter = rand.nextInt(word.length() - position);
            changeLetterColor(letter, 1);  
                        
            Map<Integer, String> map = new HashMap<>();
            for (int a = 0; a < state.length; a++) {
                if (state[a] == WHITE) {
                    map.put(a, String.valueOf(user.charAt(a)));
                }
            }

            sortIndex = rand.nextInt(map.size());
            sort = map.remove(sortIndex); // error, try splitting
            System.out.println(sort);
            changeLetterColor(sortIndex, RED);
                        
            // uh, now what?
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
            position++;
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
        
    }
           
    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);        
        
        //batch.setProjectionMatrix(camera.combined); 
        batch.begin();
        //sprite.draw(batch);
        font.draw(batch, word, 100, 500);
        font.draw(batch, buildWord(user), 100, 400);
        batch.end();
    }
    
    private class KeyProcessor extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.LEFT:
                    handleLeft();
                    break;
                case Input.Keys.RIGHT:
                    System.out.println("right press");
                    break;
                case Input.Keys.SPACE:
                    // need a third state for whether or not in menu, otherwise player could reload this again and again
                    if (wordLoaded) {
                        selectFirstLetter();
                    }                    
                    
                    if (!wordLoaded) {
                        try {
                            startNewWord();
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