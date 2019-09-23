package com.kingdomlands.game.core.entities.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.monster.Drop;
import com.kingdomlands.game.core.entities.objects.tree.Resource;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.util.*;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.stages.StageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by David K on Apr, 2019
 */
public class GameObject extends Entity {
    private Image image;
    private int rarity;
    private int timeout = 0;
    private String description;
    private List<Drop> drops;
    private boolean isOpen;
    private ObjectType objectType;

    private Resource resource;
    private int resourceHp;
    private int resourceQuantity;

    public GameObject(EntityType entityType, String name, int x, int y, Image image, int rarity, String description, List<Drop> drops, ObjectType objectType) {
        super(entityType, name, x, y);
        this.image = image;
        this.rarity = rarity;
        this.description = description;
        this.drops = drops;
        this.objectType = objectType;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (timeout > 0)
            timeout -= Gdx.graphics.getDeltaTime();

        this.setBounds(getX(), getY(), image.getWidth(), image.getHeight());
        image.setPosition(getX(), getY());
        image.draw(batch, parentAlpha);

        if (this.getName().equals("Chest") && this.rarity == -1) {
            this.isOpen = false;
            this.rarity = Methods.getSecureRandom().nextInt(6) + 1;
            this.drops = createChestDropsFromChest((int) Attribute.getAttributeFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), "Level")
                    .getLevel(PlayerManager.getCurrentPlayer().getPlayerAttributes()), this.rarity);
        }

        /*if (timeout <= 0) {
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                timeout = 30;

                if (Objects.nonNull(Methods.isMouseOverGameObject())) {
                    GameObject object = Methods.isMouseOverGameObject();

                    if (Objects.nonNull(PlayerManager.getCurrentPlayer().getTarget())) {
                        PlayerManager.getCurrentPlayer().setTarget(null);
                        PlayerManager.getCurrentPlayer().setTarget(object);
                    } else {
                        PlayerManager.getCurrentPlayer().setTarget(object);
                    }
                }
            }
        }*/

        if (Objects.nonNull(resource)) {
            if (resourceHp <= 0) {

                //Fix spawn moving and blocking in the player
                /*if (resource.getDepletedId() != -1) {
                    StageManager.addActor(ObjectManager.createObjectById(resource.getDepletedId(), (int)this.getX() + 32, (int)this.getY()));
                }*/

                Gdx.app.postRunnable(this::remove);
            }
        }
    }

    @Override
    public void contextMenu() {
        if (this.getObjectType() == ObjectType.PORTAL) {
            if (this.getName().equals("Return Portal")) {
                ContextManager.setClickedEntity(this);
                com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);
                list.setItems("Choose:", "Return Town", "Examine");

                ContextManager.getTable().add(new ScrollPane(list, Constants.DEFAULT_SKIN));
            } else {
                ContextManager.setClickedEntity(this);
                com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);
                list.setItems("Choose:", "Open Portal", "Examine");

                ContextManager.getTable().add(new ScrollPane(list, Constants.DEFAULT_SKIN));
            }
        } else if (this.getObjectType() == ObjectType.RESOURCE) {
            if (Objects.nonNull(resource)) {
                ContextManager.setClickedEntity(this);
                com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);
                list.setItems("Choose:", resource.getAction(), "Examine");

                ContextManager.getTable().add(new ScrollPane(list, Constants.DEFAULT_SKIN));
            }
        }
    }

    public void removeChest() {
        if (!isOpen) {
            this.isOpen = true;
            StageManager.removeActor(this);
            this.setImage(new Image(new Texture(Gdx.files.internal("C:\\Users\\Travy\\IdeaProjects\\realmlands_game\\desktop\\src\\assets\\gameobjects\\chest_open.png"))));
            StageManager.addActor(this);
            this.drops.forEach(drop -> {
                if (Objects.nonNull(drop)) {
                    int chance = Methods.getSecureRandom().nextInt(1000) + 1;

                    if (chance <= drop.getChance()) {
                        Item item = ItemManager.createItemById(drop.getId(), Methods.getSecureRandom().nextInt(drop.getMax()) + 1);

                        if (Objects.nonNull(item)) {
                            item.setPosition(this.getX(), this.getY() + 75);
                            StageManager.addActor(item);
                        }
                    }
                }
            });
        } else {
            AlertTextManager.add(new AlertText((int) PlayerManager.getCurrentPlayer().getX(), (int) PlayerManager.getCurrentPlayer().getY(), "Chest is open...", DamageType.DEFAULT));
        }
    }

    private List<Drop> createChestDropsFromChest(int playerLevel, int rarity) {
        int maxNumberOfDrops = getMaxNumOfDrops(rarity);
        List<Item> items = ItemManager.pullItemList();
        Collections.shuffle(items);
        List<Drop> drops = new ArrayList<>();
        items.forEach(item -> {
            if (item.getLevel() >= (playerLevel - 2) && item.getLevel() <= (playerLevel + 2) && drops.size() <= maxNumberOfDrops) {
                drops.add(new Drop(item.getId(), 1, item.getMaxDrop(), Methods.getSecureRandom().nextInt(1000) + 1));
            }
        });
        return drops;
    }

    private int getMaxNumOfDrops(int rarity) {
        switch (rarity) {
            case 1:
                return 4;
            case 2:
                return 5;
            case 3:
                return 6;
            case 4:
                return 7;
            case 5:
                return 8;
            case 6:
                return 9;
            case 7:
                return 10;
            default:
                return -1;
        }
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public int getRarity() {
        return rarity;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getDescription() {
        return description;
    }

    public List<Drop> getDrops() {
        return drops;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public int getResourceHp() {
        return resourceHp;
    }

    public void setResourceHp(int resourceHp) {
        this.resourceHp = resourceHp;
    }

    public Resource getResource() {
        return resource;
    }

    public int getResourceQuantity() {
        return resourceQuantity;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
        this.resourceHp = resource.getHp();
        this.resourceQuantity = Methods.random(resource.getAmount()[0], resource.getAmount()[1]);
    }
}
