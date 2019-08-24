package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by David K on Feb, 2019
 */
public class DamageTextManager {
    private static List<DamageText> damageTextList = new ArrayList<>();
    private static List<DamageText> damageTextListCache = new ArrayList<>();

    public static void add(DamageText damageText) {
        damageTextList.add(damageText);
    }

    public static void remove(DamageText damageText) {
        damageTextListCache.add(damageText);
    }

    public static void render(SpriteBatch spriteBatch) {
        damageTextListCache.forEach(dt -> {
            if (Objects.nonNull(dt)) {
                damageTextList.remove(dt);
            }
        });
        damageTextListCache.clear();

        damageTextList.forEach(dt -> {
            if (Objects.nonNull(dt)) {
                dt.render(spriteBatch);
            }
        });
    }
}