package com.tdv.angry.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tdv.angry.TheAngryFamily;

import java.util.ArrayList;

/**
 * Created by ThuanTM on 12/07/2016.
 * Hub contain following
 * Health/Live
 * Times
 * Weapons/ammunition
 * Menus
 * Mini-map
 * Game progression
 * Speedomte
 */
public class Hud implements Disposable {

    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    //Angry score/time Tracking Variables
    private Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private float timeCount;
    private static Integer score;

    //Scene2D widgets
    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    private Group group;
    private Image healthRed;
    private Image healthBorder;
    private Image healthWhite_left;
    private Image healthWhite_right;
    private ArrayList<Weapon> arrLstWeapon;
    // private Group groupWeapon;
    private Table tableWeapon;


    private IWeaponSelected iWeaponSelected;

    /*
        setter for iWeaponSelected
     */

    public void setiWeaponSelected(IWeaponSelected iWeaponSelected) {
        this.iWeaponSelected = iWeaponSelected;
    }


    public Hud(SpriteBatch sb) {
        //define our tracking variables
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(TheAngryFamily.WIDTH, TheAngryFamily.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        group = new Group();
        tableWeapon = getTableWeapon();
        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("ANGRY FAMILY", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //Draw healthy
        healthRed = new Image(new Texture("shape202.png"));
        healthRed.setPosition(0, 0);
        healthBorder = new Image(new Texture("shape205.png"));
        healthBorder.setPosition(-1, 0);
        healthWhite_left = new Image(new Texture("shape203.png"));
        healthWhite_left.setPosition(1, 2);
        healthWhite_right = new Image(new Texture("shape204.png"));
        healthWhite_right.setPosition(healthRed.getWidth() / 2, 2);
        // healthRed.setColor(new Color(1, 0, 0, 1));
        //healthBorder.setColor(new Color(0, 0, 1, 1));
        group.addActor(healthRed);
        group.addActor(healthBorder);
        group.addActor(healthWhite_left);
        group.addActor(healthWhite_right);
        group.setSize(healthRed.getWidth(), healthRed.getHeight());

        // groupWeapon.setPosition(0, 0);

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
//        //add a second row to our table

        group.setOrigin(group.getWidth() / 2, group.getHeight() / 2);

        table.row();
        table.add(group).colspan(3).center().expandX().padTop(TheAngryFamily.PADING_TOP);
        table.row();
        table.add(tableWeapon).colspan(3).center().expandX();

        stage.addActor(table);
    }

    /**
     * Create list weapon
     */
    private void setWeapon(){
        arrLstWeapon = new ArrayList<Weapon>();
        Weapon weapon1 = new Weapon(this, new Texture("weapon_1.png"));
        weapon1.setName("t1");
        arrLstWeapon.add(weapon1);
        Weapon weapon2 = new Weapon(this, new Texture("weapon_2.png"));
        weapon2.setName("t2");
        arrLstWeapon.add(weapon2);
        Weapon weapon3 = new Weapon(this, new Texture("weapon_3.png"));
        weapon3.setName("t3");
        arrLstWeapon.add(weapon3);
        Weapon weapon4 = new Weapon(this, new Texture("weapon_4.png"));
        weapon4.setName("t4");
        arrLstWeapon.add(weapon4);
        Weapon weapon5 = new Weapon(this, new Texture("weapon_1.png"));
        weapon5.setName("t5");
        arrLstWeapon.add(weapon5);
        Weapon weapon6 = new Weapon(this, new Texture("weapon_2.png"));
        weapon6.setName("t6");
        arrLstWeapon.add(weapon6);
        Weapon weapon7 = new Weapon(this, new Texture("weapon_3.png"));
        weapon7.setName("t7");
        arrLstWeapon.add(weapon7);
        Weapon weapon8 = new Weapon(this, new Texture("weapon_5.png"));
        weapon8.setName("t8");
        arrLstWeapon.add(weapon8);
    }

    private Table getTableWeapon(){
        setWeapon();

        Table tableWeapon = new Table();
        for(Weapon weapon: arrLstWeapon){
            tableWeapon.add(weapon);
            if(weapon.getName().equals("t4")){
                tableWeapon.add().width(weapon.getWidth() * 4);
                tableWeapon.setSize(weapon.getWidth()*12, weapon.getHeight());
            }
        }
        return tableWeapon;
    }

    private void resetWeaponPosition(float y){
        for(Weapon weapon: arrLstWeapon){
            weapon.setPosition(weapon.getX(), y);
            weapon.setSelected(false);
        }
    }

     public void update(float dt) {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }




    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 coord = stage.screenToStageCoordinates(new Vector2((float) screenX, (float) screenY));
        Actor hitActor = stage.hit(coord.x, coord.y, false);

        //Truong hop click vao weapon
        if (hitActor != null && hitActor instanceof Weapon) {

            //Xu ly truong hop khi lick vao.
            //B1: Set weapon current selected
            //B2: Set vi tri tut xuong
            //B3: Set lai cac weapon khac ve lai vi tri ban dau
            Weapon weapon = (Weapon) hitActor;
            if(!weapon.isSelected()) {
                resetWeaponPosition(hitActor.getY());
                weapon.setSelected(true);
                if (iWeaponSelected != null) {
                    weapon.animate();
                    iWeaponSelected.onSelected(weapon);
                }
            }
        }

        return true;
    }

    public interface IWeaponSelected {
        void onSelected(Weapon weapon);
    }
}
