package com.kingdomlands.game.core.entities.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.projectile.Projectile;
import com.kingdomlands.game.core.entities.projectile.Projectiles;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.stages.StageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by David K on Mar, 2019
 */
public class MonsterManager {
    public static List<Monster> pullMonsterList() {
        List<Monster> monsters = new ArrayList<>();
        JsonReader json = new JsonReader();
        Json jsonR = new Json();
        JsonValue base = json.parse(Gdx.files.internal("Monsters.json"));
        JsonValue monsterArray = base.get("monsters");

        monsterArray.forEach(c -> {
            if (Objects.nonNull(c)) {
                List<Drop> drops = new ArrayList<>();
                JsonValue dropArray = c.get("monsterDrops");

                if (Objects.nonNull(dropArray)) {
                    dropArray.forEach(v -> {
                        if (Objects.nonNull(v)) {
                            drops.add(new Drop(v.getInt("id"), v.getInt("min"), v.getInt("max"), v.getInt("chance")));
                        }
                    });
                }

                Projectiles projectiles = null;

                if (c.has("projectile")) {
                    projectiles = Projectiles.valueOf(c.getString("projectile"));
                }

                monsters.add(new Monster(c.getInt("id") ,EntityType.MONSTER, c.getString("name"), 400, 400,
                        new Image(new Texture(Gdx.files.internal("monsters/" + c.getString("image")))),
                        jsonR.fromJson(MonsterAttributes.class, c.get("monsterAttributes").toJson(JsonWriter.OutputType.json)),
                        c.getBoolean("aggressive"),
                        c.getInt("range"),
                        c.getInt("rarity"), drops, projectiles));
            }
        });

        return monsters;
    }

    public static void generateMonstersOnMap(int amount) {
        for (int i = 0; i < amount; i++) {
            int x = Methods.getSecureRandom().nextInt(15600) + 64;
            int y = Methods.getSecureRandom().nextInt(15600) + 64;

            Monster monster = pullMonsterList().get(Methods.getSecureRandom().nextInt(pullMonsterList().size()));
            monster.setX(x);
            monster.setY(y);

            StageManager.addActor(monster);
        }
    }

    public static Monster generateRngMonster(int id) {
        List<Monster> monsters = pullMonsterList();
        return monsters.stream().filter(m -> Objects.nonNull(m) && m.getId() == id).collect(Collectors.toList()).get(0);
    }
}
