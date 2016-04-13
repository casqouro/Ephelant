package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScreen {
    Stage gameScreenStage;
    private final TextureAtlas everything;    
    private final WordHandler handler;          
    
    private final Label wordLabel;
    private final Label userLabel; 
    private final Label detailLabel;    
    private final Button newWordButton;
    private final Button readyButton;
    private final Button restartButton;
    private final Button difficultyButton;
    private final Button minusplusButton;
    private final Button numbersButton;
    
    // as cool as this is, it's wastey on spacey...
    private int LANG = 0;
    private int DIFFICULTY = 1;
    private final int EASY = 0;
    private final int MEDIUM = 1;
    private final int HARD = 2;
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
    private Table layout;
            
    public GameScreen() {
        gameScreenStage = new Stage();
        gameScreenStage.addListener(new gameInputListener()); 
        everything = new TextureAtlas(Gdx.files.internal("everything.atlas"));
        handler = new WordHandler();
                        
        FileHandle fontHandle = Gdx.files.internal("Raleway-Medium.fnt");
        BitmapFont font = new BitmapFont(fontHandle);
        font.getData().markupEnabled = true;
        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);
        fontStyle.background = new TextureRegionDrawable(everything.findRegion("black"));        

        // Sets the label fonts and locations
        wordLabel = new Label("...", fontStyle);
        wordLabel.setBounds(0, 1, Gdx.graphics.getWidth(), 1);
        // x location, y location, x bounds, y bounds
        //wordLabel.setBounds(0, 400, Gdx.graphics.getWidth(), 200);
        wordLabel.setWrap(true);        
        wordLabel.setAlignment(Align.center);     
        userLabel = new Label("...", fontStyle);
        userLabel.setPosition(100, 100);      
        //userLabel.setBounds(0, 300, Gdx.graphics.getWidth(), 100);
        userLabel.setWrap(true);
        userLabel.setAlignment(Align.center);
        detailLabel = new Label("...", fontStyle);
        detailLabel.setWrap(true);                             
        
        newWordButton = new Button();         
        TextureRegionDrawable newWordTexture =  new TextureRegionDrawable(everything.findRegion("newword"));        
        newWordButton.setStyle(new Button.ButtonStyle(newWordTexture, newWordTexture, newWordTexture));
        newWordButton.addListener(new newWordListener());         
        //newWordButton.setBounds(50, 150, Gdx.graphics.getWidth() - 100, 100);        
        //gameScreenStage.addActor(newWordButton);        
        
        readyButton = new Button();
        TextureRegionDrawable notreadyTexture = new TextureRegionDrawable(everything.findRegion("notready"));        
        TextureRegionDrawable readyTexture = new TextureRegionDrawable(everything.findRegion("ready"));        
        readyButton.setStyle(new Button.ButtonStyle(notreadyTexture, notreadyTexture, readyTexture));        
        readyButton.addListener(new readyListener());        
        //readyButton.setBounds(50, 300, Gdx.graphics.getWidth() - 100, 100);        
        //gameScreenStage.addActor(readyButton);    
        
        restartButton = new Button();
        TextureRegionDrawable restartTexture = new TextureRegionDrawable(everything.findRegion("restart"));        
        restartButton.setStyle(new Button.ButtonStyle(restartTexture, restartTexture, restartTexture));
        //restartButton.setBounds(50, 150, Gdx.graphics.getWidth() - 100, 100);
        restartButton.addListener(new restartListener()); 
        
        difficultyButton = new Button();
        TextureRegionDrawable mediumTexture = new TextureRegionDrawable(everything.findRegion("medium"));
        difficultyButton.setStyle(new Button.ButtonStyle(mediumTexture, mediumTexture, mediumTexture));
        //difficultyButton.setBounds(50, 50, 100, 100);
        difficultyButton.addListener(new difficultyListener()); 
        //gameScreenStage.addActor(difficultyButton);
                
        numbersButton = new Button();
        TextureRegionDrawable number5 = new TextureRegionDrawable(everything.findRegion("number5"));        
        numbersButton.setStyle(new Button.ButtonStyle(number5, number5, number5));
        //numbersButton.setBounds(150, 50, 100, 100);
        numbersButton.addListener(new numbersListener()); 
        //gameScreenStage.addActor(numbersButton);        

        minusplusButton = new Button();
        TextureRegionDrawable plusTexture = new TextureRegionDrawable(everything.findRegion("plus"));        
        minusplusButton.setStyle(new Button.ButtonStyle(plusTexture, plusTexture, plusTexture));
        //minusplusButton.setBounds(250, 50, 100, 100);
        minusplusButton.addListener(new minusplusListener()); 
        //gameScreenStage.addActor(minusplusButton);                      
                        
        TextureAtlas timerAtlas = new TextureAtlas(Gdx.files.internal("timer.atlas")); 
        Animation timerAnim = new Animation(1 / (timerAtlas.getRegions().size / 10f), timerAtlas.getRegions());   
        timerActor = new TimerActor(timerAnim);   
                
        error = Gdx.audio.newSound(Gdx.files.internal("error.ogg"));
        correct = Gdx.audio.newSound(Gdx.files.internal("correct.ogg"));  
        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));     
        
        layout = new Table();
        layout.setFillParent(true);  
        //layout.setDebug(true);
    }
        
    public void setup() {
        gameScreenStage.clear();
        gameScreenStage.addListener(new gameInputListener());
        
        gameScreenStage.addActor(layout);
        
        // table layouts allow for CHANGING the actor in a cell, but not for
        // dynamically adding or removing them healthily
        layout.add(wordLabel).colspan(3).row();
                        
        AtlasRegion newwordRegion = new AtlasRegion(everything.findRegion("newword"));        
        layout.add(newWordButton).prefHeight(newwordRegion.originalHeight)
                                 .prefWidth(newwordRegion.originalWidth)
                                 .minHeight(newwordRegion.originalHeight / 2)
                                 .maxHeight(newwordRegion.originalHeight * 2)
                                 .minWidth(newwordRegion.originalWidth / 2)
                                 .maxWidth(newwordRegion.originalWidth * 2)
                                 .colspan(3);
        layout.row();        
        
        AtlasRegion readyRegion = new AtlasRegion(everything.findRegion("ready"));        
        layout.add(readyButton).prefHeight(readyRegion.originalHeight)
                               .prefWidth(readyRegion.originalWidth)
                               .minHeight(readyRegion.originalHeight / 2)
                               .maxHeight(readyRegion.originalHeight * 2)
                               .minWidth(readyRegion.originalWidth / 2)
                               .maxWidth(readyRegion.originalWidth * 2)
                               .colspan(3);
        readyButton.setDisabled(true);
        readyButton.getStyle().up = new TextureRegionDrawable(everything.findRegion("notready"));
        readyButton.getStyle().down = new TextureRegionDrawable(everything.findRegion("notready"));        
        layout.row();        
        
        AtlasRegion difficultyRegion = new AtlasRegion(everything.findRegion("easy"));
        layout.add(difficultyButton).prefHeight(difficultyRegion.originalHeight / 2)
                                    .prefWidth(difficultyRegion.originalWidth / 3)
                                    .minHeight(difficultyRegion.originalHeight / 2)
                                    .maxHeight(difficultyRegion.originalHeight * 2)
                                    .minWidth(readyRegion.originalWidth / 3)
                                    .maxWidth(readyRegion.originalWidth / 3);
        AtlasRegion numberRegion = new AtlasRegion(everything.findRegion("number5"));   
        layout.add(numbersButton).prefHeight(numberRegion.originalHeight / 2)
                                 .prefWidth(numberRegion.originalWidth / 3)
                                 .minHeight(numberRegion.originalHeight / 2)
                                 .maxHeight(numberRegion.originalHeight * 2)
                                 .minWidth(readyRegion.originalWidth / 3)
                                 .maxWidth(readyRegion.originalWidth / 3);
        AtlasRegion minusplusRegion = new AtlasRegion(everything.findRegion("minus"));   
        layout.add(minusplusButton).prefHeight(minusplusRegion.originalHeight / 2)
                                   .prefWidth(minusplusRegion.originalWidth / 3)
                                   .minHeight(minusplusRegion.originalHeight / 2)
                                   .maxHeight(minusplusRegion.originalHeight * 2)
                                   .minWidth(readyRegion.originalWidth / 3)
                                   .maxWidth(readyRegion.originalWidth / 3);
        
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
                wordLabel.setText(handler.selectNewWord(NUMBER, MINUSPLUS)); 
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
            String word = handler.restart();
            wordLabel.setText(word);  
            ready = false;            
        }   
    }
    
    private void setTimer(String word) {
        int time = DIFFICULTY;
        int length = word.length();             
        switch (time) {
            case EASY:
                //timerActor.setTimerLength((float) (length + (length * .25))); // needs to get larger                  
                timerActor.setTimerLength(500);
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
                timerActor.stopTimer();
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
                    TextureRegionDrawable easyTexture = new TextureRegionDrawable(everything.findRegion("easy"));
                    difficultyButton.getStyle().up = easyTexture;
                    difficultyButton.getStyle().down = easyTexture;
                    difficultyButton.getStyle().checked = easyTexture;                    
                    break;
                case MEDIUM:
                    TextureRegionDrawable mediumTexture = new TextureRegionDrawable(everything.findRegion("medium"));                    
                    difficultyButton.getStyle().up = mediumTexture;
                    difficultyButton.getStyle().down = mediumTexture;
                    difficultyButton.getStyle().checked = mediumTexture;                     
                    break;
                case HARD:
                    TextureRegionDrawable hardTexture = new TextureRegionDrawable(everything.findRegion("hard"));                    
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
                    TextureRegionDrawable number5 = new TextureRegionDrawable(everything.findRegion("number5"));                    
                    numbersButton.getStyle().up = number5;
                    numbersButton.getStyle().down = number5;
                    numbersButton.getStyle().checked = number5;                    
                    break;
                case 6:
                    TextureRegionDrawable number6 = new TextureRegionDrawable(everything.findRegion("number6"));                    
                    numbersButton.getStyle().up = number6;
                    numbersButton.getStyle().down = number6;
                    numbersButton.getStyle().checked = number6;                     
                    break;
                case 7:
                    TextureRegionDrawable number7 = new TextureRegionDrawable(everything.findRegion("number7"));                    
                    numbersButton.getStyle().up = number7;
                    numbersButton.getStyle().down = number7;
                    numbersButton.getStyle().checked = number7;                    
                    break;
                case 8:
                    TextureRegionDrawable number8 = new TextureRegionDrawable(everything.findRegion("number8"));                    
                    numbersButton.getStyle().up = number8;
                    numbersButton.getStyle().down = number8;
                    numbersButton.getStyle().checked = number8;                     
                    break;
                case 9:
                    TextureRegionDrawable number9 = new TextureRegionDrawable(everything.findRegion("number9"));                    
                    numbersButton.getStyle().up = number9;
                    numbersButton.getStyle().down = number9;
                    numbersButton.getStyle().checked = number9;                    
                    break;
                case 10:
                    TextureRegionDrawable number10 = new TextureRegionDrawable(everything.findRegion("number10"));                    
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
                    TextureRegionDrawable minusTexture = new TextureRegionDrawable(everything.findRegion("minus"));                    
                    minusplusButton.getStyle().up = minusTexture;
                    minusplusButton.getStyle().down = minusTexture;
                    minusplusButton.getStyle().checked = minusTexture;                    

                    if (handler.getWord() != null && handler.getWord().length() > NUMBER) {
                        try {
                            wordLabel.setText(handler.selectNewWord(NUMBER, MINUSPLUS));
                        } catch (IOException ex) {
                            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case PLUS:
                    TextureRegionDrawable plusTexture = new TextureRegionDrawable(everything.findRegion("plus"));                    
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
    
    public void setLang(int a) {
        LANG = a;
        handler.setLang(LANG);
    }
}