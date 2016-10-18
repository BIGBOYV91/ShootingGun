package com.tdv.angry.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tdv.angry.TheAngryFamily;
import com.tdv.angry.scenes.Hud;
import com.tdv.angry.scenes.Weapon;
import com.tdv.angry.sprites.items.Player;
import com.tdv.angry.sprites.items.PlayerFemale;
import com.tdv.angry.sprites.items.PlayerMale;


/**
 * Created by ThuanTM on 11/07/2016.
 */
public class PlayScreen implements Screen, InputProcessor {
    Sprite bg;
    Player player;
    TheAngryFamily game;
    //basic playscreen variables
    public OrthographicCamera gamecam;
    public Viewport gamePort;
    private Hud hud;


    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    //private B2WorldCreator creator;

    private Weapon currentWeapon;

    private Hud.IWeaponSelected iWeaponSelected;

    public Hud getHud() {
        return hud;
    }

    public World getWorld() {
        return world;
    }

    private boolean isBulletSelected = false;
    //Vector2 vecTranslate;

    public long duration = 0L;

    public PlayScreen(TheAngryFamily game) {
        this.game = game;

        gamecam = new OrthographicCamera();

        gamePort = new StretchViewport(TheAngryFamily.WIDTH, TheAngryFamily.HEIGHT, gamecam);
        gamePort.apply();
        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamecam.viewportWidth / 2, gamecam.viewportHeight / 2, 0);

        iWeaponSelected = new Hud.IWeaponSelected() {
            @Override
            public void onSelected(Weapon weapon) {
                currentWeapon = weapon;
                isBulletSelected = true;
                //player.getBullet(currentWeapon);
                Gdx.app.log("Weapon", currentWeapon.getName());

//                if (currentWeapon.getName().equals("t5") || currentWeapon.getName().equals("t6") || currentWeapon.getName().equals("t7") || currentWeapon.getName().equals("t8")) {
//                    createPlayer("MALE");
//                }else{
//                    createPlayer("FEMALE");
//                }


            }
        };
        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);
        hud.setiWeaponSelected(iWeaponSelected);

        createPlayer("FEMALE");

        bg = new Sprite(new Texture("shape52.png"));
        bg.setSize(TheAngryFamily.WIDTH * 2, TheAngryFamily.HEIGHT);

        Gdx.input.setInputProcessor(this);
    }

    public void createPlayer(String name) {
        if (name.equals("MALE")) {
            player = new PlayerMale(this);
        } else {
            player = new PlayerFemale(this);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        game.batch.draw(bg.getTexture(), 0, 0, gamecam.viewportWidth * 2, gamecam.viewportHeight);
        game.batch.end();
        player.draw(game.batch, delta);
        player.getBullet().resetBullet();
        hud.stage.draw();
    }

    public void update(float dt) {
        hud.update(dt);
        gamecam.update();
    }

    @Override
    public void resize(int width, int height) {
        //updated our game viewport
        gamePort.update(width, height);
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
        bg.getTexture().dispose();
        hud.dispose();
        world.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        duration = System.currentTimeMillis();
        //player.showBinoculars();
        return hud.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        duration = System.currentTimeMillis() - duration;
        if (duration > 500 && isBulletSelected) {
            player.shoot(new Vector2(screenX, (TheAngryFamily.HEIGHT - screenY)), duration / 2);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
