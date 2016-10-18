package com.tdv.angry.sprites.items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tdv.angry.network.NetClient;
import com.tdv.angry.network.Network;
import com.tdv.angry.screens.PlayScreen;
import com.tdv.angry.sprites.Bullet;

/**
 * Created by Administrator on 31/08/2016.
 */
public abstract class Player extends Sprite{
    public NetClient getNetClient() {
        return netClient;
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    public enum State {SHOTTING, STANDING, DEAD};

    protected State currentState;

    public String getSex() {
        return sex;
    }

    protected String sex = "MALE";

    public Bullet getBullet() {
        return bullet;
    }

    protected Bullet bullet;
    protected Animation playerAnim;
    protected PlayScreen playScreen;
    private float stateTimer;
    private NetClient netClient;
    public Player(){

    }

    public Player(PlayScreen playScreen, String sex){
        this.playScreen= playScreen;
        this.sex = sex;
        bullet = new Bullet(playScreen, null, this);
        stateTimer = 0;
        currentState = State.STANDING;
        createAnimation();
        this.setBounds(getLocation().x,getLocation().y, this.getWidth(), this.getHeight());

        try {
            setNetClient(new NetClient());
            Network.Login login = new Network.Login();
            login.name = this.getSex();
            getNetClient().name = this.getSex();
            getNetClient().client.sendTCP(login);
        }catch(Exception e){
            System.out.println("Loi "+e.toString());
        }
    }
    public void draw(SpriteBatch batch, float dt){
        update(dt);
        TextureRegion frame = getFrame(dt);
        Vector3 location = getLocation();
        batch.begin();
            batch.draw(frame, location.x, location.y);
            bullet.draw(batch, dt);
        batch.end();
    }

    //Make the bullet flying
    public void shoot(Vector2 target, float speed){
        if(bullet !=null) {
            bullet.touchX(target, speed);
        }
    }
    public  TextureRegion getFrame(float dt){
        currentState = getState();
        stateTimer += dt;
        TextureRegion region = playerAnim.getKeyFrame(stateTimer, true);
        return region;
    }

    public void update(float dt){
       this.setBounds(getLocation().x, getLocation().y, this.getWidth(), this.getHeight());
        Network.MoveCharacter msg = new Network.MoveCharacter();
        msg.x = bullet.getBoundingRectangle().getX();
        msg.y = bullet.getBoundingRectangle().getY();

        getNetClient().client.sendTCP(msg);

    }

    public State getState(){
        return State.STANDING;
    }

    public abstract Vector3 getLocation();

    public abstract void createAnimation();
}
