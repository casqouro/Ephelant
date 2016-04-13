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
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScreen {
    Stage gameScreenStage;
    private final TextureAtlas everything;    
    private final WordHandler handler;  
    private final Table layout;  
    private final HorizontalGroup hGroup;    
    private final TimerActor timerActor;  
    private final HashMap<String, Label> letterMap;
    
    private final Label wordLabel;
    private final Label userLabel; 
    private final Label detailLabel; 
    private final Label letter0;
    private final Label letter1;
    private final Label letter2;
    private final Label letter3;
    private final Label letter4; 
    private final Label letter5;
    private final Label letter6;
    private final Label letter7;
    private final Label letter8;
    private final Label letter9;    
    private final Button newWordButton;
    private final Button readyButton;
    private final Button restartButton;
    private final Button difficultyButton;
    private final Button minusplusButton;
    private final Button numbersButton;
        
    private int LANG = 0;
    private int DIFFICULTY = 1;
    private final int EASY = 0;
    private final int MEDIUM = 1;
    private final int HARD = 2;
    private int NUMBER = 5;
    private int MINUSPLUS = 1;
    private final int MINUS = 0;
    private final int PLUS = 1;        
    
    private final Sound error;
    private final Sound correct;   
    private final Sound click;
    
    private boolean ready = false;
    public boolean exitGame = false;
    public boolean setupCalled = false; 
            
    public GameScreen() {
        gameScreenStage = new Stage();
        gameScreenStage.addListener(new gameInputListener()); 
        everything = new TextureAtlas(Gdx.files.internal("everything.atlas"));
        handler = new WordHandler();
        layout = new Table();
        layout.setFillParent(true);  
        layout.setDebug(true);
        hGroup = new HorizontalGroup();        
                        
        FileHandle fontHandle = Gdx.files.internal("Raleway-Medium.fnt");
        BitmapFont font = new BitmapFont(fontHandle);
        font.getData().markupEnabled = true;
        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);
        fontStyle.background = new TextureRegionDrawable(everything.findRegion("black"));        

        wordLabel = new Label("...", fontStyle);
        //wordLabel.setBounds(0, 400, Gdx.graphics.getWidth(), 200);
        wordLabel.setWrap(true);        
        wordLabel.setAlignment(Align.center);     
        userLabel = new Label("...", fontStyle);
        //userLabel.setPosition(100, 100);      
        //userLabel.setBounds(0, 300, Gdx.graphics.getWidth(), 100);
        userLabel.setWrap(true);
        userLabel.setAlignment(Align.center);
        detailLabel = new Label("...", fontStyle);
        detailLabel.setWrap(true);    
        
        LabelStyle backgroundStyle = new LabelStyle();
        backgroundStyle.font = font;
        backgroundStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture((Gdx.files.internal("background.png")))));
        letter0 = new Label("", backgroundStyle);
        letter0.setAlignment(Align.center);
        letter1 = new Label("", backgroundStyle);        
        letter1.setAlignment(Align.center);        
        letter2 = new Label("", backgroundStyle);        
        letter2.setAlignment(Align.center);
        letter3 = new Label("", backgroundStyle);        
        letter3.setAlignment(Align.center);        
        letter4 = new Label("", backgroundStyle);        
        letter4.setAlignment(Align.center);
        letter5 = new Label("", backgroundStyle);        
        letter5.setAlignment(Align.center);
        letter6 = new Label("", backgroundStyle);        
        letter6.setAlignment(Align.center);
        letter7 = new Label("", backgroundStyle);        
        letter7.setAlignment(Align.center);
        letter8 = new Label("", backgroundStyle);        
        letter8.setAlignment(Align.center);
        letter9 = new Label("", backgroundStyle);        
        letter9.setAlignment(Align.center); 
        letterMap = new HashMap<>();
        letterMap.put("letter0", letter0);
        letterMap.put("letter1", letter1);
        letterMap.put("letter2", letter2);
        letterMap.put("letter3", letter3);
        letterMap.put("letter4", letter4);
        letterMap.put("letter5", letter5);
        letterMap.put("letter6", letter6);
        letterMap.put("letter7", letter7);                
        letterMap.put("letter8", letter8);        
        letterMap.put("letter9", letter9);                              
        
        newWordButton = new Button();         
        TextureRegionDrawable newWordTexture =  new TextureRegionDrawable(everything.findRegion("newword"));        
        newWordButton.setStyle(new Button.ButtonStyle(newWordTexture, newWordTexture, newWordTexture));
        newWordButton.addListener(new newWordListener());         
        //newWordButton.setBounds(50, 150, Gdx.graphics.getWidth() - 100, 100);              
        
        readyButton = new Button();
        TextureRegionDrawable notreadyTexture = new TextureRegionDrawable(everything.findRegion("notready"));        
        TextureRegionDrawable readyTexture = new TextureRegionDrawable(everything.findRegion("ready"));        
        readyButton.setStyle(new Button.ButtonStyle(notreadyTexture, notreadyTexture, readyTexture));        
        readyButton.addListener(new readyListener());        
        //readyButton.setBounds(50, 300, Gdx.graphics.getWidth() - 100, 100);           
        
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
        
        numbersButton = new Button();
        TextureRegionDrawable number5 = new TextureRegionDrawable(everything.findRegion("number5"));        
        numbersButton.setStyle(new Button.ButtonStyle(number5, number5, number5));
        //numbersButton.setBounds(150, 50, 100, 100);
        numbersButton.addListener(new numbersListener());        

        minusplusButton = new Button();
        TextureRegionDrawable plusTexture = new TextureRegionDrawable(everything.findRegion("plus"));        
        minusplusButton.setStyle(new Button.ButtonStyle(plusTexture, plusTexture, plusTexture));
        //minusplusButton.setBounds(250, 50, 100, 100);
        minusplusButton.addListener(new minusplusListener());
                                       
        TextureAtlas timerAtlas = new TextureAtlas(Gdx.files.internal("timer.atlas")); 
        Animation timerAnim = new Animation(1 / (timerAtlas.getRegions().size / 10f), timerAtlas.getRegions());   
        timerActor = new TimerActor(timerAnim);   
                
        error = Gdx.audio.newSound(Gdx.files.internal("error.ogg"));
        correct = Gdx.audio.newSound(Gdx.files.internal("correct.ogg"));  
        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));             
    }
        
    public void tableSetup() {
        gameScreenStage.clear();
        gameScreenStage.addListener(new gameInputListener());
        layout.clear();  
        gameScreenStage.addActor(layout);
        hGroup.clear();
        layout.add(hGroup).expandX().colspan(4);
        
        hGroup.addActor(letter0);
        hGroup.addActor(letter1);
        hGroup.addActor(letter2);
        hGroup.addActor(letter3);
        hGroup.addActor(letter4);
        hGroup.addActor(letter5);
        hGroup.addActor(letter6);        
        hGroup.addActor(letter7);
        hGroup.addActor(letter8);
        hGroup.addActor(letter9);
        layout.row();
                                        
        AtlasRegion newwordRegion = new AtlasRegion(everything.findRegion("newword"));        
        layout.add(newWordButton).prefHeight(newwordRegion.originalHeight)
                                 .prefWidth(newwordRegion.originalWidth)
                                 .minHeight(newwordRegion.originalHeight / 2)
                                 .maxHeight(newwordRegion.originalHeight * 2)
                                 .minWidth(newwordRegion.originalWidth / 2)
                                 .maxWidth(newwordRegion.originalWidth * 2)
                                 .colspan(2);                
        AtlasRegion readyRegion = new AtlasRegion(everything.findRegion("ready"));        
        layout.add(readyButton).prefHeight(readyRegion.originalHeight)
                               .prefWidth(readyRegion.originalWidth)
                               .minHeight(readyRegion.originalHeight / 2)
                               .maxHeight(readyRegion.originalHeight * 2)
                               .minWidth(readyRegion.originalWidth / 2)
                               .maxWidth(readyRegion.originalWidth * 2)
                               .colspan(1);
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
            
            String word = "";
            try { 
                word = (handler.selectNewWord(NUMBER, MINUSPLUS)); 
            } catch (IOException ex) {
                Logger.getLogger(Ephelant.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            hGroup.clear();            
            for (int a = 0; a < word.length(); a++) {
                letterMap.get("letter" + String.valueOf(a)).setText(String.valueOf(word.charAt(a)));
                hGroup.addActor(letterMap.get("letter" + String.valueOf(a)));
            }
            
            readyButton.getStyle().up = readyButton.getStyle().checked;
            readyButton.getStyle().down = readyButton.getStyle().checked;   
            readyButton.setDisabled(false);                            
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
                //gameScreenStage.addActor(userLabel);
                gameScreenStage.addActor(timerActor);                
                timerActor.runTimer(); 

                setTimer(handler.getWord());

                String update = handler.updateUserLabel();
                for (int a = 0; a < letterMap.size(); a++) {
                    
                }
                
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