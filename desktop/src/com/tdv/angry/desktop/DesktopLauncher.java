package com.tdv.angry.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tdv.angry.TheAngryFamily;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = TheAngryFamily.WIDTH;
        config.height = TheAngryFamily.HEIGHT;
        config.title = TheAngryFamily.title;
        new LwjglApplication(new TheAngryFamily(), config);
    }
}
