package com.kingdomlands.game.core.entities.util.pathing;

import com.kingdomlands.game.core.entities.player.PlayerManager;
import java.util.Objects;

public class EntityThread implements Runnable {
    private volatile boolean running = true;

    public EntityThread() {

    }

    @Override
    public void run() {
        while(running) {
            if (Objects.nonNull(PlayerManager.getCurrentPlayer())) {

            }
        }
    }

    public void stop() {
        running = false;
    }
}
