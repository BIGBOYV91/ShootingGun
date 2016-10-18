package com.tdv.angry.sprites.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tdv.angry.TheAngryFamily;
import com.tdv.angry.screens.PlayScreen;

/**
 * Created by ThuanTM on 31/08/2016.
 */
public class PlayerMale extends Player {


    public PlayerMale(PlayScreen playScreen){
        super(playScreen, "MALE");
        Gdx.app.log("NAY THI TOA DO: ",""+getLocation().x);
        playScreen.gamecam.position.set(playScreen.gamecam.viewportWidth*2-playScreen.gamecam.viewportWidth/2, playScreen.gamecam.viewportHeight/2,0);
    }
    @Override
    public Vector3 getLocation() {
        return new Vector3(TheAngryFamily.WIDTH*2 - 300, TheAngryFamily.PADING_BOTTOM, 0);
    }

    @Override
    public void createAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(new Texture("shape265.png")));
        //frames.add(new TextureRegion(new Texture("shape103.png")));

        playerAnim = new Animation(0.1f, frames);
        this.setSize(frames.get(0).getRegionWidth(), frames.get(0).getRegionHeight());
        frames.clear();
    }
}
