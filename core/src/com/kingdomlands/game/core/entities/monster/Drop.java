package com.kingdomlands.game.core.entities.monster;

/**
 * Created by David K on Apr, 2019
 */
public class Drop {
    int id, min, max, chance;

    public Drop(int id, int min, int max, int chance) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.chance = chance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
}
