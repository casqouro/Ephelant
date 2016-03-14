package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Ephelant;

public class DesktopLauncher {    
	public static void main (String[] arg) {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();  
            config.fullscreen = false;
            config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
            config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
            new LwjglApplication(new Ephelant(), config);            
	}
}