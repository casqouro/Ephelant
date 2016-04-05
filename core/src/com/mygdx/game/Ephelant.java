package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch; 

public class Ephelant extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;   
    
    private int screen = 0;
    private int LANG = 0;
    private static final int MENU = 0;
    private static final int GAME = 1;
    private StartScreen startScreen;
    private GameScreen gameScreen;   
    
    private Texture background; 
         
    /* THINGS TO DO    
        FEATURES
    
        1.  Pressing 'ESC' should pause everything + ask yes/no to quit.
    
        2.  Display locations need to be relative rather than hardcoded.
    
        3.  Add teasing labels when user fails.   
    
        4.  Make multi-language support more robust by adding assets so buttons
            can reflect language choices.
    
        BUGS
        
        1.  Timer: the timer needs to be removed before pasting a completed
                   word to wordLabel, otherwise it overlaps obviously.
    
        2.  Timer: the timer does not stop when a word has been successfully
                   completed.
    
        3.  Relative positions: when duplicate characters exist a number of
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
                
        background = new Texture("images//background.png");        
        camera = new OrthographicCamera(background.getWidth(), background.getHeight());
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
            batch.draw(background, 0, 0);
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