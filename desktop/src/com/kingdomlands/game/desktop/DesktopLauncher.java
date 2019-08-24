package com.kingdomlands.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kingdomlands.game.Main;
import com.kingdomlands.game.core.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Constants.TITLE;
		config.width = Constants.WINDOW_WIDTH;
		config.height = Constants.WINDOW_HEIGHT;
		config.resizable = Constants.RESIZABLE;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
		config.addIcon("logo.png", Files.FileType.Internal);
		new LwjglApplication(new Main(), config);
	}
}
