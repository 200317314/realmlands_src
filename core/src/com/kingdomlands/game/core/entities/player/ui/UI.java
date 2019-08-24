package com.kingdomlands.game.core.entities.player.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by David K on Mar, 2019
 */
public enum UI {
    INVENTORY_ICON("Inventory", new Image(new Texture(Gdx.files.internal("hud/inventory.png"))), 912, 12),
    EQUIPMENT_ICON("Equipment", new Image(new Texture(Gdx.files.internal("hud/equipment.png"))), 946, 12),
    ATTRIBUTE_ICON("Attribute", new Image(new Texture(Gdx.files.internal("hud/attribute.png"))), 980, 12),
    SKILL_ICON("Skill", new Image(new Texture(Gdx.files.internal("hud/skill.png"))), 1014, 12),
    ABILITY_ICON("Ability", new Image(new Texture(Gdx.files.internal("hud/abilities.png"))), 1048, 12),
    FAMILIAR_ICON("Familiar", new Image(new Texture(Gdx.files.internal("hud/familiar.png"))), 1082, 12),
    SETTINGS_ICON("Settings", new Image(new Texture(Gdx.files.internal("hud/settings.png"))), 1114 + 132, 12),
    INVENTORY_ICON_CLICKED("Inventory clicked", new Image(new Texture(Gdx.files.internal("hud/inventory_clicked.png"))), 912, 12),
    EQUIPMENT_ICON_CLICKED("Equipment clicked", new Image(new Texture(Gdx.files.internal("hud/equipment_clicked.png"))), 946, 12),
    ATTRIBUTE_ICON_CLICKED("Attribute clicked", new Image(new Texture(Gdx.files.internal("hud/attribute_clicked.png"))), 980, 12),
    SKILL_ICON_CLICKED("Skill clicked", new Image(new Texture(Gdx.files.internal("hud/skill_clicked.png"))), 1012, 12),
    ABILITY_ICON_CLICKED("Ability clicked", new Image(new Texture(Gdx.files.internal("hud/abilities_clicked.png"))), 1046, 12),
    FAMILIAR_ICON_CLICKED("Familiar clicked", new Image(new Texture(Gdx.files.internal("hud/familiar_clicked.png"))), 1080, 12),
    SETTINGS_ICON_CLICKED("Settings clicked", new Image(new Texture(Gdx.files.internal("hud/settings_clicked.png"))), 1114 + 132, 12),
    INVENTORY_ICON_PLACE("Inventory", new Image(new Texture(Gdx.files.internal("hud/inventory.png"))), 912, 12),
    EQUIPMENT_ICON_PLACE("Equipment", new Image(new Texture(Gdx.files.internal("hud/equipment.png"))), 946, 12),
    ATTRIBUTE_ICON_PLACE("Attribute", new Image(new Texture(Gdx.files.internal("hud/attribute.png"))), 980, 12),
    SKILL_ICON_PLACE("Skill", new Image(new Texture(Gdx.files.internal("hud/skill.png"))), 1014, 12),
    ABILITY_ICON_PLACE("Ability", new Image(new Texture(Gdx.files.internal("hud/abilities.png"))), 1048, 12),
    FAMILIAR_ICON_PLACE("Familiar", new Image(new Texture(Gdx.files.internal("hud/familiar.png"))), 1082, 12),
    SETTINGS_ICON_PLACE("Settings", new Image(new Texture(Gdx.files.internal("hud/settings.png"))), 1114 + 132, 12),
    LOADING("Loading", new Image(new Texture(Gdx.files.internal("hud/loading_screen.png"))), 0, 0);

    private final String name;
    private Image image;
    private final int x, y;

    UI(String name, Image image, int x, int y) {
        this.name = name;
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(image.getX(), image.getY(), image.getWidth(), image.getImageHeight());
    }
}

