package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class StartScreen {
    protected Stage startScreenStage;
    protected GameScreen gameScreen;
    private final Table layout;
    private final HorizontalGroup titleGroup;
    private final HorizontalGroup playGroup;
    private final HorizontalGroup howtoGroup;
    private final HorizontalGroup settingsGroup;
    private final Button playButton;
    private final Button howtoButton;
    private final Button nextButton;
    private final Button backButton;
    private final Button settingsButton;
    private final Button aboutButton;    
    
    private final TextureAtlas everything;                
    private final Label title;        
    private final Sound click;
    
    private final Image howto0;
    private final Image howto1;
    private final Image howto2;
    private int howtoPage;
    
    private int LANG = 0;
    private final int EN = 0;
    private final int PT = 1;            
    public boolean startGame = false;     
    
    public StartScreen(GameScreen gameScreenReference) {
        startScreenStage = new Stage();
        startScreenStage.addListener(new startInputListener());
        gameScreen = gameScreenReference;
        Gdx.input.setInputProcessor(startScreenStage);          
        everything = new TextureAtlas(Gdx.files.internal("everything.atlas"));
        layout = new Table();
        layout.setFillParent(true);
        titleGroup = new HorizontalGroup();
        playGroup = new HorizontalGroup();
        howtoGroup = new HorizontalGroup();
        settingsGroup = new HorizontalGroup();    
        
        howto0 = new Image(new TextureRegionDrawable(everything.findRegion("howto0")));
        howto1 = new Image(new TextureRegionDrawable(everything.findRegion("howto1")));
        howto2 = new Image(new TextureRegionDrawable(everything.findRegion("howto2")));
                
        FileHandle fontHandle = Gdx.files.internal("Raleway-Medium.fnt");
        BitmapFont font = new BitmapFont(fontHandle);
        font.getData().markupEnabled = true;       
        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);        
        
        title = new Label("[BLUE]ephelant", fontStyle);     
        title.setWrap(true);
        title.setAlignment(Align.center);        
        title.setBounds(200, 500, 0, 0); // Alignment is probably affecting this
        
        TextureRegionDrawable textButtonUp = new TextureRegionDrawable(everything.findRegion("betterbuttontwo"));   
        TextureRegionDrawable textButtonDown = new TextureRegionDrawable(everything.findRegion("betterbuttontwodown"));  
        TextureRegionDrawable textButtonDisabled = new TextureRegionDrawable(everything.findRegion("betterbuttondisabled"));  
        
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle((Drawable) textButtonUp, (Drawable) textButtonDown, (Drawable) textButtonUp, font);                                
        playButton = new TextButton("Play", textButtonStyle);        
        playButton.addListener(new playListener());
        
        howtoButton = new TextButton("How-To", textButtonStyle);
        howtoButton.addListener(new howtoListener());
        nextButton = new TextButton("NEXT", textButtonStyle);
        nextButton.addListener(new nextListener());
        nextButton.setSize(120, 50);
        backButton = new TextButton("BACK", textButtonStyle);
        backButton.addListener(new backListener());    
        backButton.setSize(120, 50);
        
        settingsButton = new Button();
        TextureRegionDrawable enButtonTexture = new TextureRegionDrawable(everything.findRegion("en"));
        settingsButton.setStyle(new ButtonStyle(enButtonTexture, enButtonTexture, enButtonTexture)); 
        settingsButton.addListener(new settingsListener());
        
        aboutButton = new Button();
        TextureRegionDrawable aboutButtonTexture = new TextureRegionDrawable(everything.findRegion("about"));       
        aboutButton.setStyle(new ButtonStyle(aboutButtonTexture, aboutButtonTexture, aboutButtonTexture));
        aboutButton.addListener(new aboutListener());
        
        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));        
                    
        startScreenStage.addActor(layout);
        layout.add(titleGroup).padBottom(50).row();
        layout.add(playGroup).padBottom(5).row();
        layout.add(howtoGroup).padBottom(20).row();
        layout.add(settingsGroup);
        titleGroup.addActor(title);
        playGroup.addActor(playButton);
        howtoGroup.addActor(howtoButton);
        settingsGroup.addActor(settingsButton);
        settingsGroup.addActor(aboutButton);
    }
            
    private class playListener extends ClickListener {    
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();
            startGame = true;
        }
    }
    
    private class howtoListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();
            layout.remove();           
            startScreenStage.addActor(howto0);
            startScreenStage.addActor(backButton);             
            startScreenStage.addActor(nextButton);
            backButton.setPosition(0, (Gdx.graphics.getHeight() - backButton.getHeight()) / 2);
            nextButton.setPosition(Gdx.graphics.getWidth() - nextButton.getWidth(), (Gdx.graphics.getHeight() - nextButton.getHeight()) / 2);
            howtoPage = 0;
        }        
    }
    
    private class nextListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play(); 
            howtoPage++;
            
            if (howtoPage == 1) {
                howto0.remove();
                startScreenStage.addActor(howto1);
                backButton.toFront();
                nextButton.toFront();
            }            
            
            if (howtoPage == 2) {
                howto1.remove();
                startScreenStage.addActor(howto2);
                backButton.toFront();
            }
        }        
    }
    
    private class backListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();        
            
            if (howtoPage == 0) {
                howto0.remove();
                backButton.remove();
                nextButton.remove();
                startScreenStage.addActor(layout);                
            }
            
            if (howtoPage == 1) {
                howto1.remove();
                startScreenStage.addActor(howto0);
                backButton.toFront();
                nextButton.toFront();
            }            
            
            if (howtoPage == 2) {
                howto2.remove();
                startScreenStage.addActor(howto1);
                backButton.toFront();
                nextButton.toFront();
            }
            
            howtoPage--;            
        }        
    }    
        
    private class settingsListener extends ClickListener {    
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();

            LANG++;

            if (LANG > PT) {
                LANG = EN;
            }

            switch (LANG) {
                case EN:
                    TextureRegionDrawable enButtonTexture = new TextureRegionDrawable(everything.findRegion("en"));                    
                    settingsButton.getStyle().up = enButtonTexture;
                    settingsButton.getStyle().down = enButtonTexture;
                    settingsButton.getStyle().checked = enButtonTexture;                    
                    break;
                case PT:
                    TextureRegionDrawable ptButtonTexture = new TextureRegionDrawable(everything.findRegion("pt"));                    
                    settingsButton.getStyle().up = ptButtonTexture;
                    settingsButton.getStyle().down = ptButtonTexture;
                    settingsButton.getStyle().checked = ptButtonTexture;                                        
                    break;
            } 
            
            gameScreen.setLang(LANG);
        }       
    }        

    private class aboutListener extends ClickListener {    
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();
            System.out.println("Created, slowly, by Matthew Frank");
        }
    }
    
    private class startInputListener extends InputListener {
        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch (keycode) {
                case Input.Keys.ESCAPE: 
                    howto0.remove();
                    howto1.remove();
                    howto2.remove();
                    backButton.remove();
                    nextButton.remove();
                    startScreenStage.addActor(layout);
                    break;
            }
            return true;
        }        
    }    
}