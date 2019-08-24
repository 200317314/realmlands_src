package com.kingdomlands.game.core.entities.player.chat;

public enum Commands {
    SPAWN_ITEM("/i", new int[]{3,4,5}, 2);

    private final String command;
    private final int[] roles;
    private int params;

    Commands(String command, int[] roles, int params) {
        this.command = command;
        this.roles = roles;
        this.params = params;
    }

    public String getCommand() {
        return command;
    }

    public int[] getRoles() {
        return roles;
    }

    public int getParams() {
        return params;
    }

    public static void execSpawnItem() {

    }
}
