package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScreen {
    Stage gameScreenStage;
    private final TextureAtlas everything;    
    private final WordHandler handler;  
    private final Table layout;  
    private final HorizontalGroup letterGroup;  
    private final HorizontalGroup answerGroup;
    private final HorizontalGroup buttonGroup;
    private final HorizontalGroup settingGroup;
    private final TimerActor timerActor;  
    private final HashMap<String, AnimatedImageTextButton> buttonMap;
     
    private final Stack stack;
    private final Label detailLabel;  
    private final HorizontalGroup detailGroup;
    private final Stack detailStack;
    private final ImageTextButton nextLetter;
    private final TextButton newWordButton;
    private final TextButton readyButton;
    private final TextButton restartButton;
    private final Button difficultyButton;
    private final Button minusplusButton;
    private final Button numbersButton;
    
    private final AnimatedImageTextButton button0;
    private final AnimatedImageTextButton button1;
    private final AnimatedImageTextButton button2;
    private final AnimatedImageTextButton button3;
    private final AnimatedImageTextButton button4;    
    private final AnimatedImageTextButton button5;
    private final AnimatedImageTextButton button6;
    private final AnimatedImageTextButton button7;
    private final AnimatedImageTextButton button8;
    private final AnimatedImageTextButton button9;    
        
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

    ImageTextButtonStyle style;
    private int highlightReference;
    
    private Image firework;

    public GameScreen() {
        gameScreenStage = new Stage();
        gameScreenStage.addListener(new gameInputListener()); 
        everything = new TextureAtlas(Gdx.files.internal("everything.atlas"));
        handler = new WordHandler();
        layout = new Table();
        layout.setFillParent(true);          
        stack = new Stack();
        layout.setDebug(true);
                
        int padding = 50;
        int paddingTotal = 50 * 4; // helps determine Stack positioning
        int spacing = 5;
        letterGroup = new HorizontalGroup().padTop(padding).padBottom(padding).space(spacing);
        answerGroup = new HorizontalGroup().padTop(padding).padBottom(padding).space(spacing);
        buttonGroup = new HorizontalGroup();
        settingGroup = new HorizontalGroup().padTop(15);     
                             
        BitmapFont font = new BitmapFont(Gdx.files.internal("raleway34.fnt"), everything.findRegion("raleway34")); 
        font.getData().markupEnabled = true;                
        BitmapFont gameplayFont = new BitmapFont(Gdx.files.internal("raleway40.fnt"), everything.findRegion("raleway40"));
        gameplayFont.getData().markupEnabled = true;
        Label.LabelStyle fontStyle = new Label.LabelStyle(gameplayFont, Color.WHITE);        
        fontStyle.background = new TextureRegionDrawable(everything.findRegion("black"));        
        
        detailLabel = new Label("\n\n\n", fontStyle);    
        detailLabel.setFontScale(0.6f);
        
        TextureAtlas violetAtlas = new TextureAtlas(Gdx.files.internal("violetshot.atlas"));
        Animation violetAnim = new Animation(1 / 7f, violetAtlas.getRegions());
        violetAnim.setPlayMode(Animation.PlayMode.LOOP);
        TextureAtlas blueAtlas = new TextureAtlas(Gdx.files.internal("blueshot.atlas"));
        Animation blueAnim = new Animation(1 / 7f, blueAtlas.getRegions());
        blueAnim.setPlayMode(Animation.PlayMode.LOOP);
        
        firework = new AnimatedImage(new AnimatedDrawable(violetAnim));
        
        TextureRegionDrawable newbgr = new TextureRegionDrawable(everything.findRegion("newbgr"));           
        TextureAtlas highlightAtlas = new TextureAtlas(Gdx.files.internal("highlight.atlas"));
        final Animation highlightAnim = new Animation(1 / 4f, highlightAtlas.getRegions()); 
        highlightAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        AnimatedDrawable adtest = new AnimatedDrawable(highlightAnim);

        button0 = new AnimatedImageTextButton("", new ImageTextButtonStyle(newbgr, adtest, newbgr, gameplayFont));    
        button1 = new AnimatedImageTextButton("", new ImageTextButtonStyle(newbgr, adtest, newbgr, gameplayFont));
        button2 = new AnimatedImageTextButton("", new ImageTextButtonStyle(newbgr, adtest, newbgr, gameplayFont));
        button3 = new AnimatedImageTextButton("", new ImageTextButtonStyle(newbgr, adtest, newbgr, gameplayFont));
        button4 = new AnimatedImageTextButton("", new ImageTextButtonStyle(newbgr, adtest, newbgr, gameplayFont));
        button5 = new AnimatedImageTextButton("", new ImageTextButtonStyle(newbgr, adtest, newbgr, gameplayFont));
        button6 = new AnimatedImageTextButton("", new ImageTextButtonStyle(newbgr, adtest, newbgr, gameplayFont));
        button7 = new AnimatedImageTextButton("", new ImageTextButtonStyle(newbgr, adtest, newbgr, gameplayFont));
        button8 = new AnimatedImageTextButton("", new ImageTextButtonStyle(newbgr, adtest, newbgr, gameplayFont));    
        button9 = new AnimatedImageTextButton("", new ImageTextButtonStyle(newbgr, adtest, newbgr, gameplayFont));     
        
        buttonMap = new HashMap<>();
        buttonMap.put("button0", button0);
        buttonMap.put("button1", button1);
        buttonMap.put("button2", button2);
        buttonMap.put("button3", button3);
        buttonMap.put("button4", button4);    
        buttonMap.put("button5", button5);
        buttonMap.put("button6", button6);
        buttonMap.put("button7", button7);
        buttonMap.put("button8", button8);
        buttonMap.put("button9", button9);  
                                
        nextLetter = new ImageTextButton("", new ImageTextButtonStyle(newbgr, newbgr, newbgr, font));        
                           
        TextureRegionDrawable textButtonUp = new TextureRegionDrawable(everything.findRegion("betterbuttontwo"));   
        TextureRegionDrawable textButtonDown = new TextureRegionDrawable(everything.findRegion("betterbuttontwodown"));  
        TextureRegionDrawable textButtonDisabled = new TextureRegionDrawable(everything.findRegion("betterbuttondisabled"));          
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle((Drawable) textButtonUp, (Drawable) textButtonDown, (Drawable) textButtonDisabled, font);                 
        newWordButton = new TextButton("New Word", textButtonStyle);
        newWordButton.addListener(new newWordListener());     
        newWordButton.setDisabled(true);
        readyButton = new TextButton("Ready", textButtonStyle);     
        readyButton.addListener(new readyListener());          
        restartButton = new TextButton("Restart", textButtonStyle);
        restartButton.addListener(new restartListener()); 
        restartButton.setDisabled(true);
        
        difficultyButton = new Button();
        TextureRegionDrawable mediumTexture = new TextureRegionDrawable(everything.findRegion("medium"));
        difficultyButton.setStyle(new Button.ButtonStyle(mediumTexture, mediumTexture, mediumTexture));
        difficultyButton.addListener(new difficultyListener()); 
        
        numbersButton = new Button();
        TextureRegionDrawable number5 = new TextureRegionDrawable(everything.findRegion("number5"));        
        numbersButton.setStyle(new Button.ButtonStyle(number5, number5, number5));
        numbersButton.addListener(new numbersListener());        

        minusplusButton = new Button();
        TextureRegionDrawable plusTexture = new TextureRegionDrawable(everything.findRegion("plus"));        
        minusplusButton.setStyle(new Button.ButtonStyle(plusTexture, plusTexture, plusTexture));
        minusplusButton.addListener(new minusplusListener());
                                       
        TextureAtlas timerAtlas = new TextureAtlas(Gdx.files.internal("timer.atlas")); 
        Animation timerAnim = new Animation(1 / (timerAtlas.getRegions().size / 10f), timerAtlas.getRegions());   
        timerActor = new TimerActor(timerAnim);                  
                
        error = Gdx.audio.newSound(Gdx.files.internal("error.ogg"));
        correct = Gdx.audio.newSound(Gdx.files.internal("correct.ogg"));  
        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));
        
        style = new ImageTextButtonStyle(newbgr, newbgr, newbgr, font);        
        
        gameScreenStage.addActor(stack);
        layout.add(letterGroup).row();
        layout.getCell(letterGroup).prefHeight(newbgr.getMinHeight() + (padding * 2));
        layout.getCell(letterGroup).minHeight(newbgr.getMinHeight() + (padding * 2));        
        layout.add(answerGroup).row();
        layout.getCell(answerGroup).prefHeight(newbgr.getMinHeight() + (padding * 2));
        layout.getCell(answerGroup).minHeight(newbgr.getMinHeight() + (padding * 2));        
        layout.add(buttonGroup).row();
        layout.add(settingGroup).row();                
        buttonGroup.addActor(newWordButton);
        buttonGroup.addActor(readyButton);                
        settingGroup.addActor(difficultyButton);
        settingGroup.addActor(numbersButton);        
        settingGroup.addActor(minusplusButton);                   
        stack.addActor(layout);       
        //layout.addActor(firework);
        
        detailStack = new Stack();
        detailGroup = new HorizontalGroup();
        detailGroup.addActor(detailLabel);        
        detailStack.addActor(detailGroup); 
        int stacksAbsurdInternalWidth = 75;
        stack.setPosition((layout.getPrefWidth()) - stacksAbsurdInternalWidth, paddingTotal);          
        System.out.println(layout.getPrefHeight());
    }     
    
    public void setup() {
        letterGroup.clear();   
        answerGroup.clear();
        buttonGroup.addActor(newWordButton);
        buttonGroup.addActor(readyButton);
        buttonGroup.removeActor(restartButton);    

        newWordButton.setTouchable(Touchable.enabled);
        newWordButton.setChecked(false);
        newWordButton.setText("[WHITE]New Word");
                
        ready = false;                   
        readyButton.setTouchable(Touchable.disabled);
        readyButton.setChecked(true);
        readyButton.setText("[GRAY]Ready");
    
        timerActor.reset();       
    }
            
    private class newWordListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();    
            timerActor.remove();
            newWordSetup();  
            
            if (readyButton.isChecked()) {
                readyButton.setTouchable(Touchable.enabled);
                readyButton.setChecked(false);
                readyButton.setText("[WHITE]Ready");                
            }
        }
    }
    
    private void newWordSetup() {          
        String word = "";
        try { 
            word = (handler.selectNewWord(NUMBER, MINUSPLUS)); 
        } catch (IOException ex) {
            Logger.getLogger(Ephelant.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        letterGroup.clear();
        for (int a = 0; a < word.length(); a++) {
            letterGroup.addActor(buttonMap.get("button" + String.valueOf(a)));
            buttonMap.get("button" + String.valueOf(a)).setText(String.valueOf(word.charAt(a)));
        }                
        
        animateLetter();
    }
        
    private class readyListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (!readyButton.isDisabled()) {
                click.play();            

                buttonGroup.removeActor(readyButton);
                buttonGroup.addActor(restartButton);
                
                newWordButton.setTouchable(Touchable.disabled);
                newWordButton.setChecked(true);
                newWordButton.setText("[GRAY]New Word");  
                
                answerGroup.addActor(nextLetter);                
                answerGroup.addActor(timerActor);                
                //gameScreenStage.addActor(timerActor);
                setTimer(handler.getWord());                
                timerActor.runTimer();                               
                
                ready = true;   
                readyButton.setChecked(false);
                updateLabelGroupDisplay();                
            }
        }
    }
    
    private class restartListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();          
            
            answerGroup.clear();
            
            timerActor.remove();
            
            buttonGroup.addActor(newWordButton);
            buttonGroup.addActor(readyButton);
            buttonGroup.removeActor(restartButton);
            
            newWordButton.setTouchable(Touchable.enabled);
            newWordButton.setChecked(false);
            newWordButton.setText("[WHITE]New Word");              
            
            timerActor.reset();    
            timerActor.remove();                    
            handler.restart();  
            ready = false;                        
            
            nextLetter.setText("");
            String word = handler.getWord();
            for (int a = 0; a < word.length(); a++) {
                letterGroup.addActor(buttonMap.get("button" + String.valueOf(a)));
                buttonMap.get("button" + String.valueOf(a)).setText(String.valueOf(word.charAt(a)));
            }              
            
            animateLetter();            
        }   
    }    
    
    // Push each letter + coloring to individual labels for display    
    private void updateLabelGroupDisplay() {          
        letterGroup.clear();        
        String[] update = handler.updateUserLabel();  
        for (int a = 0; a < update.length; a++) {
            if (!update[a].equals("")) {
                letterGroup.addActor(buttonMap.get("button" + String.valueOf(a)));                
                buttonMap.get("button" + String.valueOf(a)).setText(update[a]);                 
            }                
        }
        
        animateLetter();
        nextLetter.setText(handler.updateWordLabel());
    }      
    
    private void animateLetter() {
        // stops animating the last reference
        ImageTextButton buttonReference = buttonMap.get("button" + String.valueOf(highlightReference));
        buttonReference.getStyle().up = buttonReference.getStyle().checked; 
        
        // animates the next reference
        highlightReference = Integer.valueOf(handler.getShuffledMap().keySet().toArray()[handler.getPosition() - 1].toString());
        buttonReference = buttonMap.get("button" + String.valueOf(highlightReference));
        buttonReference.getStyle().up = buttonReference.getStyle().down;         
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
    
    public void checkEndConditions() {  
        if (ready) {
            if (timerActor.isDone() || handler.isComplete()) {
                answerGroup.clear();
                
                newWordButton.setTouchable(Touchable.enabled);
                newWordButton.setChecked(false);
                newWordButton.setText("[WHITE]New Word");
                
                ImageTextButton buttonReference = buttonMap.get("button" + String.valueOf(highlightReference));
                buttonReference.getStyle().up = buttonReference.getStyle().checked;                               
                
                ready = false;   
                timerActor.stopTimer();
            }            
        }
    }    
    
    // Tool-tips are displayed by adding text to the label
    private void displayDetail() {            
            if (difficultyButton.isOver()) {                    
                String easy = "Easy:         Unlimited time\n"
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
                detailStack.setPosition(200 + (layout.getPrefWidth() - detailGroup.getPrefWidth()) / 2, 250);                
            }
                        
            if (numbersButton.isOver() || minusplusButton.isOver()) {     
                detailStack.setPosition(layout.getPrefWidth(), 250);
                
                String value = Integer.toString(NUMBER);
                String operator = "+";
                
                if (MINUSPLUS == 0) {
                    operator = "-";
                }
                
                detailLabel.setText("Minimum or maximum word size\n"
                                  + "Ex: 8+ is words of 8-10 letters\n"
                                  + "Ex: 6- is words of 5-6 letters\n"
                                  + "Currently set at:  [GREEN]" + value + operator);      
                
                detailStack.setPosition(200 + (layout.getPrefWidth() - detailGroup.getPrefWidth()) / 2, 250);                   
            }
           
    }   
    
    // Called in Ephelant (main) to display/render tool-tips on mouse-over
    public void isOverButton() {
        if (difficultyButton.isOver() || numbersButton.isOver() || minusplusButton.isOver()) {
            displayDetail();
            gameScreenStage.addActor(detailStack);
        } else {
            detailStack.remove();
        }
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
            
            if (handler.getWord() != null && handler.getWord().length() > NUMBER && ready == false) {                        
                newWordSetup();                      
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

                    if (handler.getWord() != null && handler.getWord().length() > NUMBER && ready == false) {                        
                        newWordSetup();                      
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
                            updateLabelGroupDisplay();
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
                            updateLabelGroupDisplay();
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