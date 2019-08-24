package com.kingdomlands.game.core.entities.item;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.projectile.Projectiles;
import com.kingdomlands.game.core.entities.util.Attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemManager {
    private static List<Item> items = new ArrayList<>();

    public static void init() {
        items = pullItemList();
    }

    public static List<Item> pullItemList() {
        List<Item> items = new ArrayList<>();
        JsonReader json = new JsonReader();
        Json jsonR = new Json();
        JsonValue base = json.parse(Gdx.files.internal("items.json"));
        JsonValue itemsArray = base.get("items");

        itemsArray.forEach(c -> {
            if (Objects.nonNull(c)) {
                List<Attribute> attributes = new ArrayList<>();
                if (c.has("aspeed")) {
                    attributes.add(new Attribute("AttackSpeed", c.getDouble("aspeed")));
                }

                JsonValue arr = c.get("attributes");

                arr.forEach(c1 -> {
                    if (Objects.nonNull(c1)) {
                        int attrInt = Integer.parseInt(c1.toString().split(": ")[1]);
                        Attribute a = new Attribute(c1.name, attrInt);

                        if (Objects.nonNull(a)) {
                            attributes.add(a);
                        }
                    }
                });

                Projectiles projectiles = null;

                if (c.has("projectile")) {
                    projectiles = Projectiles.valueOf(c.getString("projectile"));
                }

                items.add(new Item(c.getInt("id"), EntityType.ITEM, c.getString("name"), 100, 100, new Image(new Texture(Gdx.files.internal("items/" + c.getString("image")))),
                        ItemType.valueOf(c.getString("type")), SlotType.valueOf(c.getString("slot")), c.getInt("level"),
                        c.getInt("rarity"), c.getInt("weight"), 1, c.getString("description"), attributes, c.getBoolean("stackable"), projectiles));
            }
        });

        return items;
    }

    public static List<Item> getItems() {
        return items;
    }

    public static Item createItemById(int id, int amount) {
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("items.json"));
        JsonValue itemsArray = base.get("items");

        for (JsonValue val : itemsArray) {
            if (Objects.nonNull(val)) {
                List<Attribute> attributes = new ArrayList<>();
                if (val.has("aspeed")) {
                    attributes.add(new Attribute("AttackSpeed", val.getDouble("aspeed")));
                }

                JsonValue arr = val.get("attributes");

                arr.forEach(c1 -> {
                    if (Objects.nonNull(c1)) {
                        Attribute a = new Attribute(c1.name, c1.asInt());

                        if (Objects.nonNull(a)) {
                            attributes.add(a);
                        }
                    }
                });

                Projectiles projectiles = null;

                if (val.has("projectile")) {
                    projectiles = Projectiles.valueOf(val.getString("projectile"));
                }

                if (val.getInt("id") == id) {
                    return new Item(val.getInt("id"), EntityType.ITEM, val.getString("name"), 100, 100, new Image(new Texture(Gdx.files.internal("items/" + val.getString("image")))),
                            ItemType.valueOf(val.getString("type")), SlotType.valueOf(val.getString("slot")), val.getInt("level"),
                            val.getInt("rarity"), val.getInt("weight"), amount, val.getString("description"), attributes, val.getBoolean("stackable"), projectiles);
                }
            }
        }

        return null;
    }

    public static Image getItemImage(int id) {
        for (Item i : getItems()) {
            if (i.getId() == id) {
                return i.getImage();
            }
        }
        return null;
    }

    public static Item getItemId(int id) {
        for (Item i : getItems()) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    public static int getCostOfItem(Item item) {
        int cost = 0;
        int totalAttrs = 0;

        if (Objects.nonNull(item) && Objects.nonNull(item.getAttributes())) {
            for (Attribute a : item.getAttributes()) {
                if (Objects.nonNull(a)) {
                    totalAttrs += a.getValue();
                }
            }
        }

        cost = totalAttrs * 4;

        if (cost == 0) {
            cost = 1;
        }
        return cost;
    }
}
