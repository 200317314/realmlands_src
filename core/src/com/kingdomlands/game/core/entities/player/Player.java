package com.kingdomlands.game.core.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.item.ItemType;
import com.kingdomlands.game.core.entities.item.SlotType;
import com.kingdomlands.game.core.entities.monster.Monster;
import com.kingdomlands.game.core.entities.npc.Npc;
import com.kingdomlands.game.core.entities.npc.NpcManager;
import com.kingdomlands.game.core.entities.objects.GameObject;
import com.kingdomlands.game.core.entities.objects.ObjectManager;
import com.kingdomlands.game.core.entities.objects.portal.PortalManager;
import com.kingdomlands.game.core.entities.player.bank.BankManager;
import com.kingdomlands.game.core.entities.player.chat.ChatManager;
import com.kingdomlands.game.core.entities.player.equipment.Equipment;
import com.kingdomlands.game.core.entities.player.equipment.EquipmentSlot;
import com.kingdomlands.game.core.entities.player.inventory.Inventory;
import com.kingdomlands.game.core.entities.player.shops.ShopManager;
import com.kingdomlands.game.core.entities.player.ui.HUD;
import com.kingdomlands.game.core.entities.player.ui.UI;
import com.kingdomlands.game.core.entities.player.ui.UIManager;
import com.kingdomlands.game.core.entities.projectile.Projectile;
import com.kingdomlands.game.core.entities.util.*;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.stages.StageManager;
import com.kingdomlands.game.core.stages.StageRender;
import com.kingdomlands.game.core.stages.Stages;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by David K on Mar, 2019
 */
public class Player extends Entity implements Json.Serializable {
    private final int RANGE = 90;
    private Image image, playerAvatar;
    private Entity target = null;
    private double time = 0;
    public static double timeout = 0;
    private int regenTime = 0, id;
    private List<Attribute> playerAttributes = new ArrayList<>();
    private List<Skill> playerSkills = new ArrayList<>();
    private Vector2 tilePoint = null;
    private Inventory inventory;
    private Equipment equipment;
    private PlayerClass playerClass;
    private List<Item> bank;

    public Player(int id, EntityType entityType, String name, int x, int y) {
        super(entityType, name, x, y);
        this.id = id;
        image = new Image(new Texture(Gdx.files.internal("images/player.png")));

        playerAttributes.add(new Attribute("Stamina", 1));
        playerAttributes.add(new Attribute("Strength", 1));
        playerAttributes.add(new Attribute("Agility", 1));
        playerAttributes.add(new Attribute("Intellect", 1));
        playerAttributes.add(new Attribute("Crit", 1));
        playerAttributes.add(new Attribute("Haste", 1));
        playerAttributes.add(new Attribute("ExpBoost", 1));
        playerAttributes.add(new Attribute("BonusArmor", 1));
        playerAttributes.add(new Attribute("LifeSteal", 1));
        playerAttributes.add(new Attribute("AttackPower", 1));
        playerAttributes.add(new Attribute("SpellPower", 1));
        playerAttributes.add(new Attribute("CastingSpeed", 1));
        playerAttributes.add(new Attribute("ManaRegen", 1));
        playerAttributes.add(new Attribute("HpRegen", 1));
        playerAttributes.add(new Attribute("Armor", 1));
        playerAttributes.add(new Attribute("Dodge", 1));
        playerAttributes.add(new Attribute("Parry", 1));
        playerAttributes.add(new Attribute("Block", 1));
        playerAttributes.add(new Attribute("Resist", 1));
        playerAttributes.add(new Attribute("MovementSpeed", 6));
        playerAttributes.add(new Attribute("CurrentHp", 100));
        playerAttributes.add(new Attribute("MaxHp", 100));
        playerAttributes.add(new Attribute("CurrentExp", 99));
        playerAttributes.add(new Attribute("MaxExp", 100));
        playerAttributes.add(new Attribute("Level", 5));
        playerAttributes.add(new Attribute("CurrentMana", 100));
        playerAttributes.add(new Attribute("MaxMana", 100));
        playerAttributes.add(new Attribute("AttackSpeed", 0.5));
        playerAttributes.add(new Attribute("Slowness", 0));

        playerSkills.add(new Skill("Sword Proficiency", 1, 0, 100, "sword.png"));
        playerSkills.add(new Skill("Axe Proficiency", 1, 0, 100, "axe.png"));
        playerSkills.add(new Skill("Mace Proficiency", 1, 0, 100, "mace.png"));
        playerSkills.add(new Skill("Staff Proficiency", 1, 0, 100, "staff.png"));
        playerSkills.add(new Skill("Bow Proficiency", 1, 0, 100, "bow.png"));
        playerSkills.add(new Skill("Woodcutting", 1, 0, 100, "woodcutting.png"));
        playerSkills.add(new Skill("Fishing", 1, 0, 100, "fishing.png"));
        playerSkills.add(new Skill("Mining", 1, 0, 100, "mining.png"));
        playerSkills.add(new Skill("Smithing", 1, 0, 100, "smithing.png"));

        List<Item> items = new ArrayList<>();
        inventory = new Inventory(this.getName(), items);

        List<EquipmentSlot> slots = new ArrayList<>();
        equipment = new Equipment(slots);
    }

