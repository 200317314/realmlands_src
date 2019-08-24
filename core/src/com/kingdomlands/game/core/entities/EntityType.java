package com.kingdomlands.game.core.entities;

/**
 * Created by David K on Mar, 2019
 */
public enum EntityType {
    UI("UI", 0),
    PLAYER("Player", 1),
    MONSTER("Monster", 2),
    NPC("NPC", 2),
    GAMEOBJECT("Obj", 3),
    ITEM("Item", 4),
    OTHER("Other", 5);

    private final String name;
    private final int layer;

    EntityType(String name, int layer) {
        this.name = name;
        this.layer = layer;
    }

    public String getName() {
        return name;
    }

    public int getLayer() {
        return layer;
    }
}
