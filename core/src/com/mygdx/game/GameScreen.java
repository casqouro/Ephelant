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
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private final Label detailLabel;
    
    private final Button newWordButton;
    private final Button readyButton;
    private final Button restartButton;
    private final Button randomtourButton;
    private final Button difficultyButton;
    private final Button minusplusButton;
    private final Button numbersButton;
    
    // as cool as this is, it's wastey on spacey...
    private int DIFFICULTY = 1;
    private final int EASY = 0;
    private final int MEDIUM = 1;
    private final int HARD = 2;
    private int RANDOMTOUR = 0;
    private final int RANDOM = 0;
    private final int TOUR = 1;
    private int NUMBER = 5;
    private int MINUSPLUS = 1;
    private final int MINUS = 0;
    private final int PLUS = 1;
    
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
    
    TextureRegionDrawable randomTexture =   new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//random.png"))));

    TextureRegionDrawable tourTexture =     new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//tour.png"))));
    
    TextureRegionDrawable number5 =         new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//number5.png"))));

    TextureRegionDrawable number6 =         new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//number6.png"))));
        
    TextureRegionDrawable number7 =         new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//number7.png"))));

    TextureRegionDrawable number8 =         new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//number8.png"))));

    TextureRegionDrawable number9 =         new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//number9.png"))));

    TextureRegionDrawable number10 =        new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//number10.png"))));
    
    TextureRegionDrawable minusTexture =    new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//minus.png"))));

    TextureRegionDrawable plusTexture =     new TextureRegionDrawable(
                                            new TextureRegion(
                                            new Texture(
                                            new FileHandle("images//plus.png"))));
    
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
        fontStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(new FileHandle("images//black.png"))));
        wordLabel = new Label("...", fontStyle);
        wordLabel.setBounds(0, 400, Gdx.graphics.getWidth(), 200);
        wordLabel.setWrap(true);        
        wordLabel.setAlignment(Align.center);     
        userLabel = new Label("...", fontStyle);
        userLabel.setPosition(100, 100);      
        userLabel.setBounds(0, 300, Gdx.graphics.getWidth(), 100);
        userLabel.setWrap(true);
        userLabel.setAlignment(Align.center);
        detailLabel = new Label("...", fontStyle);
        detailLabel.setWrap(true);
          
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
        newWordButton.setBounds(50, 150, Gdx.graphics.getWidth() - 100, 100);
        newWordButton.addListener(new newWordListener());         
        gameScreenStage.addActor(newWordButton);        
        
        readyButton = new Button();
        readyButton.setStyle(new Button.ButtonStyle(notreadyTexture, notreadyTexture, readyTexture));
        readyButton.setBounds(50, 300, Gdx.graphics.getWidth() - 100, 100);        
        readyButton.addListener(new readyListener());        
        gameScreenStage.addActor(readyButton);    
        
        restartButton = new Button();
        restartButton.setStyle(new Button.ButtonStyle(restartTexture, restartTexture, restartTexture));
        restartButton.setBounds(50, 150, Gdx.graphics.getWidth() - 100, 100);
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
        //gameScreenStage.addActor(randomtourButton);
        
        numbersButton = new Button();
        numbersButton.setStyle(new Button.ButtonStyle(number5, number5, number5));
        numbersButton.setBounds(150, 50, 100, 100);
        numbersButton.addListener(new numbersListener()); 
        gameScreenStage.addActor(numbersButton);        

        minusplusButton = new Button();
        minusplusButton.setStyle(new Button.ButtonStyle(plusTexture, plusTexture, plusTexture));
        minusplusButton.setBounds(250, 50, 100, 100);
        minusplusButton.addListener(new minusplusListener()); 
        gameScreenStage.addActor(minusplusButton);                      
                        
        TextureAtlas timerAtlas = new TextureAtlas(Gdx.files.internal("animation//timer.atlas")); 
        Animation timerAnim = new Animation(1 / (timerAtlas.getRegions().size / 10f), timerAtlas.getRegions());   
        timerActor = new TimerActor(timerAnim);   
                
        error = Gdx.audio.newSound(Gdx.files.internal("sounds//error.ogg"));
        correct = Gdx.audio.newSound(Gdx.files.internal("sounds//correct.ogg"));  
        click = Gdx.audio.newSound(Gdx.files.internal("sounds//click.ogg"));                        
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
        //gameScreenStage.addActor(randomtourButton);
        gameScreenStage.addActor(minusplusButton);
        gameScreenStage.addActor(numbersButton);
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
            readyButton.getStyle().up = readyButton.getStyle().checked;
            readyButton.getStyle().down = readyButton.getStyle().checked;   
            readyButton.setDisabled(false);
            
            try {
                String word = handler.selectNewWord(NUMBER, MINUSPLUS);                
                wordLabel.setText(word);
            } catch (IOException ex) {
                Logger.getLogger(Ephelant.class.getName()).log(Level.SEVERE, null, ex);
            }                 
        }
    } 
    
    private class readyListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (!readyButton.isDisabled()) {
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
                readyButton.setChecked(false);
            }
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
    
    public Boolean isOver() {
        if (difficultyButton.isOver() || numbersButton.isOver() || minusplusButton.isOver()) {
            displayDetail();
            return true;
        }
        return true;
    }   
    
    public void checkEndConditions() {  
        if (ready) {
            if (timerActor.isDone() || handler.isComplete()) {
                ready = false;     
                timerActor.remove();
            }            
        }
    }    
    
    private void displayDetail() {
            gameScreenStage.addActor(detailLabel);
            detailLabel.setFontScale(0.6f);
            detailLabel.setBounds(25, 200, Gdx.graphics.getWidth() - 25, 150);
            
            if (difficultyButton.isOver()) {
                difficultyButton.toFront();
                
                String easy = "Easy:         +25% time\n"
                            + "                   No penalties\n";
                String medium = "Medium:   Normal\n";
                String hard = "Hard:         -25% time";
                String collated = "";
                
                switch (DIFFICULTY) {
                    case EASY:
                        collated = "[GREEN]" + easy + "[WHITE]" + medium + hard;
                        break;
                    case MEDIUM:
                        collated = "[WHITE]" + easy + "[GREEN]" + medium + "[WHITE]" + hard;
                        break;
                    case HARD:
                        collated = "[WHITE]" + easy + medium + "[GREEN]" + hard;
                        break;
                }

                detailLabel.setText(collated);
            }
            
            if (randomtourButton.isOver()) {
                randomtourButton.toFront();                
                detailLabel.setText("Random: any word from the whole set\n"
                                  + "Tour: ten random words, repeated\n");
            }
            
            if (numbersButton.isOver() || minusplusButton.isOver()) {
                numbersButton.toFront();
                
                String value = Integer.toString(NUMBER);
                String operator = "+";
                
                if (MINUSPLUS == 0) {
                    operator = "-";
                }
                
                detailLabel.setText("Minimum or maximum word size\n"
                                  + "Ex: 8+ is words of 8-10 letters\n"
                                  + "Ex: 6- is words of 5-6 letters\n\n"
                                  + "Currently set at:  [GREEN]" + value + operator);
            }            
    }
    
    private void removeDetail() {
        detailLabel.remove();
    }     
    
    private class difficultyListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();
            
            DIFFICULTY++;
            
            if (DIFFICULTY > HARD) {
                DIFFICULTY = EASY;
            }
                        
            switch (DIFFICULTY) {
                case EASY:
                    difficultyButton.getStyle().up = easyTexture;
                    difficultyButton.getStyle().down = easyTexture;
                    difficultyButton.getStyle().checked = easyTexture;                    
                    break;
                case MEDIUM:
                    difficultyButton.getStyle().up = mediumTexture;
                    difficultyButton.getStyle().down = mediumTexture;
                    difficultyButton.getStyle().checked = mediumTexture;                     
                    break;
                case HARD:
                    difficultyButton.getStyle().up = hardTexture;
                    difficultyButton.getStyle().down = hardTexture;
                    difficultyButton.getStyle().checked = hardTexture;                     
                    break;
            }            
        }          
        
        /* Keeping this prevents a render-flicker, where for an instant the
           detail pop-up is not displayed, so the background pokes through.
        
           So even though there's a method in the main render() function, 
           maintaining this method in every button in the tool-tip line makes
           moving between them appear much smoother.
        */
        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {   
            displayDetail();
        }        
                
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (!difficultyButton.isOver() && !numbersButton.isOver() && !minusplusButton.isOver()) {
                removeDetail();
            }
        }
    } 
            
    private class randomtourListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();
            
            RANDOMTOUR++;
            
            if (RANDOMTOUR > TOUR) {
                RANDOMTOUR = RANDOM;
            }
            
            switch (RANDOMTOUR) {
                case RANDOM:
                    randomtourButton.getStyle().up = randomTexture;
                    randomtourButton.getStyle().down = randomTexture;
                    randomtourButton.getStyle().checked = randomTexture;                    
                    break;
                case TOUR:
                    randomtourButton.getStyle().up = tourTexture;
                    randomtourButton.getStyle().down = tourTexture;
                    randomtourButton.getStyle().checked = tourTexture;                     
                    break;
            }            
        }   
        
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) { 
            if (!randomtourButton.isOver()) {
                removeDetail();
            }
        }        
    }   
    
    private class numbersListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();
            
            NUMBER++;
            
            if (NUMBER > 10) {
                NUMBER = 5;
            }
            
            switch (NUMBER) {
                case 5:
                    numbersButton.getStyle().up = number5;
                    numbersButton.getStyle().down = number5;
                    numbersButton.getStyle().checked = number5;                    
                    break;
                case 6:
                    numbersButton.getStyle().up = number6;
                    numbersButton.getStyle().down = number6;
                    numbersButton.getStyle().checked = number6;                     
                    break;
                case 7:
                    numbersButton.getStyle().up = number7;
                    numbersButton.getStyle().down = number7;
                    numbersButton.getStyle().checked = number7;                    
                    break;
                case 8:
                    numbersButton.getStyle().up = number8;
                    numbersButton.getStyle().down = number8;
                    numbersButton.getStyle().checked = number8;                     
                    break;
                case 9:
                    numbersButton.getStyle().up = number9;
                    numbersButton.getStyle().down = number9;
                    numbersButton.getStyle().checked = number9;                    
                    break;
                case 10:
                    numbersButton.getStyle().up = number10;
                    numbersButton.getStyle().down = number10;
                    numbersButton.getStyle().checked = number10;                    
                    break;                    
            }            
        }
        
        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {   
            displayDetail();
        }
        
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) { 
            if (!difficultyButton.isOver() && !numbersButton.isOver() && !minusplusButton.isOver()) {
                removeDetail();
            }
        }        
    }                
    
    private class minusplusListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();
            
            MINUSPLUS++;
            
            if (MINUSPLUS > PLUS) {
                MINUSPLUS = MINUS;
            }
            
            switch (MINUSPLUS) {
                case MINUS:
                    minusplusButton.getStyle().up = minusTexture;
                    minusplusButton.getStyle().down = minusTexture;
                    minusplusButton.getStyle().checked = minusTexture;                    
                    break;
                case PLUS:
                    minusplusButton.getStyle().up = plusTexture;
                    minusplusButton.getStyle().down = plusTexture;
                    minusplusButton.getStyle().checked = plusTexture;                     
                    break;                   
            }            
        }
        
        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {   
            displayDetail();
        }        
        
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) { 
            if (!difficultyButton.isOver() && !numbersButton.isOver() && !minusplusButton.isOver()) {
                removeDetail();
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
                            if (DIFFICULTY != EASY) {
                                timerActor.advance();
                            }
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
                            if (DIFFICULTY != EASY) {
                                timerActor.advance();
                            }
                        }                    
                    }
                    break;
            }
            return true;
        }        
    }
}