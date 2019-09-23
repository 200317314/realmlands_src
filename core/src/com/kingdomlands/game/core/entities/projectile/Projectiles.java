package com.kingdomlands.game.core.entities.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by David K on Apr, 2019
 */
public enum Projectiles {
    ARROW("Arrow", new Image(new Texture(Gdx.files.internal("projectiles/arrow.png"))), 8, 6, true, false),
    BONE_ARROW("Arrow", new Image(new Texture(Gdx.files.internal("projectiles/bone_arrow.png"))), 8, 6, true, false),
    GOBLIN_ARROW("Arrow", new Image(new Texture(Gdx.files.internal("projectiles/goblin_arrow.png"))), 8, 6, true, false),
    MAGIC_ORB("Magic Orb", new Image(new Texture(Gdx.files.internal("projectiles/magic_orb.png"))), 8, 6, false, true);

    private final String name;
    private final Image image;
    private final int speed;
    private final int range;
    private final boolean ammo;
    private final boolean magical;

    Projectiles(String name, Image image, int speed, int range, boolean ammo, boolean magical) {
        this.name = name;
        this.image = image;
        this.speed = speed;
        this.range = range;
        this.ammo = ammo;
        this.magical = magical;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public int getSpeed() {
        return speed;
    }

    public int getRange() {
        return range;
    }

    public boolean isAmmo() {
        return ammo;
    }

    public boolean isMagical() {
        return magical;
    }}
