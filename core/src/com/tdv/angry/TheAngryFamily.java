package com.tdv.angry;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdv.angry.screens.SplashScreen;

/**
 * Created by ThuanTM on 11/07/2016.
 */


/*public class TheAngryFamily implements ApplicationListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final float PPM = 1;
    public static final String title = "AngryFamily";
    private static final int FRAME_COLS = 6;         // #1
    private static final int FRAME_ROWS = 5;         // #2

    Animation walkAnimation;          // #3
    Texture walkSheet;              // #4
    TextureRegion[] walkFrames;             // #5
    SpriteBatch spriteBatch;            // #6
    TextureRegion currentFrame;           // #7

    float stateTime;                                        // #8

    @Override
    public void create() {
        walkSheet = new Texture(Gdx.files.internal("sprite-animation4.png")); // #9
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);              // #10
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.1f, walkFrames);      // #11
        spriteBatch = new SpriteBatch();                // #12
        stateTime = 0f;                         // #13
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);                        // #14
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);  // #16
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50);             // #17
        spriteBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}/*

/*public class TheAngryFamily extends ApplicationAdapter {
    Texture img;
	Sprite sprite;
		public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	public static final float PPM = 1;
	public static final String title = "AngryFamily";
		public SpriteBatch batch;
	public static AssetManager manager;
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("test1.png");
		sprite = new Sprite(img);
		sprite.setPosition(
				Gdx.graphics.getWidth()/2 - sprite.getWidth()/2,
				Gdx.graphics.getHeight()/2 - sprite.getHeight()/2);
		sprite.setScale(1.0f,6.0f);
		sprite.setRotation(90f);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
//		batch.draw(sprite, sprite.getX(),sprite.getY(),sprite.getWidth()/2,
//				sprite.getHeight()/2,sprite.getWidth(),
//				sprite.getHeight(),sprite.getScaleX(),
//				sprite.getScaleY(),sprite.getRotation());


		batch.draw(sprite, sprite.getX(),sprite.getY(),sprite.getWidth()/2,
				sprite.getHeight()/2,sprite.getWidth(),
				sprite.getHeight(),sprite.getScaleX(),
				sprite.getScaleY(),sprite.getRotation());

		batch.end();
	}
}*/


public class TheAngryFamily extends Game {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	public static final float PPM = 1;
	public static final int PADING_TOP = 30;
	public static final int PADING_BOTTOM = 30;

	public static final String title = "AngryFamily";
	public SpriteBatch batch;
	public static AssetManager manager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		manager = new AssetManager();

		manager.finishLoading();

		setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}
	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
