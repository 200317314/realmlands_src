package com.kingdomlands.game.core.entities.util.contextmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.item.ItemType;
import com.kingdomlands.game.core.entities.monster.Monster;
import com.kingdomlands.game.core.entities.npc.Npc;
import com.kingdomlands.game.core.entities.npc.NpcManager;
import com.kingdomlands.game.core.entities.npc.NpcType;
import com.kingdomlands.game.core.entities.objects.GameObject;
import com.kingdomlands.game.core.entities.objects.ObjectManager;
import com.kingdomlands.game.core.entities.objects.portal.PortalManager;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.bank.BankManager;
import com.kingdomlands.game.core.entities.player.chat.ChatManager;
import com.kingdomlands.game.core.entities.player.shops.ShopManager;
import com.kingdomlands.game.core.entities.projectile.Projectile;
import com.kingdomlands.game.core.entities.util.*;
import com.kingdomlands.game.core.entities.util.groups.GroupManager;
import com.kingdomlands.game.core.stages.StageManager;
import com.kingdomlands.game.core.stages.StageRender;
import com.kingdomlands.game.core.stages.Stages;

import java.util.Objects;

public class ContextManager {
    private static Stage stage;
    private static SelectBox<String> contextMenu;
    private static Entity clickedEntity;
    private static Table table;
    private static List<String> list;

    public static void init() {
        stage = new Stage();
        open();
    }

    public static SelectBox<String> getContextMenu() {
        return contextMenu;
    }

    public static void setContextMenu(SelectBox<String> contextMenu) {
        ContextManager.contextMenu = contextMenu;
    }

    public static Entity getClickedEntity() {
        return clickedEntity;
    }

    public static void setClickedEntity(Entity clickedEntity) {
        ContextManager.clickedEntity = clickedEntity;
    }

    public static void close() {
        Player.timeout = 0.5;
        ContextManager.getTable().clear();
        //ContextManager.getContextMenu().getList().clear();
        ContextManager.getTable().setVisible(false);
        //ContextManager.getContextMenu().setDisabled(true);
        ContextManager.getTable().remove();
    }

    public static void open() {
        table = new Table(Constants.DEFAULT_SKIN);
        table.setFillParent(false);


        //table.padTop(50);
        table.setWidth(180);
        //table.add(scrollPane);
        table.setVisible(false);

        StageManager.addActor(table);

        contextMenu = new SelectBox(Constants.DEFAULT_SKIN);
        contextMenu.setDisabled(false);
        contextMenu.setVisible(false);
        contextMenu.setWidth(120);
        contextMenu.setPosition(100,300);

        //StageManager.addActor(contextMenu);

        table.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = "";

                for (Cell c : ContextManager.getTable().getCells()) {
                    if (c.getActor() instanceof ScrollPane) {
                        ScrollPane pane = (ScrollPane) c.getActor();
                        List<String> list = (List<String>) pane.getActor();
                        selected = list.getSelected();
                    }
                }

                Entity selectedEntity = ContextManager.getClickedEntity();

                if (selected.equals("Examine")) {
                    if (selectedEntity instanceof Item) {
                        ChatManager.addChat("[Battle]: " + ((Item) selectedEntity).getDescription() + ".");
                    } else if (selectedEntity instanceof Monster) {
                        ChatManager.addChat("[Battle]: " + selectedEntity.getName() + " is a " + Methods.getRarityName(((Monster) selectedEntity).getRarity()) + "level " + (int)((Monster) selectedEntity).getMonsterAttributes().getLevel() + ".");
                    } else if (selectedEntity instanceof Player) {
                        ChatManager.addChat("[Battle]: " + "You are a level " + (int)Attribute.getAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), "Level") + ".");
                    } else if (selectedEntity instanceof Npc) {
                        Npc npc = (Npc) selectedEntity;

                        if (npc.getNpcType() == NpcType.TRADER) {
                            ChatManager.addChat("[Battle]: " + npc.getName() +".");
                        } else if (npc.getNpcType() == NpcType.BANKER) {
                            ChatManager.addChat("[Battle]: " + "One of the many bankers across Realm Lands" +".");
                        }
                    }

