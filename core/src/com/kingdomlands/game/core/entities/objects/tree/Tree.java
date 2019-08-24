package com.kingdomlands.game.core.entities.objects.tree;

import com.kingdomlands.game.core.entities.util.Methods;

public enum Tree {
    OAK_TREE(100, 6, 86, 24, 1, new int[]{1, 8}),
    WILLOW_TREE(350, 14, 87, 26, 5, new int[]{1, 10}),
    MAPLE_TREE(750, 32, 88, 28, 10, new int[]{1, 5});

    private final int treeHp, treeExp, logId, stumpId, treeLevel, logQuantity;

    Tree(int treeHp, int treeExp, int logId, int stumpId, int treeLevel, int[] logQuantity) {
        this.treeHp = treeHp;
        this.treeExp = treeExp;
        this.logId = logId;
        this.stumpId = stumpId;
        this.treeLevel = treeLevel;
        this.logQuantity = Methods.random(logQuantity[0], logQuantity[1]);
    }

    public int getTreeHp() {
        return treeHp;
    }

    public int getTreeExp() {
        return treeExp;
    }

    public int getLogId() {
        return logId;
    }

    public int getStumpId() {
        return stumpId;
    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public int getLogQuantity() {
        return logQuantity;
    }
}
