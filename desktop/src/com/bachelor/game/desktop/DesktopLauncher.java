package com.bachelor.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bachelor.game.BachelorClient;

public class DesktopLauncher {
	public static void main (String[] arg) {
		new LwjglApplication(new BachelorClient(), createConfiguration());
	}

	private static LwjglApplicationConfiguration createConfiguration() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1366;
		config.height = 768;
		config.fullscreen = false;
		config.resizable = false;
		config.title = "Test Game";
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = -1;
		config.samples = 8;

		return config;
	}
}
