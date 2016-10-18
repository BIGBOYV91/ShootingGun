package com.tdv.angry.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.tdv.angry.TheAngryFamily;

/**
 * Created by ThuanTM on 09/07/2016.
 */
public class SplashScreen implements Screen {
    private Texture splashScreen;
    private TheAngryFamily game;

    public SplashScreen(TheAngryFamily game){
        this.game = game;
        splashScreen = new Texture("shape30.png");
    }

    public void handleInput(float dt){
        if(Gdx.input.justTouched()){
            game.setScreen(new PlayScreen(game));
        }
    }

    public void update(float dt) {
        //handle user input first
        handleInput(dt);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);
        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(splashScreen, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        splashScreen.dispose();
    }
}
