package com.kingdomlands.game.core.entities.player.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.objects.ObjectManager;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.equipment.EquipmentSlot;
import com.kingdomlands.game.core.entities.util.Attribute;
import com.kingdomlands.game.core.entities.util.ItemHoverManager;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.entities.util.Skill;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.entities.util.groups.Group;
import com.kingdomlands.game.core.stages.StageManager;
import com.kingdomlands.game.core.stages.StageRender;
import com.kingdomlands.game.core.stages.Stages;
import com.kingdomlands.game.core.stages.login.LoginStage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UIManager {
    private static HUD currentTab = null;
    private static List<UI> icons = new ArrayList<>();
    private static int timeout = 0;

    private static ScrollPane skillScrollPane, inventoryScrollPane, attributeScrollPane, equipmentScrollPane, settingsScrollPane;

    public static void init() {
        icons.add(UI.INVENTORY_ICON_PLACE);
        icons.add(UI.ATTRIBUTE_ICON_PLACE);
        icons.add(UI.EQUIPMENT_ICON_PLACE);
        icons.add(UI.SKILL_ICON_PLACE);
        icons.add(UI.ABILITY_ICON_PLACE);
        icons.add(UI.FAMILIAR_ICON_PLACE);
        icons.add(UI.SETTINGS_ICON_PLACE);
    }

    public static void updatePosition() {
        timeout -= Gdx.graphics.getDeltaTime();
        Vector2 pos = new Vector2(PlayerManager.getCurrentPlayer().getX() - 300, PlayerManager.getCurrentPlayer().getY() - 400);

        if (Objects.nonNull(currentTab)) {
            //Render Inventory HUD
            if (currentTab.equals(HUD.INVENTORY)) {
                if (Objects.nonNull(inventoryScrollPane)) {
                    inventoryScrollPane.setPosition(pos.x + 596, pos.y + 80);
                }
            }

            //Render Skill HUD
            if (currentTab.equals(HUD.SKILL)) {
                if (Objects.nonNull(skillScrollPane)) {
                    skillScrollPane.setPosition(pos.x + 596, pos.y + 80);
                }
            }

            //Render Attribute HUD
            if (currentTab.equals(HUD.ATTRIBUTE)) {
                if (Objects.nonNull(attributeScrollPane)) {
                    attributeScrollPane.setPosition(pos.x + 596, pos.y + 80);
                }
            }

            //Render Equipment HUD
            if (currentTab.equals(HUD.EQUIPMENT)) {
                if (Objects.nonNull(equipmentScrollPane)) {
                    equipmentScrollPane.setPosition(pos.x + 596, pos.y + 80);
                }
            }

            //Render Setting HUD
            if (currentTab.equals(HUD.SETTINGS)) {
                if (Objects.nonNull(settingsScrollPane)) {
                    settingsScrollPane.setPosition(pos.x + 596, pos.y + 80);
                }
            }
        }
    }

    public static void setCurrentTab(HUD tab) {
        if (timeout <= 0) {
            if (Objects.nonNull(currentTab)) {
                //End HUD
                if (currentTab.equals(HUD.INVENTORY)) {
                    inventoryScrollPane.addAction(Actions.removeActor());
                }

                if (currentTab.equals(HUD.SKILL)) {
                    skillScrollPane.addAction(Actions.removeActor());
                }

                if (currentTab.equals(HUD.ATTRIBUTE)) {
                    attributeScrollPane.addAction(Actions.removeActor());
                }

                if (currentTab.equals(HUD.EQUIPMENT)) {
                    equipmentScrollPane.addAction(Actions.removeActor());
                }

                if (currentTab.equals(HUD.SETTINGS)) {
                    settingsScrollPane.addAction(Actions.removeActor());
                }

                if (currentTab.equals(tab)) {
                    currentTab = null;
                } else {
                    if (Objects.nonNull(tab)) {
                        currentTab = tab;

                        if (tab.equals(HUD.INVENTORY)) {
                            openInventoryTab();
                        }

                        if (tab.equals(HUD.SKILL)) {
                            openSkillTab();
                        }

                        if (tab.equals(HUD.ATTRIBUTE)) {
                            openAttributeTab();
                        }

                        if (tab.equals(HUD.EQUIPMENT)) {
                            openEquipmentTab();
                        }

                        if (tab.equals(HUD.SETTINGS)) {
                            openSettingsTab();
                        }
                    }
                }
            } else {
                currentTab = tab;

                if (Objects.nonNull(tab)) {
                    if (tab.equals(HUD.INVENTORY)) {
                        openInventoryTab();
                    }

                    if (tab.equals(HUD.SKILL)) {
                        openSkillTab();
                    }

                    if (tab.equals(HUD.ATTRIBUTE)) {
                        openAttributeTab();
                    }

                    if (tab.equals(HUD.EQUIPMENT)) {
                        openEquipmentTab();
                    }

                    if (tab.equals(HUD.SETTINGS)) {
                        openSettingsTab();
                    }
                }
            }
        }
        timeout = 30;
    }

    public static void openInventoryTab() {
        if (Objects.nonNull(inventoryScrollPane)) {
            inventoryScrollPane.addAction(Actions.removeActor());
        }

        Table skillTable = new Table(Constants.DEFAULT_SKIN);
        skillTable.align(Align.left);

        for (Item item : Objects.requireNonNull(PlayerManager.getCurrentPlayer()).getInventory().getItems()) {
            if (Objects.nonNull(item)) {
                Label label = new Label("" + item.getName() + " x" + Methods.convertNum(item.getAmount()), Constants.DEFAULT_SKIN);
                label.setColor(Methods.getRarityColor(item.getRarity()));

                skillTable.add(item.getImage());
                skillTable.add(label);
                skillTable.row();

                removeAllListeners(item.getImage());

                item.getImage().addListener(new ClickListener() {
                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        item.setHovered(true);
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        item.setHovered(false);
                    }
                });

                item.getImage().addListener(new ClickListener(Input.Buttons.RIGHT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (!ContextManager.getTable().isVisible()) {
                            ContextManager.open();

                            ContextManager.getTable().setVisible(true);

                            Vector2 stageToScreen = StageManager.getCurrentStage().screenToStageCoordinates(new Vector2(698, 654));

                            ContextManager.getTable().setPosition(stageToScreen.x, stageToScreen.y);
                            ContextManager.getTable().setWidth(400);
                            ContextManager.getTable().setHeight(400);

                            item.contextMenu();
                        } else {
                            ContextManager.close();
                        }
                    }
                });
            }
        }

        inventoryScrollPane = new ScrollPane(skillTable, Constants.DEFAULT_SKIN);
        inventoryScrollPane.setHeight(400);
        inventoryScrollPane.setWidth(344);
        inventoryScrollPane.setFadeScrollBars(false);
        inventoryScrollPane.setScrollbarsVisible(true);

        StageManager.addActor(inventoryScrollPane);
    }

    public static void openEquipmentTab() {
        if (Objects.nonNull(equipmentScrollPane)) {
            equipmentScrollPane.addAction(Actions.removeActor());
        }

        Table skillTable = new Table(Constants.DEFAULT_SKIN);
        skillTable.align(Align.left);

        for (EquipmentSlot equipmentSlot : Objects.requireNonNull(PlayerManager.getCurrentPlayer()).getEquipment().getEquipmentSlots()) {
            if (Objects.nonNull(equipmentSlot)) {
                if (Objects.nonNull(equipmentSlot.getItem())) {
                    Label label;

                    if (equipmentSlot.getItem().isStackable()) {
                        label = new Label("" + equipmentSlot.getItem().getName() + " x" + equipmentSlot.getItem().getAmount(), Constants.DEFAULT_SKIN);
                    } else {
                        label = new Label("" + equipmentSlot.getItem().getName(), Constants.DEFAULT_SKIN);
                    }

                    label.setColor(Methods.getRarityColor(equipmentSlot.getItem().getRarity()));

                    Stack stack = new Stack();
                    stack.add(new Image(new Texture(Gdx.files.internal("slots/" + equipmentSlot.getSlot().toLowerCase() + ".png"))));
                    stack.add(equipmentSlot.getItem().getImage());

                    skillTable.add(stack);
                    skillTable.add(label);
                    skillTable.row();

                    equipmentSlot.getItem().getImage().addListener(new ClickListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            if (Objects.nonNull(equipmentSlot.getItem())) {
                                equipmentSlot.getItem().setHovered(true);
                            }
                        }

                        @Override
                        public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            if (Objects.nonNull(equipmentSlot.getItem())) {
                                equipmentSlot.getItem().setHovered(false);
                            }
                        }
                    });

                    equipmentSlot.getItem().getImage().addListener(new ClickListener(Input.Buttons.RIGHT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            PlayerManager.getCurrentPlayer().getEquipment().removeItem(equipmentSlot.getItem());
                        }
                    });
                } else {
                    Label label = new Label("Nothing Equipped", Constants.DEFAULT_SKIN);
                    label.setColor(Color.RED);

                    skillTable.add(new Image(new Texture(Gdx.files.internal("slots/" + equipmentSlot.getSlot().toLowerCase() + ".png"))));
                    skillTable.add(label);
                    skillTable.row();
                }
            }
        }

        equipmentScrollPane = new ScrollPane(skillTable, Constants.DEFAULT_SKIN);
        equipmentScrollPane.setHeight(400);
        equipmentScrollPane.setWidth(344);
        equipmentScrollPane.setFadeScrollBars(false);
        equipmentScrollPane.setScrollbarsVisible(true);

        StageManager.addActor(equipmentScrollPane);
    }

    public static void openAttributeTab() {
        Table skillTable = new Table(Constants.DEFAULT_SKIN);

        for (Attribute attribute : PlayerManager.getCurrentPlayer().getPlayerAttributes()) {
            if (Objects.nonNull(attribute)) {
                Label label = new Label("" + attribute.getName() + ": " + Methods.convertNum((int)attribute.getValue()), Constants.DEFAULT_SKIN);

                skillTable.add(attribute.getIcon());
                skillTable.add(label);
                skillTable.row();
            }
        }

        attributeScrollPane = new ScrollPane(skillTable, Constants.DEFAULT_SKIN);
        attributeScrollPane.setHeight(400);
        attributeScrollPane.setWidth(344);
        attributeScrollPane.setFadeScrollBars(false);
        attributeScrollPane.setScrollbarsVisible(true);

        StageManager.addActor(attributeScrollPane);
    }

    public static void openSkillTab() {
        Table skillTable = new Table(Constants.DEFAULT_SKIN);

        for (Skill skill : Objects.requireNonNull(PlayerManager.getCurrentPlayer()).getPlayerSkills()) {
            if (Objects.nonNull(skill)) {
                Label label = new Label("" + skill.getName() + " Level: " + skill.getLevel() + " (" + Methods.convertNum((int)skill.getCurrentExp()) + "/" + Methods.convertNum((int)skill.getMaxExp()) + " xp)", Constants.DEFAULT_SKIN);
                label.setColor(Color.GREEN);

                skillTable.add(skill.getImage());
                skillTable.add(label);
                skillTable.row();
            }
        }

        skillScrollPane = new ScrollPane(skillTable, Constants.DEFAULT_SKIN);
        skillScrollPane.setHeight(400);
        skillScrollPane.setWidth(344);
        skillScrollPane.setFadeScrollBars(false);
        skillScrollPane.setScrollbarsVisible(true);

        StageManager.addActor(skillScrollPane);
    }

    public static void openSettingsTab() {
        Table skillTable = new Table(Constants.DEFAULT_SKIN);

        TextButton logout = new TextButton("Logout", Constants.DEFAULT_SKIN);
        logout.setColor(Color.RED);

        skillTable.add(logout);
        skillTable.row();

        logout.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayerManager.savePlayer();
                Gdx.app.exit();
            }
        });

        settingsScrollPane = new ScrollPane(skillTable, Constants.DEFAULT_SKIN);
        settingsScrollPane.setHeight(400);
        settingsScrollPane.setWidth(344);
        settingsScrollPane.setFadeScrollBars(false);
        settingsScrollPane.setScrollbarsVisible(true);

        StageManager.addActor(settingsScrollPane);
    }

    public static List<UI> getIcons() {
        return icons;
    }

    private static boolean isMouseOverItem(Item item) {
        if (Objects.nonNull(item)) {
            if (item.getBounds().contains(new Vector2(Gdx.input.getX(),800 - Gdx.input.getY()))) {
                return true;
            }
        }

        return false;
    }

    public static HUD getCurrentTab() {
        return currentTab;
    }

    public static boolean isInventoryOpen() {
        return Objects.nonNull(currentTab) && currentTab.equals(HUD.INVENTORY);
    }

    public static Rectangle getBounds() {
        if (Objects.nonNull(currentTab)) {
            if (currentTab.equals(HUD.SKILL) && Objects.nonNull(skillScrollPane)) {
                Vector2 pos = new Vector2(skillScrollPane.getX(), skillScrollPane.getY());

                return new Rectangle(pos.x, pos.y, skillScrollPane.getWidth(), skillScrollPane.getHeight());
            }

            if (currentTab.equals(HUD.INVENTORY) && Objects.nonNull(inventoryScrollPane)) {
                Vector2 pos = new Vector2(inventoryScrollPane.getX(), inventoryScrollPane.getY());

                return new Rectangle(pos.x, pos.y, inventoryScrollPane.getWidth(), inventoryScrollPane.getHeight());
            }

            if (currentTab.equals(HUD.ATTRIBUTE) && Objects.nonNull(attributeScrollPane)) {
                Vector2 pos = new Vector2(attributeScrollPane.getX(), attributeScrollPane.getY());

                return new Rectangle(pos.x, pos.y, attributeScrollPane.getWidth(), attributeScrollPane.getHeight());
            }

            if (currentTab.equals(HUD.EQUIPMENT) && Objects.nonNull(equipmentScrollPane)) {
                Vector2 pos = new Vector2(equipmentScrollPane.getX(), equipmentScrollPane.getY());

                return new Rectangle(pos.x, pos.y, equipmentScrollPane.getWidth(), equipmentScrollPane.getHeight());
            }

            if (currentTab.equals(HUD.SETTINGS) && Objects.nonNull(settingsScrollPane)) {
                Vector2 pos = new Vector2(settingsScrollPane.getX(), settingsScrollPane.getY());

                return new Rectangle(pos.x, pos.y, settingsScrollPane.getWidth(), settingsScrollPane.getHeight());
            }
        }

        return null;
    }

    public static boolean isMouseOverTab() {
        Rectangle bounds = getBounds();

        if (Objects.nonNull(bounds)) {
            return bounds.contains(StageManager.getCurrentStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY())));
        }

        return false;
    }

    public static void removeAllListeners(Actor actor) {
        Array<EventListener> listeners = new Array<>(actor.getListeners());
        for (EventListener listener : listeners) {
            actor.removeListener(listener);
        }
    }
}
