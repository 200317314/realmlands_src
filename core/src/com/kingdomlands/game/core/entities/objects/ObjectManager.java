package com.kingdomlands.game.core.entities.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.objects.tree.Resource;
import com.kingdomlands.game.core.stages.StageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by David K on Apr, 2019
 */
public class ObjectManager {
    public static List<GameObject> pullObjectList() {
        List<GameObject> objs = new ArrayList<>();
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("gameobjects.json"));
        JsonValue objectsArray = base.get("objects");

        objectsArray.forEach(c -> {
            if (Objects.nonNull(c)) {
                objs.add(new GameObject(EntityType.GAMEOBJECT, c.getString("name"), 10, 10,
                        new Image(new Texture(Gdx.files.internal("gameobjects/" + c.getString("image")))),
                        c.getInt("rarity"), c.getString("description"), new ArrayList<>(), ObjectType.valueOf(c.getString("type"))));
            }
        });

        return objs;
    }

    public static GameObject createObjectById(int id, int x, int y) {
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("gameobjects.json"));
        JsonValue objectsArray = base.get("objects");

        for (JsonValue val : objectsArray) {
            if (Objects.nonNull(val)) {
                if (val.getInt("id") == id) {
                    GameObject gameObject = new GameObject(EntityType.GAMEOBJECT, val.getString("name"), x, y,
                            new Image(new Texture(Gdx.files.internal("gameobjects/" + val.getString("image")))),
                            val.getInt("rarity"), val.getString("description"), new ArrayList<>(), ObjectType.valueOf(val.getString("type")));

                    if (val.has("tree_type")) {
                        gameObject.setResource(Resource.valueOf(val.getString("tree_type")));
                    }

                    return gameObject;
                }
            }
        }

        return null;
    }

    public static List<GameObject> getAllObjectsOnMap() {
        List<GameObject> gameObjects = new ArrayList<>();
        Stage currentStage = StageManager.getCurrentStage();

        if (Objects.nonNull(currentStage)) {
            currentStage.getActors().forEach(actor -> {
                if(Objects.nonNull(actor)) {
                    if (actor instanceof GameObject)
                        gameObjects.add((GameObject) actor);
                }
            });
        }
        return gameObjects;
    }
}