    public Player() {
        super(EntityType.PLAYER, "BROKEN PLAYER", 1, 1);
        //ChatManager.init();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        time += Gdx.graphics.getDeltaTime();
        timeout -= Gdx.graphics.getDeltaTime();
        regenTime ++;

        if (Objects.nonNull(target)) {
            if (target instanceof Monster) {
                if (Objects.nonNull(tilePoint)) {
                    tilePoint = null;
                }
            }
        }

        //slowDown();

        this.setBounds(getX(), getY(), 64, 64);

        if (Objects.nonNull(image)) {
            image.setPosition(getX(), getY());
            image.draw(batch, parentAlpha);
        }

        if (Attribute.getAttributeFromList(this.playerAttributes, "CurrentExp").getCurrentExp(this.playerAttributes) >= Attribute.getAttributeFromList(this.playerAttributes, "MaxExp").getMaxExp(this.playerAttributes)) {
            levelUp();
        }

        getFont().setColor(Color.WHITE);

        if (UI.INVENTORY_ICON.getBounds().contains(new Vector2(Gdx.input.getX(), 800 - Gdx.input.getY()))) {
            UI.INVENTORY_ICON_PLACE.setImage(UI.INVENTORY_ICON_CLICKED.getImage());
        } else {
            UI.INVENTORY_ICON_PLACE.setImage(UI.INVENTORY_ICON.getImage());
        }

        if (UI.ATTRIBUTE_ICON.getBounds().contains(new Vector2(Gdx.input.getX(), 800 - Gdx.input.getY()))) {
            UI.ATTRIBUTE_ICON_PLACE.setImage(UI.ATTRIBUTE_ICON_CLICKED.getImage());
        } else {
            UI.ATTRIBUTE_ICON_PLACE.setImage(UI.ATTRIBUTE_ICON.getImage());
        }

        if (UI.EQUIPMENT_ICON.getBounds().contains(new Vector2(Gdx.input.getX(), 800 - Gdx.input.getY()))) {
            UI.EQUIPMENT_ICON_PLACE.setImage(UI.EQUIPMENT_ICON_CLICKED.getImage());
        } else {
            UI.EQUIPMENT_ICON_PLACE.setImage(UI.EQUIPMENT_ICON.getImage());
        }

        if (UI.SKILL_ICON.getBounds().contains(new Vector2(Gdx.input.getX(), 800 - Gdx.input.getY()))) {
            UI.SKILL_ICON_PLACE.setImage(UI.SKILL_ICON_CLICKED.getImage());
        } else {
            UI.SKILL_ICON_PLACE.setImage(UI.SKILL_ICON.getImage());
        }

        if (UI.ABILITY_ICON.getBounds().contains(new Vector2(Gdx.input.getX(), 800 - Gdx.input.getY()))) {
            UI.ABILITY_ICON_PLACE.setImage(UI.ABILITY_ICON_CLICKED.getImage());
        } else {
            UI.ABILITY_ICON_PLACE.setImage(UI.ABILITY_ICON.getImage());
        }

        if (UI.FAMILIAR_ICON.getBounds().contains(new Vector2(Gdx.input.getX(), 800 - Gdx.input.getY()))) {
            UI.FAMILIAR_ICON_PLACE.setImage(UI.FAMILIAR_ICON_CLICKED.getImage());
        } else {
            UI.FAMILIAR_ICON_PLACE.setImage(UI.FAMILIAR_ICON.getImage());
        }

        if (UI.SETTINGS_ICON.getBounds().contains(new Vector2(Gdx.input.getX(), 800 - Gdx.input.getY()))) {
            UI.SETTINGS_ICON_PLACE.setImage(UI.SETTINGS_ICON_CLICKED.getImage());
        } else {
            UI.SETTINGS_ICON_PLACE.setImage(UI.SETTINGS_ICON.getImage());
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !ShopManager.isShopOpen() && !BankManager.isBankOpen() && !PortalManager.isPortalOpen()) {
            /*if (ContextManager.getContextMenu().isVisible()) {
                ContextManager.outSideClick();
            }*/

            Vector2 clickedRaw = new Vector2(Gdx.input.getX(), Constants.WINDOW_HEIGHT - Gdx.input.getY());
            Vector2 clickVector = new Vector2(StageManager.getCurrentStage().screenToStageCoordinates(new Vector2(Gdx.input.getX() - 64, Gdx.input.getY() + 64)));

            if (Objects.isNull(clickedIcon(clickedRaw))) {
                /*if (!ContextManager.getContextMenu().isVisible()) {
                    if (Objects.nonNull(UIManager.getCurrentTab())) {
                        if (!UIManager.getCurrentTab().getBounds().contains(clickedRaw)) {
                            tilePoint = Methods.getCenter(new Vector2((int)clickVector.x, (int)clickVector.y));

                            if (Objects.nonNull(target)) {
                                target = null;
                            }
                        }
                    } else {
                        tilePoint = Methods.getCenter(new Vector2((int)clickVector.x, (int)clickVector.y));

                        if (Objects.nonNull(target)) {
                            target = null;
                        }
                    }
                }*/
            } else {
                if (clickedIcon(clickedRaw).equals(UI.INVENTORY_ICON_PLACE)) {
                    if (Objects.nonNull(UIManager.getCurrentTab()) && UIManager.getCurrentTab().equals(HUD.INVENTORY)) {
                        UIManager.setCurrentTab(HUD.INVENTORY);
                    } else {
                        UIManager.setCurrentTab(HUD.INVENTORY);
                    }
                }

                if (clickedIcon(clickedRaw).equals(UI.ATTRIBUTE_ICON_PLACE)) {
                    if (Objects.nonNull(UIManager.getCurrentTab()) && UIManager.getCurrentTab().equals(HUD.ATTRIBUTE)) {
                        UIManager.setCurrentTab(HUD.ATTRIBUTE);
                    } else {
                        UIManager.setCurrentTab(HUD.ATTRIBUTE);
                    }
                }

                if (clickedIcon(clickedRaw).equals(UI.EQUIPMENT_ICON_PLACE)) {
                    if (Objects.nonNull(UIManager.getCurrentTab()) && UIManager.getCurrentTab().equals(HUD.EQUIPMENT)) {
                        UIManager.setCurrentTab(HUD.EQUIPMENT);
                    } else {
                        UIManager.setCurrentTab(HUD.EQUIPMENT);
                    }
                }

                if (clickedIcon(clickedRaw).equals(UI.SKILL_ICON_PLACE)) {
                    if (Objects.nonNull(UIManager.getCurrentTab()) && UIManager.getCurrentTab().equals(HUD.SKILL)) {
                        UIManager.setCurrentTab(HUD.SKILL);
                    } else {
                        UIManager.setCurrentTab(HUD.SKILL);
                    }
                }

                if (clickedIcon(clickedRaw).equals(UI.SETTINGS_ICON_PLACE)) {
                    if (Objects.nonNull(UIManager.getCurrentTab()) && UIManager.getCurrentTab().equals(HUD.SETTINGS)) {
                        UIManager.setCurrentTab(HUD.SETTINGS);
                    } else {
                        UIManager.setCurrentTab(HUD.SETTINGS);
                    }
                }
            }

            if (ContextManager.getTable().isVisible()) {
                Vector2 posMouse = StageManager.getCurrentStage().screenToStageCoordinates(new Vector2(Gdx.input.getX() - 10 , Gdx.input.getY() - 120));
                Rectangle box = new Rectangle(ContextManager.getTable().getX(), ContextManager.getTable().getY(), 180, 180);

                if (!box.contains(posMouse)) {
                    ContextManager.close();
                }
            } else if (!ContextManager.getTable().isVisible()) {
                if (Objects.isNull(clickedIcon(clickedRaw))) {
                    if (!ContextManager.getContextMenu().isVisible()) {
                        if (Objects.nonNull(UIManager.getCurrentTab())) {
                            if (!UIManager.isMouseOverTab() && timeout <= 0) {
                                if (!StageManager.clickedAnEntity(new Vector2(Gdx.input.getX(), Constants.WINDOW_HEIGHT - Gdx.input.getY()))) {
                                    tilePoint = Methods.getCenter(new Vector2((int)clickVector.x, (int)clickVector.y));

                                    if (Objects.nonNull(target)) {
                                        target = null;
                                    }
                                }
                            }
                        } else if (timeout <= 0) {
                            if (!StageManager.clickedAnEntity(new Vector2(Gdx.input.getX(), Constants.WINDOW_HEIGHT - Gdx.input.getY()))) {
                                tilePoint = Methods.getCenter(new Vector2((int)clickVector.x, (int)clickVector.y));

                                if (Objects.nonNull(target)) {
                                    target = null;
                                }
                            }
                        }
                    }
                }
            }
        }

        if(Objects.nonNull(tilePoint)) {
            if (getDistanceVector(tilePoint) <= RANGE) {
                tilePoint = null;
            } else {
                if (tilePoint.x <= 15600 && tilePoint.x >= 66 && tilePoint.y <= 15600 && tilePoint.y >= 66 && timeout <= 0) {
                    this.move(Methods.getCenter(tilePoint), Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").getMovementSpeed(this.playerAttributes));
                }
            }
        }

        if (Objects.nonNull(target) && target instanceof Monster) {
            inCombat();
        }

        if (Objects.nonNull(target) && target instanceof Item) {
           pickUpItem();
        }

        if (Objects.nonNull(target) && target instanceof GameObject) {
            if (target.getName().equals("Chest")) {
                openChest();
            }
        }

        StageManager.getCurrentStage().getCamera().position.set(getX(), getY(), 0);

        if(isDead()){
            PlayerManager.getCurrentPlayer().setTarget(null);
            inventory.getItems().forEach(item -> PlayerManager.getCurrentPlayer().getInventory().removeItem(inventory.getItemIndex(item)));
            Attribute.setAttributeValueFromList(this.playerAttributes, "CurrentHp", Attribute.getAttributeValueFromList(this.playerAttributes, "MaxHp"));

            Gdx.app.postRunnable(() -> {
                UIManager.setCurrentTab(null);

                Player player = PlayerManager.getCurrentPlayer();

                StageManager.getAllEntities().forEach(e -> {
                    if (e instanceof GameObject || e instanceof Npc || e instanceof Monster || e instanceof Item) {
                        e.remove();
                    }
                });

                StageRender.loadTiledMap(Stages.TOWN.getTiledMap());

                Gdx.input.setInputProcessor(StageManager.getCurrentStage());
                StageManager.addActor(player);
                Objects.requireNonNull(player).setPosition(7265, 7800);

                //Add Buildings
                StageManager.addActor(ObjectManager.createObjectById(18, 7180, 7860));

                //Add Npcs
                Npc seymour = NpcManager.createNpc(1);
                seymour.setPosition(9285, 16000 - 6783);
                StageManager.addActor(seymour);

                Npc ghorza = NpcManager.createNpc(2);
                ghorza.setPosition(7052, 16000 - 8766);
                StageManager.addActor(ghorza);

                Npc gamel = NpcManager.createNpc(3);
                gamel.setPosition(7367, 16000 - 8853);
                StageManager.addActor(gamel);

                Npc romella = NpcManager.createNpc(4);
                romella.setPosition(7749, 16000 - 8148);
                StageManager.addActor(romella);

                Npc rabaz = NpcManager.createNpc(5);
                rabaz.setPosition(7990, 16000 - 8306);
                StageManager.addActor(rabaz);

                ChatManager.init();
            });
        }

        checkRegen();
    }

    @Override
    public void contextMenu() {
        ContextManager.setClickedEntity(this);
        com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);
        list.setItems(getName(), "Examine");

        ContextManager.getTable().add(new ScrollPane(list, Constants.DEFAULT_SKIN));
    }

