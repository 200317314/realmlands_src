package com.kingdomlands.game.core.entities.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.bank.BankManager;
import com.kingdomlands.game.core.entities.player.inventory.Inventory;
import com.kingdomlands.game.core.entities.player.shops.ShopManager;
import com.kingdomlands.game.core.entities.player.ui.HUD;
import com.kingdomlands.game.core.entities.player.ui.UI;
import com.kingdomlands.game.core.entities.player.ui.UIManager;
import com.kingdomlands.game.core.entities.projectile.Projectiles;
import com.kingdomlands.game.core.entities.util.*;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.stages.StageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Item extends Entity implements Json.Serializable {
    private Image image;
    private ItemType itemType;
    private SlotType slotType;
    private int id, level, rarity, weight, amount;
    private String description;
    private List<Attribute> attributes;
    private ShapeRenderer shapeRenderer;
    private boolean stackable, hovered = false;
    private int timeout = 0;
    private Projectiles projectile;

    public Item(int id, EntityType entityType, String name, int x, int y, Image image, ItemType itemType, SlotType slotType, int level, int rarity, int weight, int amount, String description, List<Attribute> attributes, boolean stackable, Projectiles projectile) {
        super(entityType, name, x, y);
        this.id = id;
        this.image = image;
        this.itemType = itemType;
        this.slotType = slotType;
        this.level = level;
        this.rarity = rarity;
        this.weight = weight;
        this.amount = amount;
        this.description = description;
        this.attributes = attributes;
        this.shapeRenderer = new ShapeRenderer();
        this.stackable = stackable;
        this.projectile = projectile;

        if (isAttributable(this.itemType) && Objects.isNull(projectile)) {
            if (this.rarity == -1) {
                this.rarity = getRarityRng();
            }

            addRandomAttributes(this.rarity);

            for (Attribute a : this.attributes) {
                if (!a.getName().equals("AttackSpeed") && a.getValue() == 1.0) {
                    if (a.getName().contains("Regen")) {
                        int stat = Methods.random((this.level + this.rarity), (this.level + this.rarity) * 3) / 8;
                        a.setValue((stat == 0) ? 1 : stat);
                    } else {
                        a.setValue(Methods.random((this.level + this.rarity), (this.level + this.rarity) * 3));
                    }
                } else if (a.getValue() != 1.0 && !a.getName().equals("AttackSpeed")) {
                    a.setValue(Methods.random((this.level + this.rarity), (this.level + this.rarity) * 2) + a.getValue());
                }
            }
        }
    }

    public Item(int id, EntityType entityType, String name, int x, int y, Image image, ItemType itemType, SlotType slotType, int level, int rarity, int weight, int amount, String description, List<Attribute> attributes, ShapeRenderer shapeRenderer, boolean stackable, boolean replace, Projectiles projectile) {
        super(entityType, name, x, y);
        this.id = id;
        this.image = image;
        this.itemType = itemType;
        this.slotType = slotType;
        this.level = level;
        this.rarity = rarity;
        this.weight = weight;
        this.amount = amount;
        this.description = description;
        this.attributes = attributes;
        this.shapeRenderer = shapeRenderer;
        this.stackable = stackable;
        this.projectile = projectile;
    }

    public Item() {
        super(EntityType.ITEM, "BROKEN ITEM", 1, 1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (timeout > 0) {
            timeout -= Gdx.graphics.getDeltaTime();
        }

        if (Objects.nonNull(image)) {
            this.setBounds(getX(), getY(), image.getWidth(), image.getImageHeight());
            image.setPosition(getX(), getY());
            image.draw(batch, 100);
        }


        /*if (!PlayerManager.getCurrentPlayer().getInventory().getItems().contains(this)) {
            this.setBounds(getX(), getY(), 64, 64);
            image.setPosition(getX(), getY());
            image.draw(batch, 100);

            if (isMouseOverMe()) {
                Vector2 pos = StageManager.getCurrentStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
                getFont().setColor(getRarityColor(this.rarity));
                getFont().draw(batch,  getRarityName(this.rarity)+ this.getName() + " Lvl: " + this.level, (int)pos.x + 12 ,(int)pos.y + 20);
                getFont().setColor(Color.WHITE);
                getFont().draw(batch, this.description, (int)pos.x + 12 ,(int)pos.y + 8);

                if (getDamage() != -1) {
                    if (Attribute.getAttributeValueFromList(this.attributes, "AttackSpeed") != -1) {
                        getFont().draw(batch, "Damage: " + Methods.convertNum(getDamage()) + " - " + Methods.convertNum(getDamage()*2) + " | Speed: " + Attribute.getAttributeValueFromList(this.attributes, "AttackSpeed"), (int)pos.x + 12 ,(int)pos.y - 4);
                    } else {
                        getFont().draw(batch, "Damage: " + Methods.convertNum(getDamage()) + " - " + Methods.convertNum(getDamage()*2), (int)pos.x + 12 ,(int)pos.y - 4);
                    }
                } else {

                }

                if (attributes.size() != 0) {
                    int offSet = 12;
                    int multi = 0;
                    getFont().draw(batch, "---------------------------------", (int)pos.x + 12 ,(int)pos.y - 16);
                    for (Attribute a : attributes) {
                        multi++;
                        if (!a.getName().equals("AttackSpeed") || !a.getName().equals("AttackPower")) {
                            getFont().draw(batch, a.getName() + ": +" + Methods.convertNum((int)a.getValue()), (int)pos.x + 12 ,(int)pos.y - (16 + offSet*multi));
                        }
                    }
                }
            }
        }*/

        if (timeout <= 0) {
            if (Objects.nonNull(UIManager.getCurrentTab()) && UIManager.getCurrentTab().equals(HUD.INVENTORY)) {
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    timeout = 30;
                    if (isMouseOverMeRaw() && PlayerManager.getCurrentPlayer().getInventory().getItems().contains(this)) {
                        //PlayerManager.getCurrentPlayer().getInventory().removeItem(PlayerManager.getCurrentPlayer().getInventory().getItemIndex(this));
                        if (!ContextManager.getTable().isVisible()) {
                            ContextManager.open();

                            ContextManager.getTable().setVisible(true);

                            Vector2 stageToScreen = StageManager.getCurrentStage().screenToStageCoordinates(new Vector2(698, 654));

                            ContextManager.getTable().setPosition(stageToScreen.x, stageToScreen.y);
                            ContextManager.getTable().setWidth(400);
                            ContextManager.getTable().setHeight(400);

                            contextMenu();
                        } else {
                            ContextManager.close();
                        }
                    }
                }

                /*if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    timeout = 30;

                    if (this.getItemType().equals(ItemType.CONSUMABLE)) {
                        if (isMouseOverMeRaw() && Attribute.getAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), "Level") >= this.getLevel()) {
                            this.consume();
                        } else if (isMouseOverMeRaw()) {
                            AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX(), (int)PlayerManager.getCurrentPlayer().getY(), "Level is too low...", DamageType.DEFAULT));
                        }
                    } else {
                        if (isMouseOverMeRaw() && PlayerManager.getCurrentPlayer().getInventory().getItems().contains(this)) {
                            if (Attribute.getAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), "Level") >= this.getLevel()) {
                                PlayerManager.getCurrentPlayer().getEquipment().addItem(this);
                            } else {
                                AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX(), (int)PlayerManager.getCurrentPlayer().getY(), "Level is too low...", DamageType.DEFAULT));
                            }
                        }
                    }
                }*/
            }

            if (Objects.nonNull(UIManager.getCurrentTab()) && UIManager.getCurrentTab().equals(HUD.EQUIPMENT)) {
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    timeout = 30;
                    if (isMouseOverMeRaw()) {
                        PlayerManager.getCurrentPlayer().getEquipment().removeItem(this);
                    }
                }
            }
        }
    }

    @Override
    public void contextMenu() {
        ContextManager.setClickedEntity(this);

        if (PlayerManager.getCurrentPlayer().getInventory().getItems().contains(this)) {
            if (this.getItemType() == ItemType.CONSUMABLE) {
                com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);

                if (ShopManager.isShopOpen() && this.getId() != 75) {
                    if (!this.isStackable() || this.isStackable() && this.getAmount() == 1) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Examine");
                    } else if (this.getAmount() >= 100) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Sell 5", "Sell 10", "Sell 25", "Sell 50", "Sell 100", "Sell All", "Examine");
                    } else if (this.getAmount() >= 50) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Sell 5", "Sell 10", "Sell 25", "Sell 50", "Sell All", "Examine");
                    } else if (this.getAmount() >= 25) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Sell 5", "Sell 10", "Sell 25", "Sell All", "Examine");
                    } else if (this.getAmount() >= 10) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Sell 5", "Sell 10", "Sell All", "Examine");
                    } else if (this.getAmount() >= 5) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Sell 5", "Sell All", "Examine");
                    }
                } else {
                    System.out.println("else ree");
                    list.setItems("Choose:", "Consume", "Drop", "Examine");
                }

                if (BankManager.isBankOpen()) {
                    if (!this.isStackable() || this.isStackable() && this.getAmount() == 1) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Examine");
                    } else if (this.getAmount() >= 100) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit 25", "Deposit 50", "Deposit 100", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 50) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit 25", "Deposit 50", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 25) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit 25", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 10) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 5) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit All", "Examine");
                    }
                } else {
                    list.setItems("Choose:", "Consume", "Drop", "Examine");
                }

                ScrollPane scrollPane = new ScrollPane(list, Constants.DEFAULT_SKIN);
                ContextManager.getTable().add(scrollPane);
            } else if (this.getItemType() != ItemType.QUEST && this.getItemType() != ItemType.ITEM) {
                com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);

                if (ShopManager.isShopOpen()) {
                    list.setItems("Choose:", "Wear", "Drop", "Sell 1", "Examine");
                } else {
                    list.setItems("Choose:", "Wear", "Drop", "Examine");
                }

                if (BankManager.isBankOpen()) {
                    if (!this.isStackable() || this.isStackable() && this.getAmount() == 1) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Examine");
                    } else if (this.getAmount() >= 100) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit 25", "Deposit 50", "Deposit 100", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 50) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit 25", "Deposit 50", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 25) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit 25", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 10) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 5) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit All", "Examine");
                    }
                }

                ScrollPane scrollPane = new ScrollPane(list, Constants.DEFAULT_SKIN);
                ContextManager.getTable().add(scrollPane);
            } else {
                com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);

                if (ShopManager.isShopOpen() && this.getId() != 75) {
                    if (!this.isStackable() || this.isStackable() && this.getAmount() == 1) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Examine");
                    } else if (this.getAmount() >= 100) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Sell 5", "Sell 10", "Sell 25", "Sell 50", "Sell 100", "Sell All", "Examine");
                    } else if (this.getAmount() >= 50) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Sell 5", "Sell 10", "Sell 25", "Sell 50", "Sell All", "Examine");
                    } else if (this.getAmount() >= 25) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Sell 5", "Sell 10", "Sell 25", "Sell All", "Examine");
                    } else if (this.getAmount() >= 10) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Sell 5", "Sell 10", "Sell All", "Examine");
                    } else if (this.getAmount() >= 5) {
                        list.setItems("Choose:", "Consume", "Drop", "Sell 1", "Sell 5", "Sell All", "Examine");
                    }
                } else {
                    if (!this.isStackable() || this.isStackable() && this.getAmount() == 1) {
                        list.setItems("Choose:", "Consume", "Drop", "Examine");
                    } else if (this.getAmount() >= 100) {
                        list.setItems("Choose:", "Consume", "Drop", "Drop 1", "Drop 5", "Drop 10", "Drop 25", "Drop 50", "Drop 100", "Drop All", "Examine");
                    } else if (this.getAmount() >= 50) {
                        list.setItems("Choose:", "Consume", "Drop 1", "Drop 5", "Drop 10", "Drop 25", "Drop 50", "Drop All", "Examine");
                    } else if (this.getAmount() >= 25) {
                        list.setItems("Choose:", "Consume", "Drop 1", "Drop 5", "Drop 10", "Drop 25", "Drop All", "Examine");
                    } else if (this.getAmount() >= 10) {
                        list.setItems("Choose:", "Consume", "Drop 1", "Drop 5", "Drop 10", "Drop All", "Examine");
                    } else if (this.getAmount() >= 5) {
                        list.setItems("Choose:", "Consume", "Drop 1", "Drop 5", "Drop All", "Examine");
                    }
                }

                if (BankManager.isBankOpen()) {
                    if (!this.isStackable() || this.isStackable() && this.getAmount() == 1) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Examine");
                    } else if (this.getAmount() >= 100) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit 25", "Deposit 50", "Deposit 100", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 50) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit 25", "Deposit 50", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 25) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit 25", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 10) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit 10", "Deposit All", "Examine");
                    } else if (this.getAmount() >= 5) {
                        list.setItems("Choose:", "Consume", "Drop", "Deposit 1", "Deposit 5", "Deposit All", "Examine");
                    }
                }/* else {
                    if (!this.isStackable() || this.isStackable() && this.getAmount() == 1) {
                        list.setItems("Choose:", "Consume", "Drop", "Examine");
                    } else if (this.getAmount() >= 100) {
                        list.setItems("Choose:", "Consume", "Drop", "Drop 1", "Drop 5", "Drop 10", "Drop 25", "Drop 50", "Drop 100", "Drop All", "Examine");
                    } else if (this.getAmount() >= 50) {
                        list.setItems("Choose:", "Consume", "Drop 1", "Drop 5", "Drop 10", "Drop 25", "Drop 50", "Drop All", "Examine");
                    } else if (this.getAmount() >= 25) {
                        list.setItems("Choose:", "Consume", "Drop 1", "Drop 5", "Drop 10", "Drop 25", "Drop All", "Examine");
                    } else if (this.getAmount() >= 10) {
                        list.setItems("Choose:", "Consume", "Drop 1", "Drop 5", "Drop 10", "Drop All", "Examine");
                    } else if (this.getAmount() >= 5) {
                        list.setItems("Choose:", "Consume", "Drop 1", "Drop 5", "Drop All", "Examine");
                    }
                }*/

                ScrollPane scrollPane = new ScrollPane(list, Constants.DEFAULT_SKIN);
                ContextManager.getTable().add(scrollPane);
            }
        } else if (!UIManager.isMouseOverTab()) {
            com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);
            list.setItems("Choose:", "Take", "Examine");

            ScrollPane scrollPane = new ScrollPane(list, Constants.DEFAULT_SKIN);
            ContextManager.getTable().add(scrollPane);
        }
    }

    public boolean isMouseOverMe() {
        if (this.getBounds().contains(StageManager.getCurrentStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(),Gdx.input.getY())))) {
            return true;
        }

        return false;
    }

    public boolean isMouseOverMeRaw() {
        if (this.getBounds().contains(new Vector2(Gdx.input.getX(),800 - Gdx.input.getY()))) {
            return true;
        }

        return false;
    }

    public int getDamage() {
        int dmg = -1;

        if (Attribute.getAttributeValueFromList(this.attributes, "AttackPower") != -1) {
            dmg += (int)Attribute.getAttributeValueFromList(this.attributes, "AttackPower");
        }

        if (Attribute.getAttributeValueFromList(this.attributes, "SpellPower") != -1) {
            dmg += (int)Attribute.getAttributeValueFromList(this.attributes, "SpellPower");
        }

        if (Attribute.getAttributeValueFromList(this.attributes, "Strength") != -1) {
            dmg += (int)Attribute.getAttributeValueFromList(this.attributes, "Strength");
        }

        return dmg;
    }

    public boolean consume() {
        if (Attribute.getAttributeFromList(this.attributes, "CurrentHp").getValue() > 0) {
            Attribute.setAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), "CurrentHp", Attribute.getAttributeFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), "CurrentHp").getValue() + Attribute.getAttributeFromList(this.attributes, "CurrentHp").getValue());
            AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX(), (int)PlayerManager.getCurrentPlayer().getY(), "+" + Attribute.getAttributeFromList(this.attributes, "CurrentHp").getValue() + " hp", DamageType.HPREGEN));
            PlayerManager.getCurrentPlayer().getInventory().deleteItem(this);
        }

        return false;
    }

    public int getMaxDrop() {
        if (stackable) {
            if (getName().equals("Iron Arrows")) {
                switch (this.rarity) {
                    case 1:
                        return 15;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        return 15 * this.rarity;
                }
            }

            if (getName().equals("Coins")) {
                switch (this.rarity) {
                    case 1:
                        return 100;
                    case 2:
                        return 250;
                    case 3:
                        return 500;
                    case 4:
                        return 1000;
                    case 5:
                        return 2500;
                    case 6:
                        return 5000;
                    case 7:
                        return 10000;
                }
            }

            switch (this.rarity) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    return 3 * this.rarity;
                default:
                    return -1;
            }
        }
        return 1;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Projectiles getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectiles projectile) {
        this.projectile = projectile;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    private boolean isAttributable(ItemType itemType) {
        return itemType.equals(ItemType.SWORD) || itemType.equals(ItemType.AXE) || itemType.equals(ItemType.MACE) || itemType.equals(ItemType.ARMOR) || itemType.equals(ItemType.ACCESSORY) || itemType.equals(ItemType.BOW) || itemType.equals(ItemType.WAND);
    }

    private boolean isWeapon(ItemType itemType) {
        return itemType.equals(ItemType.SWORD) || itemType.equals(ItemType.AXE) || itemType.equals(ItemType.MACE) || itemType.equals(ItemType.BOW) || itemType.equals(ItemType.WAND);
    }

    private boolean isArmor(ItemType itemType) {
        return itemType.equals(ItemType.ARMOR) || itemType.equals(ItemType.ACCESSORY);
    }

    private void addRandomAttributes(int rarity) {
        //String[] attrs = {"Stamina", "Strength", "Agility", "Intellect", "Crit", "Haste", "ExpBoost", "BonusArmor", "LifeSteal", "SpellPower", "CastingSpeed", "ManaRegen", "HpRegen", "Dodge", "Parry", "Block", "Resist", "MovementSpeed", "MaxHp", "MaxMana"};
        String[] attrs = {"Stamina", "Strength", "Agility", "Intellect", "BonusArmor", "SpellPower", "ManaRegen", "HpRegen", "MaxHp", "MaxMana"};



        for (int i = 0; i < rarity; i++) {
            int rngNum = Methods.getSecureRandom().nextInt(attrs.length);

            this.attributes.add(new Attribute(attrs[rngNum], 1));
        }
    }

    @Override
    public void write(Json json) {
        json.writeValue("class", "com.kingdomlands.game.core.entities.item.Item");
        json.writeValue("id", getId());
        json.writeValue("amount", getAmount());
        json.writeValue("itemAttributes", getAttributes());
        json.writeValue("rarity", getRarity());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has("id") && jsonData.getInt("id") != 0) {
            setId(jsonData.getInt("id"));
            setAmount(jsonData.getInt("amount"));

            setRarity(jsonData.getInt("rarity"));
            setStackable(ItemManager.getItemId(getId()).stackable);
            setLevel(ItemManager.getItemId(getId()).level);
            setName(ItemManager.getItemId(getId()).getName());
            setItemType(ItemManager.getItemId(getId()).itemType);
            setSlotType(ItemManager.getItemId(getId()).getSlotType());
            setDescription(ItemManager.getItemId(getId()).description);

            List<Attribute> attributes = new ArrayList<>();
            JsonValue attrs = jsonData.get("itemAttributes");
            setImage(ItemManager.getItemImage(getId()));

            for (JsonValue v : attrs) {
                attributes.add(json.fromJson(Attribute.class, v.toString()));
            }

            setAttributes(attributes);
        }
    }
}
