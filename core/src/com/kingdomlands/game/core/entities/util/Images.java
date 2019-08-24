package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.graphics.Texture;
import com.kingdomlands.game.core.entities.player.PlayerManager;

import java.util.Objects;

/**
 * Created by David K on Jan, 2019
 */
public enum Images {
    LOGIN(new Texture("hud/loginBG.png")),
    //INVENTORY_ICON(new Texture("hud/inventory_icon.png")),
    //ATTRIBUTE_ICON(new Texture("hud/attribute_icon-t.png")),
    //EQUIPMENT_ICON(new Texture("hud/equipment_icon.png")),
    //SKILL_ICON(new Texture("hud/skill_icon.png")),
    DRUID_HUD(new Texture("avatars/druid.png"));

    private final Texture texture;

    Images(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public static Texture getHud() {
        if (Objects.nonNull(Objects.requireNonNull(PlayerManager.getCurrentPlayer()).getPlayerClass())) {
            return Images.valueOf(PlayerManager.getCurrentPlayer().getPlayerClass().getAvatar()).getTexture();
        }

        return DRUID_HUD.getTexture();
    }
}