    public void addExp(double exp) {
        Attribute.setAttributeValueFromList(this.playerAttributes,
                "CurrentExp",
                Attribute.getAttributeFromList(this.playerAttributes, "CurrentExp").getCurrExp(this.playerAttributes) + exp);
    }

    public void checkRegen() {
        if (regenTime >= 60) {
            if ((Attribute.getAttributeFromList(this.playerAttributes, "CurrentHp").getCurrentHp(this.playerAttributes) < Attribute.getAttributeFromList(this.playerAttributes, "MaxHp").getMaxHp(this.playerAttributes))) {
                Attribute.setAttributeValueFromList(this.playerAttributes,
                        "CurrentHp",
                        Attribute.getAttributeFromList(this.playerAttributes, "CurrentHp").getCurrentHp(this.playerAttributes) + Attribute.getAttributeFromList(this.playerAttributes, "HpRegen").getHpRegen(this.playerAttributes));
            }

            if ((Attribute.getAttributeFromList(this.playerAttributes, "CurrentMana").getCurrMana(this.playerAttributes) < Attribute.getAttributeFromList(this.playerAttributes, "MaxMana").getMaxMana(this.playerAttributes))) {
                Attribute.setAttributeValueFromList(this.playerAttributes,
                        "CurrentMana",
                        Attribute.getAttributeFromList(this.playerAttributes, "CurrentMana").getCurrMana(this.playerAttributes) + Attribute.getAttributeFromList(this.playerAttributes, "ManaRegen").getManaRegen(this.playerAttributes));
            }
            regenTime = 0;
        }

        if (Attribute.getAttributeValueFromList(this.playerAttributes, "CurrentHp") > Attribute.getAttributeValueFromList(this.playerAttributes, "MaxHp")) {
            Attribute.setAttributeValueFromList(this.playerAttributes, "CurrentHp", Attribute.getAttributeValueFromList(this.playerAttributes, "MaxHp"));
        }

        if (Attribute.getAttributeValueFromList(this.playerAttributes, "CurrentMana") > Attribute.getAttributeValueFromList(this.playerAttributes, "MaxMana")) {
            Attribute.setAttributeValueFromList(this.playerAttributes, "CurrentMana", Attribute.getAttributeValueFromList(this.playerAttributes, "MaxMana"));
        }
    }

