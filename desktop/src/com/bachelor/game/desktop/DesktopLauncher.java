package com.bachelor.game.desktop;

import com.bachelor.game.BachelorClient;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		new LwjglApplication(new BachelorClient(), createConfiguration());
	}

	private static LwjglApplicationConfiguration createConfiguration() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1080;
		config.fullscreen = false;
		config.resizable = false;
		config.title = "Test Game";
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = -1;
		config.samples = 16;

		return config;
	}
}
