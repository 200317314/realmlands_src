package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.audio.Sound;

import java.util.Objects;

public class SoundManager {
    private static Sound currentSound = null;
    private static Sound backGroundSound = null;

    public static Sound getCurrentSound() {
        return currentSound;
    }

    public static void setCurrentSound(Sound currentSound) {
        SoundManager.currentSound = currentSound;
        currentSound.play(1.0f);
    }

    public static Sound getBackGroundSound() {
        return backGroundSound;
    }

    public static void setBackGroundSound(Sound backGroundSound) {
        if (Objects.nonNull(getBackGroundSound())) {
            getBackGroundSound().stop();
            SoundManager.getBackGroundSound().dispose();
        }

        SoundManager.backGroundSound = backGroundSound;
        backGroundSound.play(0.01f);
        backGroundSound.loop(0.01f);
    }

    public static void playSoundFx(Sound fx) {
        fx.play(0.10f);
    }
}