    public void takeDamage(int damage, Entity damager, boolean crit) {
        if (!Objects.nonNull(target)) {
            if (damager instanceof Monster) {
                target = damager;
            }
        }

        if (damager instanceof Monster) {
            SoundManager.playSoundFx(Gdx.audio.newSound(Gdx.files.internal("sounds/monster_attack.wav")));
        } else if (damager instanceof Projectile) {
            if (((Projectile) damager).getProjectiles().isMagical()) {
                SoundManager.playSoundFx(Gdx.audio.newSound(Gdx.files.internal("sounds/magic_attack.wav")));
            } else {
                SoundManager.playSoundFx(Gdx.audio.newSound(Gdx.files.internal("sounds/arrow_attack.wav")));
            }
        }

        if (crit) {
            Attribute.setAttributeValueFromList(this.playerAttributes,
                    "CurrentHp",
                    Attribute.getAttributeFromList(this.playerAttributes, "CurrentHp").getCurrentHp(this.playerAttributes) - (Attribute.getNegatedDamage(damage, this.playerAttributes)  * 2));
            DamageTextManager.add(new DamageText((int)this.getX() + 32, (int)this.getY() + 32, (int)Attribute.getNegatedDamage(damage, this.playerAttributes)  * 2, 100, DamageType.CRITICAL));
            ChatManager.addChat("[Battle]: " + damager.getName() + " has attacked you for " + (int)Attribute.getNegatedDamage(damage, this.playerAttributes)  * 2 + " damage.");
        } else {
            Attribute.setAttributeValueFromList(this.playerAttributes,
                    "CurrentHp",
                    Attribute.getAttributeFromList(this.playerAttributes, "CurrentHp").getCurrentHp(this.playerAttributes) - Attribute.getNegatedDamage(damage, this.playerAttributes) );
            DamageTextManager.add(new DamageText((int)this.getX() + 32, (int)this.getY() + 32, (int)Attribute.getNegatedDamage(damage, this.playerAttributes) , 100, DamageType.PHYSICAL));
            ChatManager.addChat("[Battle]: " + damager.getName() + " has attacked you for " + (int)Attribute.getNegatedDamage(damage, this.playerAttributes)  + " damage.");
        }
    }

