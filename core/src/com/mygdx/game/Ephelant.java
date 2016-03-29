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
    private static final int MENU = 0;
    private static final int GAME = 1;
    private StartScreen startScreen;
    private GameScreen gameScreen;   
    
    private Texture background; 
         
    /* THINGS TO DO       
        BUGS
    
            2.  Handle relative positions!  Roundabout has two U's.  The user
                doesn't know which one I'm using.  The first U does NOT have a
                'd' to its left, but the second U DOES have a 'd' to its left.
    
                When a letter is selected I could probably troll the remaining
                unplaced letters and add ALL letters of a type to an array.
                Then I could execute the logic for each one until yes/no.
    
                [UPDATE] - more complex than I thought and perhaps even
                unfixable with the current design.
    
            3.  Rare bug with extra letters appearing.
                
                [UPDATE] - this is related to the relative positions bug.
                If you have "Pitiful", you have p-i-i.  When determining what
                color to mark letters the FOR loop will mark both of them.    
    
        FEATURES
    
            1.  Pressing 'ESC' should pause everything + ask yes/no to quit.
            2.  Display locations need to be relative rather than hardcoded.
            3.  Add teasing labels when user fails.
*/
    
    @Override
    public void create() {
        batch = new SpriteBatch();                                      
                
        background = new Texture("images//background.png");        
        camera = new OrthographicCamera(background.getWidth(), background.getHeight());
        camera.position.set(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f, 0);
        camera.update();                                                   
        
        startScreen = new StartScreen();   
        gameScreen = new GameScreen();       
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