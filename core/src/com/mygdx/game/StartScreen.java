package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
        
    private final Label title;
    
    private final Button play;
    private final Button settings;
    private final Button about;
    
    private final Sound click;
    
    private int LANG = 0;
    private final int EN = 0;
    private final int PT = 1;
    
    private TextureRegionDrawable enButtonTexture = new TextureRegionDrawable(
                                                    new TextureRegion(
                                                    new Texture(Gdx.files.internal("images/en.png"))));
    
    private TextureRegionDrawable ptButtonTexture = new TextureRegionDrawable(
                                                    new TextureRegion(
                                                    new Texture(Gdx.files.internal("images/pt.png"))));           
    
    public boolean startGame = false;
    
    public StartScreen(GameScreen gameScreenReference) {
        startScreenStage = new Stage();
        gameScreen = gameScreenReference;
        Gdx.input.setInputProcessor(startScreenStage);         
                
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts//Raleway-Medium.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        BitmapFont font = generator.generateFont(parameter);
        font.getData().markupEnabled = true;
        generator.dispose();

        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);        
        title = new Label("[BLUE]ephelant", fontStyle);     
        title.setWrap(true);
        title.setAlignment(Align.center);        
        title.setBounds(200, 500, 0, 0); // Alignment is probably affecting this
        
        play = new Button();        
        TextureRegionDrawable playButtonTexture = new TextureRegionDrawable(
                                                  new TextureRegion(
                                                  new Texture(Gdx.files.internal("images/play.png"))));        
        play.setStyle(new ButtonStyle(playButtonTexture, playButtonTexture, playButtonTexture));
        play.setBounds(105, 248, 190, 105);
        play.addListener(new playListener());
        
        settings = new Button();
        /*TextureRegionDrawable settingsButtonTexture = new TextureRegionDrawable(
                                                  new TextureRegion(
                                                  new Texture(
                                                  new FileHandle("images//settings.png"))));*/
        settings.setStyle(new ButtonStyle(enButtonTexture, enButtonTexture, enButtonTexture));
        settings.setBounds(15, 15, 150, 150); 
        settings.addListener(new settingsListener());
        
        about = new Button();
        TextureRegionDrawable aboutButtonTexture = new TextureRegionDrawable(
                                                   new TextureRegion(
                                                   new Texture(Gdx.files.internal("images/about.png"))));        
        about.setStyle(new ButtonStyle(aboutButtonTexture, aboutButtonTexture, aboutButtonTexture));
        about.setBounds(225, 15, 150, 150);
        about.addListener(new aboutListener());
        
        click = Gdx.audio.newSound(Gdx.files.internal("sounds//click.ogg"));        
                
        startScreenStage.addActor(title);
        startScreenStage.addActor(play);
        startScreenStage.addActor(settings);
        startScreenStage.addActor(about);
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
                    settings.getStyle().up = enButtonTexture;
                    settings.getStyle().down = enButtonTexture;
                    settings.getStyle().checked = enButtonTexture;                    
                    break;
                case PT:
                    settings.getStyle().up = ptButtonTexture;
                    settings.getStyle().down = ptButtonTexture;
                    settings.getStyle().checked = ptButtonTexture;                                        
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