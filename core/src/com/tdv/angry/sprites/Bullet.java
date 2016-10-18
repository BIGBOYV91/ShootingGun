package com.tdv.angry.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tdv.angry.TheAngryFamily;
import com.tdv.angry.network.Character;
import com.tdv.angry.scenes.Weapon;
import com.tdv.angry.screens.PlayScreen;
import com.tdv.angry.sprites.items.Player;

import java.util.Map;

import static com.tdv.angry.sprites.Bullet.State.DEFAULT;

/**
 * Created by ThuanTM on 26/07/2016.
 */
public class Bullet extends Sprite implements IBullet {
    private static final float GRAVITY = -5f;
    private static final float MOVEMENT = 450;

    public enum State {DEFAULT, FLY, DETECTED}

    ;

    private float speed = 100;
    private float angle = 0;

    private State currentState;
    private PlayScreen playScreen;
    private Weapon weapon;
    private Animation playerAnim;
    private float stateTimer;
    private Vector3 position;
    private boolean fly = false;
    private Player player;

    //public Rectangle getBounds() {
    //    return bounds;
    //}

    //private Rectangle bounds;
    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    private Vector3 velocity;

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setFly(boolean fly) {
        this.fly = fly;
    }

    public Bullet(PlayScreen playScreen, Weapon weapon, Player player) {
        this.playScreen = playScreen;
        this.weapon = weapon;
        this.player = player;


        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(new Texture("shape190.png")));
        frames.add(new TextureRegion(new Texture("shape191.png")));
        frames.add(new TextureRegion(new Texture("shape192.png")));
        frames.add(new TextureRegion(new Texture("shape193.png")));


        playerAnim = new Animation(0.3f, frames);

        currentState = DEFAULT;

        position = player.getLocation();

        //bounds = new Rectangle(this.getX(), this.getY(),frames.get(0).getRegionWidth(), frames.get(0).getRegionHeight());
        this.setBounds(position.x, position.y, frames.get(0).getRegionWidth(), frames.get(0).getRegionHeight());
        velocity = new Vector3(0, 0, 0);
    }

//    public Bullet(PlayScreen playScreen, Weapon weapon, int x, int y){
//        this(playScreen, weapon);
//        position = new Vector3(x, y, 0);
//        velocity = new Vector3(0, 0, 0);
//    }

    public void draw(SpriteBatch batch, float delta) {
        batch.draw(getFrame(delta), position.x, position.y);
        flying(delta);
    }

    @Override
    public boolean detectEnemy() {

        return false;
    }

    public void touchX(Vector2 target, float speed) {
        angle = getAngle(target);
        this.speed = speed;//MOVEMENT * (1-angle);


        //STRONG = MOVEMENT * (1-angle) * strong/500;


        //1. Goc cang cao thi Movement cang giam
        //2. Do manh yeu * Movenet

        velocity.y = GRAVITY;// *speed/400;
        // Gdx.app.log("AAA", speed +"  THUANTM TOUCH = ("+target.x+", "+target.y+")  "+getAngle(target));
        setFly(true);
    }

    /*public float getAngle(Point target) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - y, target.x - x));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }*/

    public float getAngle(Vector2 target) {
        float x = player.getLocation().x;
        float y = player.getLocation().y;
        if (x > TheAngryFamily.WIDTH) x = player.getLocation().x - TheAngryFamily.WIDTH;
        float angle = (float) Math.toDegrees(Math.atan2(target.y - y, target.x - x));
        Gdx.app.log("Angle", "Tu (" + x + " , " + y + ") ==> (" + target.x + " , " + target.y + "):  Toa do: " + angle);
        return angle;
    }

    @Override
    public void flying(float dt) {

        this.setBounds(position.x, position.y, this.getWidth(), this.getHeight());
        if (fly) {
            velocity.add(0, GRAVITY, 0);
            velocity.scl(dt);
            double velocityX = Math.cos((angle) * Math.PI / 180) * (speed * dt);
            double velocityY = Math.sin((angle) * Math.PI / 180) * (speed * dt);
            position.add((float) velocityX, (float) velocityY + velocity.y, 0);

            velocity.scl(1 / dt);
            if (position.x > playScreen.gamecam.viewportWidth / 2 && position.x < playScreen.gamecam.viewportWidth * 3 / 2) {
                playScreen.gamecam.position.set(position.x, playScreen.gamecam.viewportHeight / 2, 0);
            }
            resetBullet();
        } else {
            for (Map.Entry<Integer, Character> entry : player.getNetClient().characters.entrySet()) {
                Integer key = entry.getKey();
                Character character = entry.getValue();
                if (!character.name.equals(player.getSex())) {
                    position.set(character.x, character.y, 0);
                    playScreen.gamecam.position.set(position.x, playScreen.gamecam.viewportHeight / 2, 0);
                }
            }
        }
    }

    public void resetBullet() {

        if (position.x < 0 || position.y < 0 || position.x > playScreen.gamecam.viewportWidth * 2) {
            velocity = new Vector3(0, speed, 0);
            position = player.getLocation();
            Gdx.app.log("TT", "TT MALE ==> " + player.getSex());
            if (player.getSex().equals("MALE")) {
                playScreen.gamecam.position.set(playScreen.gamecam.viewportWidth * 2 - playScreen.gamecam.viewportWidth / 2, playScreen.gamecam.viewportHeight / 2, 0);
            } else {
                playScreen.gamecam.position.set(playScreen.gamecam.viewportWidth / 2, playScreen.gamecam.viewportHeight / 2, 0);
            }
            setFly(false);
        }
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        stateTimer += dt;

        TextureRegion region = playerAnim.getKeyFrame(stateTimer, true);
        return region;
    }

    public State getState() {
        //default la stone
        return DEFAULT;
    }
}