    public void levelUp() {
        PlayerClass.levelUp(this);
    }

    public void inCombat() {
        if (Objects.nonNull(target)) {
            Monster monster = (Monster) target;
            double distance = getDistance(monster);

            if (Objects.nonNull(getEquipment().getItemFromSlot(SlotType.RIGHTHAND)) && Objects.nonNull(getEquipment().getItemFromSlot(SlotType.RIGHTHAND).getProjectile())) {
                if (distance <= getEquipment().getItemFromSlot(SlotType.RIGHTHAND).getProjectile().getRange()*64 && time >= Attribute.getAttributeFromList(this.playerAttributes, "AttackSpeed").getAttackSpeed(this.playerAttributes)) {
                    if (Objects.nonNull(getEquipment().getItemFromSlot(SlotType.CAPE)) && Objects.nonNull(getEquipment().getItemFromSlot(SlotType.CAPE).getProjectile()) || !getEquipment().getItemFromSlot(SlotType.RIGHTHAND).getProjectile().isAmmo()) {
                        if (Objects.nonNull(getEquipment().getItemFromSlot(SlotType.CAPE)) && getEquipment().getItemFromSlot(SlotType.CAPE).getProjectile().equals(getEquipment().getItemFromSlot(SlotType.RIGHTHAND).getProjectile()) || !getEquipment().getItemFromSlot(SlotType.RIGHTHAND).getProjectile().isAmmo()) {
                            int damage = (int) Attribute.getProjectileDamage(this.playerAttributes);
                            Item rightHandItem = this.equipment.getItemFromSlot(SlotType.RIGHTHAND);

                            if (Objects.nonNull(rightHandItem) && rightHandItem.getItemType() == ItemType.WAND) {
                                damage = (int)Attribute.getMagicalDamage(this.playerAttributes);
                            }
                            time = 0;

                            StageManager.addActor(new Projectile(EntityType.GAMEOBJECT, "Arrow",
                                    (int)this.getX(), (int)this.getY() + 32, getEquipment().getItemFromSlot(SlotType.RIGHTHAND).getProjectile(), damage, monster));

                            if (getEquipment().getItemFromSlot(SlotType.RIGHTHAND).getProjectile().isAmmo()) {
                                getEquipment().removeQuantityOne(SlotType.CAPE);
                            }
                        } else {
                            time = 0;
                            AlertTextManager.add(new AlertText((int)getX(), (int)getY(), "You need proper arrows.", DamageType.DEFAULT));
                        }
                    } else {
                        time = 0;
                        AlertTextManager.add(new AlertText((int)getX(), (int)getY(), "You need arrows.", DamageType.DEFAULT));
                    }
                } else if (distance >= getEquipment().getItemFromSlot(SlotType.RIGHTHAND).getProjectile().getRange()*64) {
                    move(new Vector2((int)monster.getX(), (int)monster.getY()), Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").getMovementSpeed(this.playerAttributes) * 1.2);
                }
            } else {
                if (distance <= RANGE) {
                    if (distance <= RANGE && time >= Attribute.getAttributeFromList(this.playerAttributes, "AttackSpeed").getAttackSpeed(this.playerAttributes)) {
                        int damage = (int)Attribute.getPhysicalDamage(this.playerAttributes);

                        if (Methods.random(1, 100) <= Attribute.getAttributeFromList(this.playerAttributes, "Crit").getCrit(this.playerAttributes)) {
                            monster.takeDamage(damage, true, null);
                        } else {
                            monster.takeDamage(damage, false, null);
                        }
                        time = 0;
                    }
                } else {
                    move(new Vector2((int)monster.getX(), (int)monster.getY()), Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").getMovementSpeed(this.playerAttributes) * 1.2);
                }
            }
        }
    }

    public void pickUpItem() {
        if (Objects.nonNull(target)) {
            Item item = (Item) target;
            double distance = getDistance(item);

            if (distance <= RANGE) {
                inventory.addItem(item);
                target = null;
            } else {
                move(new Vector2((int)item.getX() + 32, (int)item.getY() + 32), Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").getMovementSpeed(this.playerAttributes) * 1.2);
            }
        }
    }

    public void slowDown() {
        if(inventory.getInventoryWeight() <= inventory.getMaxCarryWeight()) {
            Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").setValue(Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").getMovementSpeed(this.playerAttributes) + Attribute.getAttributeFromList(this.playerAttributes, "Slowness").getSlowness(this.playerAttributes));
            Attribute.getAttributeFromList(this.playerAttributes, "Slowness").setValue(0);
        }

        if((getSlownessPercentFromWeightPercentage() != Attribute.getAttributeFromList(this.playerAttributes, "Slowness").getSlowness(this.playerAttributes) || getSlownessPercentFromWeightPercentage() == 0) && inventory.getInventoryWeight() > inventory.getMaxCarryWeight()) {
            Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").setValue(Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").getMovementSpeed(this.playerAttributes) + Attribute.getAttributeFromList(this.playerAttributes, "Slowness").getSlowness(this.playerAttributes));

            Attribute.getAttributeFromList(this.playerAttributes, "Slowness").setValue(Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").getMovementSpeed(this.playerAttributes) * getSlownessPercentFromWeightPercentage());

            Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").setValue(Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").getMovementSpeed(this.playerAttributes) - Attribute.getAttributeFromList(this.playerAttributes, "Slowness").getSlowness(this.playerAttributes));
        }
    }

    public double getSlownessPercentFromWeightPercentage() {
        //ratio is 75:90 which equals 1:1.2
        return BigDecimal.valueOf(inventory.getPercentageAboveMaxWeight() * 1.2)
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public void openChest() {
        if (Objects.nonNull(target)) {
            GameObject chest = (GameObject) target;

            if (this.getDistance(this.target) <= this.RANGE) {
                chest.removeChest();
                target = null;
            } else {
                move(new Vector2((int) chest.getX() + 32, (int) chest.getY() + 32), Attribute.getAttributeFromList(this.playerAttributes, "MovementSpeed").getMovementSpeed(this.playerAttributes) * 1.2);
            }
        }
    }

    public boolean isDead() {
        return Attribute.getAttributeValueFromList(this.playerAttributes, "CurrentHp") <= 0;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Attribute> getPlayerAttributes() {
        return playerAttributes;
    }

    public void setPlayerAttributes(List<Attribute> playerAttributes) {
        this.playerAttributes = playerAttributes;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Vector2 getCenter() {
        return new Vector2(getX() + 32, getY() + 32);
    }

    private double getExpForLevel(double level) {
        return (50 * (level) * (level) * (level) - 150 * (level) * (level) + 400 * (level)) / 3;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setPlayerSkills(List<Skill> playerSkills) { this.playerSkills = playerSkills; }

    public List<Skill> getPlayerSkills() { return this.playerSkills; }

    private UI clickedIcon(Vector2 click) {
        for (UI ui : UIManager.getIcons()) {
            if (Objects.nonNull(ui)) {
                if (ui.getBounds().contains(click)) {
                    return ui;
                }
            }
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    public List<Item> getBank() {
        return bank;
    }

    public void setBank(List<Item> bank) {
        this.bank = bank;
    }

    public Image getPlayerAvatar() {
        return playerAvatar;
    }

    public void setPlayerAvatar(Image playerAvatar) {
        this.playerAvatar = playerAvatar;
    }

    /*
    public Player(EntityType entityType, String name, int x, int y) {
        super(entityType, name, x, y);
     */

    @Override
    public void write(Json json) {
        json.writeValue("id", getId());
        json.writeValue("type", getEntityType());
        json.writeValue("name", getName());
        json.writeValue("x", getX());
        json.writeValue("y", getY());
        json.writeValue("attributes", getPlayerAttributes());
        json.writeValue("inventory", getInventory());
        json.writeValue("equipment", getEquipment());
        json.writeValue("skills", getPlayerSkills());
        json.writeValue("classes", getPlayerClass());
        json.writeValue("bank", getBank());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        setId(Integer.parseInt(jsonData.getString("id")));

        Gdx.app.postRunnable(() -> {
            setImage(new Image(new Texture(Gdx.files.internal("images/player.png"))));
        });

        setName(jsonData.getString("name"));

        List<Attribute> attributes = new ArrayList<>();
        JsonValue attrs = jsonData.get("attributes");

        for (JsonValue v : attrs) {
            attributes.add(json.fromJson(Attribute.class, v.toString()));
        }

        setPlayerAttributes(attributes);

        String invenJson = jsonData.get("inventory").toString();
        invenJson = "{" + invenJson + "}";
        setInventory(json.fromJson(Inventory.class, invenJson));

        String equipJson = jsonData.get("equipment").toString();
        equipJson = "{" + equipJson + "}";
        setEquipment(json.fromJson(Equipment.class, equipJson));

        //remove this
        List<EquipmentSlot> slots = new ArrayList<>();
        setEquipment(new Equipment(slots));

        List<Skill> skills = new ArrayList<>();

        JsonValue skillJ = jsonData.get("skills");
        for (JsonValue v : skillJ) {
            skills.add(json.fromJson(Skill.class, v.toString()));
        }

        if (skills.size() == 0) {
            //Defaults
            playerSkills.add(new Skill("Sword Proficiency", 1, 0, 100, "sword.png"));
            playerSkills.add(new Skill("Axe Proficiency", 1, 0, 100, "axe.png"));
            playerSkills.add(new Skill("Mace Proficiency", 1, 0, 100, "mace.png"));
            playerSkills.add(new Skill("Staff Proficiency", 1, 0, 100, "staff.png"));
            playerSkills.add(new Skill("Bow Proficiency", 1, 0, 100, "bow.png"));
            playerSkills.add(new Skill("Woodcutting", 1, 0, 100, "woodcutting.png"));
            playerSkills.add(new Skill("Fishing", 1, 0, 100, "fishing.png"));
            playerSkills.add(new Skill("Mining", 1, 0, 100, "mining.png"));
            playerSkills.add(new Skill("Smithing", 1, 0, 100, "smithing.png"));
        } else {
            //playerSkills = skills;
            playerSkills.add(new Skill("Sword Proficiency", 1, 0, 100, "sword.png"));
            playerSkills.add(new Skill("Axe Proficiency", 1, 0, 100, "axe.png"));
            playerSkills.add(new Skill("Mace Proficiency", 1, 0, 100, "mace.png"));
            playerSkills.add(new Skill("Staff Proficiency", 1, 0, 100, "staff.png"));
            playerSkills.add(new Skill("Bow Proficiency", 1, 0, 100, "bow.png"));
            playerSkills.add(new Skill("Woodcutting", 1, 0, 100, "woodcutting.png"));
            playerSkills.add(new Skill("Fishing", 1, 0, 100, "fishing.png"));
            playerSkills.add(new Skill("Mining", 1, 0, 100, "mining.png"));
            playerSkills.add(new Skill("Smithing", 1, 0, 100, "smithing.png"));
        }

        Attribute.setAttributeValueFromList(playerAttributes, "Level", 1);
        setPlayerClass(PlayerClass.DRUID); //ADD HUD pics for other classes

        if (Objects.nonNull(playerClass)) {
            //setPlayerClass(playerClass);
            Gdx.app.postRunnable(() -> setImage(new Image(new Texture(Gdx.files.internal("classes/" + playerClass.getImage())))));
        }

        Attribute.setAttributeValueFromList(this.playerAttributes, "CurrentHp", Attribute.getAttributeValueFromList(this.playerAttributes, "MaxHp"));

        if (jsonData.hasChild("bank")) {
            setBank(new ArrayList<>());
            JsonValue bank = jsonData.get("bank");

            for (JsonValue v : bank) {
                if (Objects.nonNull(v)) {

                    Item item = ItemManager.createItemById(v.getInt("id"), v.getInt("amount"));
                    item.setRarity(v.getInt("rarity"));

                    List<Attribute> itemAttrs = new ArrayList<>();
                    JsonValue jAttrs = v.get("itemAttributes");

                    for (JsonValue v1 : jAttrs) {
                        itemAttrs.add(json.fromJson(Attribute.class, v1.toString()));
                    }

                    item.setAttributes(itemAttrs);

                    getBank().add(item);
                }
            }
        } else {
            setBank(new ArrayList<>());
        }

        //TODO: remove this shit
        playerAttributes.clear();
        playerAttributes.add(new Attribute("Stamina", 1));
        playerAttributes.add(new Attribute("Strength", 7));
        playerAttributes.add(new Attribute("Agility", 1));
        playerAttributes.add(new Attribute("Intellect", 1));
        playerAttributes.add(new Attribute("Crit", 1));
        playerAttributes.add(new Attribute("Haste", 1));
        playerAttributes.add(new Attribute("ExpBoost", 1));
        playerAttributes.add(new Attribute("BonusArmor", 1));
        playerAttributes.add(new Attribute("LifeSteal", 1));
        playerAttributes.add(new Attribute("AttackPower", 1));
        playerAttributes.add(new Attribute("SpellPower", 1));
        playerAttributes.add(new Attribute("CastingSpeed", 1));
        playerAttributes.add(new Attribute("ManaRegen", 1));
        playerAttributes.add(new Attribute("HpRegen", 1));
        playerAttributes.add(new Attribute("Armor", 1));
        playerAttributes.add(new Attribute("Dodge", 1));
        playerAttributes.add(new Attribute("Parry", 1));
        playerAttributes.add(new Attribute("Block", 1));
        playerAttributes.add(new Attribute("Resist", 1));
        playerAttributes.add(new Attribute("MovementSpeed", 4));
        playerAttributes.add(new Attribute("CurrentHp", 100));
        playerAttributes.add(new Attribute("MaxHp", 100));
        playerAttributes.add(new Attribute("CurrentExp", 99));
        playerAttributes.add(new Attribute("MaxExp", 100));
        playerAttributes.add(new Attribute("Level", 1));
        playerAttributes.add(new Attribute("CurrentMana", 100));
        playerAttributes.add(new Attribute("MaxMana", 100));
        playerAttributes.add(new Attribute("AttackSpeed", 2.0));
        playerAttributes.add(new Attribute("Slowness", 0));
    }
}
