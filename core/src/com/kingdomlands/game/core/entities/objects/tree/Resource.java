package com.kingdomlands.game.core.entities.objects.tree;

import com.kingdomlands.game.core.entities.util.Methods;

public enum Resource {
    OAK_TREE(100, 6, 86, 24, 1, new int[]{1, 8}, "Chop Down", "Woodcutting", "Chop!", "woodcutting.wav"),
    WILLOW_TREE(350, 14, 87, 26, 5, new int[]{1, 10}, "Chop Down", "Woodcutting", "Chop!", "woodcutting.wav"),
    MAPLE_TREE(750, 32, 88, 28, 10, new int[]{1, 5}, "Chop Down", "Woodcutting", "Chop!", "woodcutting.wav"),
    SHRIMP_FISHSPOT(100, 7, 91, -1, 1, new int[]{1, 5}, "Fish", "Fishing", "Blub!", "fishing.wav"),
    MINNOWS_FISHSPOT(140, 9, 90, -1, 2, new int[]{1, 10}, "Fish", "Fishing", "Blub!", "fishing.wav"),
    CRAYFISH_FISHSPOT(100, 5, 89, -1, 1, new int[]{1, 10}, "Fish", "Fishing", "Blub!", "fishing.wav"),
    WHITEPERCH_FISHSPOT(200, 11, 96, -1, 3, new int[]{1, 5}, "Fish", "Fishing", "Blub!", "fishing.wav"),
    YELLOWPERCH_FISHSPOT(280, 15, 97, -1, 4, new int[]{1, 6}, "Fish", "Fishing", "Blub!", "fishing.wav"),
    STRIPEDBASS_FISHSPOT(360, 20, 93, -1, 5, new int[]{3, 8}, "Fish", "Fishing", "Blub!", "fishing.wav"),
    TROUT_FISHSPOT(360, 28, 94, -1, 7, new int[]{1, 10}, "Fish", "Fishing", "Blub!", "fishing.wav"),
    WALLEYE_FISHSPOT(520, 36, 95, -1, 10, new int[]{1, 10}, "Fish", "Fishing", "Blub!", "fishing.wav"),
    SMALLMOUTHBASS_FISHSPOT(680, 46, 92, -1, 12, new int[]{1, 10}, "Fish", "Fishing", "Blub!", "fishing.wav"),
    COPPER_ROCK(100, 5, 127, 31, 1, new int[]{1, 10}, "Mine", "Mining", "Tick!", "mining.wav"),
    TIN_ROCK(100, 5, 129, 31, 1, new int[]{1, 10}, "Mine", "Mining", "Tick!", "mining.wav");


    private final int hp, exp, resourceId, depletedId, level, quantity;
    private final String action, skill, blurb, sound;
    private final int[] amount;

    Resource(int hp, int exp, int resourceId, int depletedId, int level, int[] amount, String action, String skill, String blurb, String sound) {
        this.hp = hp;
        this.exp = exp;
        this.resourceId = resourceId;
        this.depletedId = depletedId;
        this.level = level;
        this.quantity = Methods.random(amount[0], amount[1]);
        this.action = action;
        this.skill = skill;
        this.blurb = blurb;
        this.amount = amount;
        this.sound = sound;
    }

    public int getHp() {
        return hp;
    }

    public int getExp() {
        return exp;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getDepletedId() {
        return depletedId;
    }

    public int getLevel() {
        return level;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getAction() {
        return action;
    }

    public String getSkill() {
        return skill;
    }

    public String getBlurb() {
        return blurb;
    }

    public int[] getAmount() {
        return amount;
    }

    public String getSound() {
        return sound;
    }
}
