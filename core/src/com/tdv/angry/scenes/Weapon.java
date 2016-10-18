package com.tdv.angry.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Administrator on 26/07/2016.
 */
public class Weapon extends Image {

    private  Hud hud;
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Weapon(Hud hud, Texture texture){
        super(texture);
        this.hud = hud;
    }

    private void translateDown(){
        //this.setDrawable(new SpriteDrawable(new Sprite(new Texture("shape207.png"))));
        this.setPosition(getX(), getY()-10);
    }

    public void animate(){
        translateDown();
        //Hien thi anh? nhap nhay, to/nho here
    }

}
