package com.kingdomlands.game.core.entities.util.groups;

public enum Group {
    SNAKE_PIT("Snake Pit", new int[]{4, 5, 6}, new int[]{5, 23, 24}, new int[]{}, 0, 0, 4, 10, 5, 15, 10),
    SCORPION_PIT("Scorpion Den", new int[]{3, 3, 3, 3, 4}, new int[]{5, 3, 2}, new int[]{}, 0, 0, 3, 5, 5, 15, 10),
    CREATURE_PIT("Critters", new int[]{1, 2}, new int[]{5, 3, 2}, new int[]{}, 0, 2, 0, 3, 8, 15, 10);

    private final int[] monsterIds;
    private final int[] gameObjectIds;
    private final int[] itemIds;
    private final int minItemSpawn;
    private final int maxItemSpawn;
    private final int minGameObjectSpawn;
    private final int maxGameObjectSpawn;
    private final int minMonsterSpawn;
    private final int maxMonsterSpawn;
    private final int chance;
    private final String name;

    Group(String name, int[] monsterIds, int[] gameObjectIds, int[] itemIds, int minItemSpawn, int maxItemSpawn, int minGameObjectSpawn, int maxGameObjectSpawn, int minMonsterSpawn, int maxMonsterSpawn, int chance) {
        this.monsterIds = monsterIds;
        this.gameObjectIds = gameObjectIds;
        this.itemIds = itemIds;
        this.minItemSpawn = minItemSpawn;
        this.maxItemSpawn = maxItemSpawn;
        this.minGameObjectSpawn = minGameObjectSpawn;
        this.maxGameObjectSpawn = maxGameObjectSpawn;
        this.minMonsterSpawn = minMonsterSpawn;
        this.maxMonsterSpawn = maxMonsterSpawn;
        this.chance = chance;
        this.name = name;
    }

    public int[] getMonsterIds() {
        return monsterIds;
    }

    public int[] getGameObjectIds() {
        return gameObjectIds;
    }

    public int[] getItemIds() {
        return itemIds;
    }

    public int getMinItemSpawn() {
        return minItemSpawn;
    }

    public int getMaxItemSpawn() {
        return maxItemSpawn;
    }

    public int getMinGameObjectSpawn() {
        return minGameObjectSpawn;
    }

    public int getMaxGameObjectSpawn() {
        return maxGameObjectSpawn;
    }

    public int getMinMonsterSpawn() {
        return minMonsterSpawn;
    }

    public int getMaxMonsterSpawn() {
        return maxMonsterSpawn;
    }

    public int getChance() {
        return chance;
    }

    public String getName() {
        return name;
    }
}