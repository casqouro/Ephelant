package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScreen {
    Stage gameScreenStage;
    private final WordHandler handler;    
    
    private final BitmapFont font;
    private final FreeTypeFontGenerator generator;
    private final FreeTypeFontGenerator.FreeTypeFontParameter parameter;    
    
    private final Label wordLabel;
    private final Label userLabel;   
    
    private final Button newWordButton;
    private final Button readyButton;
    private final Button restartButton;
    private final Button randomtourButton;
    private final Button difficultyButton;
    
    private int DIFFICULTY = 1;
    private final int EASY = 0;
    private final int MEDIUM = 1;
    private final int HARD = 2;
    private int RANDOMTOUR = 0;
    private final int RANDOM = 0;
    private final int TOUR = 1;
    
    private final TimerActor timerActor;    
    
    private final Sound error;
    private final Sound correct;   
    private final Sound click;
    
    private boolean ready = false;
    public boolean exitGame = false;
    public boolean setupCalled = false;    
    
    TextureRegionDrawable easyTexture =     new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//easy.png"))));  
    
    TextureRegionDrawable mediumTexture =   new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//medium.png"))));

    TextureRegionDrawable hardTexture =     new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//hard.png"))));
    
    TextureRegionDrawable randomTexture =     new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//random.png"))));

    TextureRegionDrawable tourTexture =     new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//tour.png"))));
    
    public GameScreen() {
        gameScreenStage = new Stage();
        gameScreenStage.addListener(new gameInputListener());        
        handler = new WordHandler();
        
        // Generates the font using the specific True-Type-Font from assets
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts//Raleway-Medium.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        font = generator.generateFont(parameter);
        font.getData().markupEnabled = true;
        generator.dispose();        
        
        // Sets the label fonts and locations
        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);
        // fontStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(new FileHandle("images//black.png")))) {}; (NOT WORKING?)
        wordLabel = new Label("...", fontStyle);
        wordLabel.setBounds(0, 400, Gdx.graphics.getWidth(), 200);
        wordLabel.setWrap(true);        
        wordLabel.setAlignment(Align.center);     
        userLabel = new Label("...", fontStyle);
        userLabel.setPosition(100, 100);      
        userLabel.setBounds(0, 300, Gdx.graphics.getWidth(), 100);
        userLabel.setWrap(true);
        userLabel.setAlignment(Align.center);      
          
        TextureRegionDrawable newWordTexture =  new TextureRegionDrawable(
                                                  new TextureRegion(
                                                  new Texture(
                                                  new FileHandle("images//newword.png"))));
        TextureRegionDrawable readyTexture =    new TextureRegionDrawable(
                                                  new TextureRegion(
                                                  new Texture(
                                                  new FileHandle("images//ready.png"))));
        TextureRegionDrawable notreadyTexture = new TextureRegionDrawable(
                                                  new TextureRegion(
                                                  new Texture(
                                                  new FileHandle("images//notready.png"))));        
        TextureRegionDrawable restartTexture = new TextureRegionDrawable(
                                                  new TextureRegion(
                                                  new Texture(
                                                  new FileHandle("images//restart.png"))));        
        
        newWordButton = new Button();         
        newWordButton.setStyle(new Button.ButtonStyle(newWordTexture, newWordTexture, newWordTexture));
        newWordButton.setBounds(50, 300, Gdx.graphics.getWidth() - 100, 100);        
        //newWordButton.setBounds(50, 450, Gdx.graphics.getWidth() - 100, 100);
        newWordButton.addListener(new newWordListener());         
        gameScreenStage.addActor(newWordButton);        
        
        readyButton = new Button();
        readyButton.setStyle(new Button.ButtonStyle(notreadyTexture, notreadyTexture, readyTexture));
        readyButton.setBounds(50, 150, Gdx.graphics.getWidth() - 100, 100);
        readyButton.setDisabled(true);
        readyButton.addListener(new readyListener());        
        gameScreenStage.addActor(readyButton);    
        
        restartButton = new Button();
        restartButton.setStyle(new Button.ButtonStyle(restartTexture, restartTexture, restartTexture));
        restartButton.setBounds(50, 150, Gdx.graphics.getWidth() - 100, 100);
        restartButton.setDisabled(true);
        restartButton.addListener(new restartListener()); 
        
        difficultyButton = new Button();
        difficultyButton.setStyle(new Button.ButtonStyle(mediumTexture, mediumTexture, mediumTexture));
        difficultyButton.setBounds(50, 50, 100, 100);
        difficultyButton.addListener(new difficultyListener()); 
        gameScreenStage.addActor(difficultyButton);
        
        randomtourButton = new Button();
        randomtourButton.setStyle(new Button.ButtonStyle(randomTexture, randomTexture, randomTexture));
        randomtourButton.setBounds(150, 50, 100, 100);
        randomtourButton.addListener(new randomtourListener()); 
        gameScreenStage.addActor(randomtourButton);        
                        
        TextureAtlas timerAtlas = new TextureAtlas(Gdx.files.internal("animation//timer.atlas")); 
        Animation timerAnim = new Animation(1 / (timerAtlas.getRegions().size / 10f), timerAtlas.getRegions());   
        timerActor = new TimerActor(timerAnim);   
                
        error = Gdx.audio.newSound(Gdx.files.internal("sounds//error.ogg"));
        correct = Gdx.audio.newSound(Gdx.files.internal("sounds//correct.ogg"));  
        click = Gdx.audio.newSound(Gdx.files.internal("sounds//click.ogg"));  
        
        //newWordTexture.getRegion().getTexture().dispose();
        //readyTexture.getRegion().getTexture().dispose();
        //notreadyTexture.getRegion().getTexture().dispose();
        //timerAtlas.dispose();                
    }
        
    public void setup() {
        gameScreenStage.clear();
        gameScreenStage.addListener(new gameInputListener());
        gameScreenStage.addActor(newWordButton);
        gameScreenStage.addActor(readyButton);  
        readyButton.setDisabled(true);        
        TextureRegionDrawable notreadyTexture =   new TextureRegionDrawable(
                                                  new TextureRegion(
                                                  new Texture(
                                                  new FileHandle("images//notready.png"))));
        readyButton.getStyle().up = notreadyTexture;
        readyButton.getStyle().down = notreadyTexture;        
        gameScreenStage.addActor(difficultyButton);   
        gameScreenStage.addActor(randomtourButton);         
        wordLabel.setText("");
        userLabel.setText("");
        ready = false;        
        timerActor.reset();
    }     
        
    private class newWordListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();            
            
            gameScreenStage.addActor(wordLabel);
            readyButton.setDisabled(false);
            readyButton.getStyle().up = readyButton.getStyle().checked;
            readyButton.getStyle().down = readyButton.getStyle().checked;     
            
            try {
                String word = handler.selectNewWord();                
                wordLabel.setText(word);
            } catch (IOException ex) {
                Logger.getLogger(Ephelant.class.getName()).log(Level.SEVERE, null, ex);
            }                 
        }
    } 
    
    private class readyListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();            
            
            newWordButton.remove();
            readyButton.remove();
            gameScreenStage.addActor(restartButton);
            gameScreenStage.addActor(userLabel);
            gameScreenStage.addActor(timerActor);                
            timerActor.runTimer(); 
            
            setTimer(handler.getWord());
               
            wordLabel.setText(handler.updateWordLabel());
            userLabel.setText(handler.updateUserLabel());
            ready = true;    
        }
    }
        
    private class restartListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();            
            
            gameScreenStage.addActor(newWordButton);
            gameScreenStage.addActor(readyButton);           
            timerActor.reset();    
            timerActor.remove();            
            handler.restart();        
            String word = handler.restart();
            wordLabel.setText(word);  
            ready = true;            
        }   
    }
    
    private void setTimer(String word) {
        int time = DIFFICULTY;
        int length = word.length();                
        switch (time) {
            case EASY:
                System.out.println("easy");
                timerActor.setTimerLength((float) (length + (length * .25))); // needs to get larger                  
                break;
            case MEDIUM:
                timerActor.setTimerLength((float) length);
                break;
            case HARD:
                timerActor.setTimerLength((float) (length - (length * .25))); // needs to get smaller
                break;
        }        
    }
    
    private class difficultyListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();
            
            DIFFICULTY++;
            
            if (DIFFICULTY > 2) {
                DIFFICULTY = 0;
            }
                        
            switch (DIFFICULTY) {
                case 0:
                    difficultyButton.getStyle().up = easyTexture;
                    difficultyButton.getStyle().down = easyTexture;
                    difficultyButton.getStyle().checked = easyTexture;                    
                    break;
                case 1:
                    difficultyButton.getStyle().up = mediumTexture;
                    difficultyButton.getStyle().down = mediumTexture;
                    difficultyButton.getStyle().checked = mediumTexture;                     
                    break;
                case 2:
                    difficultyButton.getStyle().up = hardTexture;
                    difficultyButton.getStyle().down = hardTexture;
                    difficultyButton.getStyle().checked = hardTexture;                     
                    break;
            }            
        }   
    } 
    
    private class randomtourListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();
            
            RANDOMTOUR++;
            
            if (RANDOMTOUR > 1) {
                RANDOMTOUR = 0;
            }
            
            switch (RANDOMTOUR) {
                case 0:
                    randomtourButton.getStyle().up = randomTexture;
                    randomtourButton.getStyle().down = randomTexture;
                    randomtourButton.getStyle().checked = randomTexture;                    
                    break;
                case 1:
                    randomtourButton.getStyle().up = tourTexture;
                    randomtourButton.getStyle().down = tourTexture;
                    randomtourButton.getStyle().checked = tourTexture;                     
                    break;
            }            
        }   
    }    

    public void checkEndConditions() {  
        if (ready) {
            if (timerActor.isDone() || handler.isComplete()) {
                ready = false;     
                timerActor.remove();
                
                // At this point I have to figure out all the logic associated with ending the game
                // Adding a restart button, making things appear/disappear
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
                case Input.Keys.A:
                    if (ready) { 
                        if (handler.handleLeft()) {
                            correct.play();
                            wordLabel.setText(handler.updateWordLabel());
                            userLabel.setText(handler.updateUserLabel());
                        } else {
                            error.play();
                            timerActor.advance();
                        }                    
                    }
                    break;
                case Input.Keys.RIGHT:
                case Input.Keys.D:
                    if (ready) {
                        if (handler.handleRight()) {
                            correct.play();
                            wordLabel.setText(handler.updateWordLabel());
                            userLabel.setText(handler.updateUserLabel());
                        } else {
                            error.play();
                            timerActor.advance();
                        }                    
                    }
                    break;
            }
            return true;
        }        
    }
}