package com.kingdomlands.game.core.entities.player.timers;

import com.badlogic.gdx.Gdx;
import com.kingdomlands.game.core.entities.player.PlayerManager;

public class PlayerSaveTimer {
    private static long sleeper = 3600/4;

    public static void autoSavePlayer() {
        sleeper -= Gdx.graphics.getDeltaTime();

        if (sleeper <= 0) {
            sleeper = 3600;
            PlayerManager.savePlayer();
        }
    }
}
