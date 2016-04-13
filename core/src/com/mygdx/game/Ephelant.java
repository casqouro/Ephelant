package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch; 
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ephelant extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;   
    
    private int screen = 0;
    private int LANG = 0;
    private static final int MENU = 0;
    private static final int GAME = 1;
    private StartScreen startScreen;
    private GameScreen gameScreen;   
         
    /* THINGS TO DO    
        FEATURES
    
        FXD.  Use texture atlases instead of base inclusions
    
        0.  Use block letters instead of lower-case letters
    
        1.  Pressing 'ESC' should pause everything + ask yes/no to quit.
            Maybe not because:
            a) the game might be able to be condensed into a single screen
            b) from a mobile perspective one button press is all it takes to
               indicate that's what the user wants
    
        2.  Display locations and sizes need to be relative rather than hardcoded.
    
        3.  Add teasing labels when user fails.   
    
        4.  Make multi-language support more robust by adding assets so buttons
            can reflect language choices.
            
        5.  Needs a tutorial or explanation
    
        6.  Identify time being up somehow by adding:
            a) adding a sound
            b) display the word with completed letters in white, and incomplete
               letters in red
            c) display a big "TIME'S UP"
            d) display teasing messages like:
                "What's a [WORD]?"
                "My grandma makes [WORD] too!"
    
        7.  SCORE.  SCORE.  SCORE.  There must be a score.
            Local scores as well as global scores.
    
        8.  Different modes, such as:
            Tour - giving the player words to spell until they fail
            Timed - how many words can you spell in X seconds
            Score - spell words until reaching a specific score
    
        9.  Underline or BOX the highlighted letter
    
        BUGS (FXD = FIXED) (WNF = WILL NOT FIX)
    
        IMPORTANT: switching to minus crashes when first starting up

        FXD - Crash if plus/minus button is clicked before anything else
              RESULT: minusplusListener now checks to see if handler.getWord() is null
        FXD - shuffleLetters() is being called twice on restart.
              RESULT: restartListener was calling shuffle twice        
        FXD - Timer: the timer needs to be removed before pasting a completed
                     word to wordLabel, otherwise it overlaps.
              RESULT: checkEndConditions() now stops and removes the timer. 
        FXD - The timer does not stop when a word is completed.
              RESULT: checkEndConditions() now stops and removes the timer.

        WNF.  Relative positions: when duplicate characters exist a number of
                                issues crop up.
    
                                (A) Left and Right can both be valid answers.
                                    Ex: "ROUNDABOUT" has two 'U' characters.
                                        The 'D' character is both to the left of
                                        a 'U' and to the right of a 'U'.
    
                                (B) Trying to solve (A) by adding the letters to
                                    an array brings up the issue of 'identity'.
                                    If neighboring letters have been placed,
                                    then a duplicate letter may be 'identified'
                                    and is no longer really a duplicate.  This
                                    issue can even extend to groups, like the
                                    two 'OU' groups in "ROUNDABOUT".
    
                                    However, I think identity can be solved by
                                    checking if any letters between duplicates
                                    has been placed.  If so, then both letters
                                    are identifiable (excepting cases like 'UO'
                                    in "ROUNDABOUT", of which there are two.
    
                                (C) Sometimes pressing LEFT will highlight a
                                    character to the right, and vice versa.
    
                                    This only happens when attempting to solve
                                    the duplicates issue.
    
                                CONCLUSION: don't use any words with duplicate
                                            characters
    
                                            I'd love to solve this programming
                                            challenge but I have limited time.    
*/
    
    @Override
    public void create() {
        batch = new SpriteBatch();                                      
                         
        camera = new OrthographicCamera(1164, 1164);
        camera.position.set(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f, 0);
        camera.update();                                                   
        
        gameScreen = new GameScreen();        
        startScreen = new StartScreen(gameScreen);          
    }
                                     
    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                        
        batch.setProjectionMatrix(camera.combined);
        
        if (screen == MENU) {                     
            Gdx.input.setInputProcessor(startScreen.startScreenStage);
            batch.begin();
            batch.end();
            startScreen.startScreenStage.act();
            startScreen.startScreenStage.draw();    
            
            if (startScreen.startGame) {
                screen = GAME;
                gameScreen.exitGame = false;
            } 
        }
        
        if (screen == GAME) {
            Gdx.input.setInputProcessor(gameScreen.gameScreenStage); 
            
            if (gameScreen.exitGame) {
                screen = MENU;
                startScreen.startGame = false;
                gameScreen.setupCalled = false;
            }            
            
            if (startScreen.startGame && !gameScreen.setupCalled) {
                gameScreen.setup();
                gameScreen.setupCalled = true;
            }                        
            
            gameScreen.isOver();
            gameScreen.checkEndConditions();
            gameScreen.gameScreenStage.act();             
            gameScreen.gameScreenStage.draw();                                                            
        }
    }
    
    @Override
    public void dispose() {
        batch.dispose();
    }
}

// counter showing letters remaining?
// counter can have splash animations
// could even award points for speed - faster you go, bigger anim, more points
// the letter should pulsate
// the letter should pulsate faster and brighter with less remaining time