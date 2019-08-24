package com.kingdomlands.game.core.entities.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kingdomlands.game.core.entities.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NpcManager {
    public static Npc createNpc(int id) {
        List<Npc> npcs = new ArrayList<>();
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("npc.json"));
        JsonValue monsterArray = base.get("npcs");

        monsterArray.forEach(c -> {
            if (Objects.nonNull(c)) {
                if (c.getInt("id") == id) {
                    npcs.add(new Npc(EntityType.NPC, c.getString("name"), 1, 1,
                            new Image(new Texture(Gdx.files.internal("npcs/" + c.getString("image")))),
                            NpcType.valueOf(c.getString("type")), c.getInt("shop")));
                }
            }
        });

        return npcs.get(0);
    }
}
