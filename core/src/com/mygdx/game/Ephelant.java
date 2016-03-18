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
        SCREENS
            2.  GameScreen class (WORK)
    
        BUGS
    
            1.  Handle relative positions!  Roundabout has two U's.  The user
                doesn't know which one I'm using.  The first U does NOT have a
                'd' to its left, but the second U DOES have a 'd' to its left.
    
                When a letter is selected I could probably troll the remaining
                unplaced letters and add ALL letters of a type to an array.
                Then I could execute the logic for each one until yes/no.
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
            
            if (!gameScreen.setupCalled) {
                gameScreen.setup();
                gameScreen.setupCalled = true;
            }                      
            batch.begin();
            batch.end();            
            
            gameScreen.gameScreenStage.draw();            
            gameScreen.gameScreenStage.act();                                    
            
            if (gameScreen.exitGame) {
                screen = MENU;
                startScreen.startGame = false;
                gameScreen.setupCalled = false;
            }            
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
// at higher levels you have much less time