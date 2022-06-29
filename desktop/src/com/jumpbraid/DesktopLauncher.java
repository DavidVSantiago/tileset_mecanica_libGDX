package com.jumpbraid;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Braid Runner");
		config.setWindowedMode(App.LARGURA_TELA, App.ALTURA_TELA);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new App(), config);
	}
}
