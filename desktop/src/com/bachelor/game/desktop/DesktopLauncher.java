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
		config.width = 1024;
		config.height = 768;
		config.fullscreen = false;
		config.resizable = false;
		config.title = "Bachelor Game";
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = -1;
		config.samples = 4;

		return config;
	}
}