                    ContextManager.close();
                } else if (selected.equals("Attack")) {
                    if (selectedEntity instanceof Monster) {
                        PlayerManager.getCurrentPlayer().setTarget(selectedEntity);
                    }

                    ContextManager.close();
                } else if (selected.equals("Take")) {
                    if (selectedEntity instanceof Item) {
                        PlayerManager.getCurrentPlayer().setTarget(null);
                        PlayerManager.getCurrentPlayer().setTarget(selectedEntity);
                    }

                    ContextManager.close();
                } else if (selected.contains("Drop")) {
                    if (selectedEntity instanceof Item) {
                        int amount = 1;

                        if (selected.contains("Drop All")) {
                            amount = ((Item) selectedEntity).getAmount();
                        } else {
                            String[] splitted = selected.split("Drop ");
                            if (splitted.length > 1) {
                                amount = Integer.parseInt(splitted[1]);
                            }
                        }

                        PlayerManager.getCurrentPlayer().getInventory().removeItem(PlayerManager.getCurrentPlayer().getInventory().getItemIndex((Item) selectedEntity), amount);
                    }

                    ContextManager.close();
                } else if (selected.equals("Wear")) {
                    if (selectedEntity instanceof Item) {
                        Item item = (Item) selectedEntity;

                        if (item.getItemType().equals(ItemType.CONSUMABLE)) {
                            if (Attribute.getAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), "Level") >= item.getLevel()) {
                                item.consume();
                            } else {
                                AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX(), (int)PlayerManager.getCurrentPlayer().getY(), "Level is too low...", DamageType.DEFAULT));
                            }
                        } else {
                            if (Attribute.getAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), "Level") >= item.getLevel()) {
                                PlayerManager.getCurrentPlayer().getEquipment().addItem(item);
                            } else {
                                AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX(), (int)PlayerManager.getCurrentPlayer().getY(), "Level is too low...", DamageType.DEFAULT));
                            }
                        }
                    }

                    ContextManager.close();
                } else if (selected.equals("Open Shop")) {
                    if (selectedEntity instanceof Npc) {
                        Npc npc = (Npc) selectedEntity;

                        if (npc.getDistance(PlayerManager.getCurrentPlayer()) <= 160) {
                            ShopManager.openShop(npc.getShopId());
                        } else {
                            AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX(), (int)PlayerManager.getCurrentPlayer().getY(), "You need to be closer to do that...", DamageType.DEFAULT));
                        }
                    }
                } else if (selected.equals("Open Bank")) {
                    if (selectedEntity instanceof Npc) {
                        Npc npc = (Npc) selectedEntity;

                        if (npc.getDistance(PlayerManager.getCurrentPlayer()) <= 160) {
                            BankManager.openBank();
                        } else {
                            AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX(), (int)PlayerManager.getCurrentPlayer().getY(), "You need to be closer to do that...", DamageType.DEFAULT));
                        }
                    }
                } else if (selected.contains("Sell ")) {
                    Item item = (Item) selectedEntity;
                    int amount;

                    if (selected.contains("All")) {
                        amount = item.getAmount();
                    } else {
                        amount = Integer.parseInt(selected.split("Sell ")[1]);
                    }

                    PlayerManager.getCurrentPlayer().getInventory().addCurrencyAmount(75, (ItemManager.getCostOfItem(item)/4) * amount);
                    PlayerManager.getCurrentPlayer().getInventory().removeCurrencyAmount(item.getId(), amount);

                    ChatManager.addChat("[Shop]: " + " You have sold x" + amount + " of " + item.getName() + " for " + (ItemManager.getCostOfItem(item)/4) * amount + " Coins.");

                    close();
                } else if (selected.contains("Deposit ")) {
                    Item item = (Item) selectedEntity;
                    int amount;

                    if (selected.contains("All")) {
                        amount = item.getAmount();
                    } else {
                        amount = Integer.parseInt(selected.split("Deposit ")[1]);
                    }

                    BankManager.addItem(item, amount);

                    ChatManager.addChat("[Bank]: " + " You have deposited " + item.getName() + " into the bank.");
                    close();
                } else if (selected.contains("Open Portal")) {
                    if (selectedEntity.getDistance(PlayerManager.getCurrentPlayer()) <= 160) {
                        PortalManager.openPortal();
                    } else {
                        AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX(), (int)PlayerManager.getCurrentPlayer().getY(), "You need to be closer to do that...", DamageType.DEFAULT));
                    }

                    close();
                } else if (selected.contains("Return Town")) {
                    if (selectedEntity.getDistance(PlayerManager.getCurrentPlayer()) <= 160) {
                        Player player = PlayerManager.getCurrentPlayer();

                        StageManager.getAllEntities().forEach(e -> {
                            if (e instanceof GameObject || e instanceof Npc || e instanceof Monster || e instanceof Item || e instanceof Projectile) {
                                e.remove();
                            }
                        });

                        StageRender.loadTiledMap(Stages.TOWN.getTiledMap());

                        Gdx.input.setInputProcessor(StageManager.getCurrentStage());
                        StageManager.addActor(player);
                        Objects.requireNonNull(player).setPosition(7265, 7800);

                        //Add Buildings
                        StageManager.loadTown();

                        ChatManager.init();
                    } else {
                        AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX(), (int)PlayerManager.getCurrentPlayer().getY(), "You need to be closer to do that...", DamageType.DEFAULT));
                    }

                    close();
                } else if (selectedEntity instanceof GameObject && Objects.nonNull(((GameObject) selectedEntity).getResource())) {
                    if (selectedEntity.getDistance(PlayerManager.getCurrentPlayer()) <= 160) {
                        if (Objects.nonNull(((GameObject) selectedEntity).getResource())) {
                            PlayerManager.getCurrentPlayer().setTarget(selectedEntity);
                        }
                    } else {
                        AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX(), (int)PlayerManager.getCurrentPlayer().getY(), "You need to be closer to do that...", DamageType.DEFAULT));
                    }

                    close();
                }
            }
        });
    }

    public static Table getTable() {
        return table;
    }

    public static void setTable(Table table) {
        ContextManager.table = table;
    }
}
