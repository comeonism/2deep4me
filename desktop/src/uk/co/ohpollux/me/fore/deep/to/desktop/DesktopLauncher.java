package uk.co.ohpollux.me.fore.deep.to.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import uk.co.ohpollux.me.fore.deep.to.TDFMLevel;

public class DesktopLauncher {
    public static void main(String[] arg) {
	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	config.width = 1024;
	config.height = 920;
	new LwjglApplication(new TDFMLevel(), config);
    }
}
