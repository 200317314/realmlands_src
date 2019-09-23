package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by David K on Feb, 2019
 */
public enum DamageType {
    DEFAULT("npc", Color.WHITE),
    PHYSICAL("physical", Color.RED),
    RANGED("ranged", Color.SCARLET),
    MAGICAL("magical", Color.BLUE),
    POISON("poison", Color.GREEN),
    NPC("npc", Color.ORANGE),
    EXP("exp", Color.CHARTREUSE),
    CRITICAL("critical", Color.NAVY),
    HPREGEN("hpregen", Color.GREEN),
    MANAREGEN("manaregen", Color.BLUE),
    EARTH("earth", Color.YELLOW),
    FIRE("fire", Color.ORANGE),
    UNDEAD("undead", Color.PURPLE),
    LEVEL_UP("level", Color.ORANGE);

    private final String name;
    private final Color color;

    DamageType(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public BitmapFont getFont() {
        BitmapFont font = new BitmapFont();
        font.setColor(color);
        return font;
    }

    public static Color getColor(String text) {
        if (text.contains("[Battle]")) {
            return Color.RED;
        } else if (text.contains("[Login]")) {
            return Color.ORANGE;
        } else if (text.contains("[Loot]")) {
            return Color.GREEN;
        } else if (text.contains("[System]")) {
            return Color.PURPLE;
        } else if (text.contains("[Shop]")) {
            return Color.ROYAL;
        } else if (text.contains("[Bank]")) {
            return Color.CORAL;
        }

        return Color.WHITE;
    }
}