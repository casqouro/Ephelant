package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class StartScreen {
    protected Stage startScreenStage;
    protected GameScreen gameScreen;
    private final TextureAtlas everything;    
    private final Button playButton;
    private final Button settingsButton;
    private final Button aboutButton;            
    private final Label title;        
    private final Sound click;
    
    private int LANG = 0;
    private final int EN = 0;
    private final int PT = 1;            
    public boolean startGame = false;     
    
    public StartScreen(GameScreen gameScreenReference) {
        startScreenStage = new Stage();
        gameScreen = gameScreenReference;
        Gdx.input.setInputProcessor(startScreenStage);          
        everything = new TextureAtlas(Gdx.files.internal("everything.atlas"));
                
        FileHandle fontHandle = Gdx.files.internal("Raleway-Medium.fnt");
        BitmapFont font = new BitmapFont(fontHandle);
        font.getData().markupEnabled = true;       
        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);        
        
        title = new Label("[BLUE]ephelant", fontStyle);     
        title.setWrap(true);
        title.setAlignment(Align.center);        
        title.setBounds(200, 500, 0, 0); // Alignment is probably affecting this
        
        playButton = new Button();        
        TextureRegionDrawable playButtonTexture = new TextureRegionDrawable(everything.findRegion("play"));       
        playButton.setStyle(new ButtonStyle(playButtonTexture, playButtonTexture, playButtonTexture));
        playButton.setBounds(105, 248, 190, 105);
        playButton.addListener(new playListener());
        
        settingsButton = new Button();
        TextureRegionDrawable enButtonTexture = new TextureRegionDrawable(everything.findRegion("en"));
        settingsButton.setStyle(new ButtonStyle(enButtonTexture, enButtonTexture, enButtonTexture));
        settingsButton.setBounds(15, 15, 150, 150); 
        settingsButton.addListener(new settingsListener());
        
        aboutButton = new Button();
        TextureRegionDrawable aboutButtonTexture = new TextureRegionDrawable(everything.findRegion("about"));       
        aboutButton.setStyle(new ButtonStyle(aboutButtonTexture, aboutButtonTexture, aboutButtonTexture));
        aboutButton.setBounds(225, 15, 150, 150);
        aboutButton.addListener(new aboutListener());
        
        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));        
                
        startScreenStage.addActor(title);
        startScreenStage.addActor(playButton);
        startScreenStage.addActor(settingsButton);
        startScreenStage.addActor(aboutButton);
    }
            
    private class playListener extends ClickListener {    
        @Override
        public void clicked(InputEvent event, float x, float y) {
            click.play();
            startGame = true;
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
}