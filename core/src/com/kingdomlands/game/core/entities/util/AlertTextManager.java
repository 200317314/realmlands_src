package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by David K on Mar, 2019
 */
public class AlertTextManager {
    private static List<AlertText> alertTextList = new ArrayList<>();
    private static List<AlertText> alertTextListCache = new ArrayList<>();

    public static void add(AlertText damageText) {
        alertTextList.add(damageText);
    }

    public static void remove(AlertText damageText) {
        alertTextListCache.add(damageText);
    }

    public static void render(SpriteBatch spriteBatch) {
        alertTextListCache.forEach(dt -> {
            if (Objects.nonNull(dt)) {
                alertTextList.remove(dt);
            }
        });
        alertTextListCache.clear();

        alertTextList.forEach(dt -> {
            if (Objects.nonNull(dt)) {
                dt.render(spriteBatch);
            }
        });
    }
}
