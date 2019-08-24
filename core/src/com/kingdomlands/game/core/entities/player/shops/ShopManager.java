package com.kingdomlands.game.core.entities.player.shops;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.chat.ChatManager;
import com.kingdomlands.game.core.entities.player.inventory.Inventory;
import com.kingdomlands.game.core.entities.player.ui.HUD;
import com.kingdomlands.game.core.entities.player.ui.UI;
import com.kingdomlands.game.core.entities.player.ui.UIManager;
import com.kingdomlands.game.core.entities.util.Attribute;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.stages.StageManager;

import java.util.ArrayList;
import java.util.Objects;

public class ShopManager {
    private static ShapeRenderer shapeRenderer = new ShapeRenderer();
    private static int currentShopOpen = -1;
    private static ScrollPane scrollPane;
    private static TextField shopLabel, itemTitle, levelLabel, rarityLabel, costLabel, priceTotal;
    private static TextArea attributesArea;
    private static Slider amountSlider;
    private static TextButton buyButton, closeButton;
    private static Table root;
    private static List<String> shopList;

    private static final TextureRegionDrawable scrollPaneDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("widget_bgs/scrollpane_bg.png")), 0, 0, 360, 460));
    private static final TextureRegionDrawable titleDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("widget_bgs/title_bg.png")), 0, 0, 400, 40));

    public static void openShop(int id) {
        ContextManager.close();

        if (!UIManager.isInventoryOpen()) {
            UIManager.setCurrentTab(HUD.INVENTORY);
        }

        if (isShopOpen()) {
            closeShop();
        }

        Shop shop = getShop(id);

        if (Objects.nonNull(shop)) {
            currentShopOpen = id;
            Vector2 pos = new Vector2(PlayerManager.getCurrentPlayer().getX() - 300, PlayerManager.getCurrentPlayer().getY() - 400);

            root = new Table(Constants.DEFAULT_SKIN);

            for (Item it : shop.getItems()) {
                if (Objects.nonNull(it)) {
                    Label label = new Label("" + it.getName(), Constants.DEFAULT_SKIN);
                    label.setColor(Methods.getRarityColor(it.getRarity()));

                    root.add(it.getImage());
                    root.add(label);
                    root.row();

                    label.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            attributesArea.clear();
                            attributesArea.setText("");

                            if (Objects.nonNull(it)) {
                                itemTitle.setText(String.valueOf(label.getText()));
                                levelLabel.setText("Level: " + it.getLevel());
                                rarityLabel.setColor(Methods.getRarityColor(it.getRarity()));
                                rarityLabel.setText("" + Methods.getRarityName(it.getRarity()));
                                costLabel.setColor(Color.YELLOW);
                                costLabel.setText("" + ItemManager.getCostOfItem(it) + " " + ItemManager.getItemId(shop.getCurrency()).getName());
                                priceTotal.setText((int)(amountSlider.getValue() * ItemManager.getCostOfItem(getItemFromShop(shop, itemTitle.getText()))) + "x " + ItemManager.getItemId(shop.getCurrency()).getName());
                                buyButton.setText("Buy x" + (int)amountSlider.getValue());

                                for (Attribute a : it.getAttributes()) {
                                    if (Objects.nonNull(a)) {
                                        attributesArea.appendText(a.getName() + ": +" + a.getValue() + "\n");
                                    }
                                }
                            }
                        }
                    });
                }
            }

            scrollPane = new ScrollPane(root, Constants.DEFAULT_SKIN);
            scrollPane.setPosition(pos.x - 184, pos.y + 198);
            scrollPane.setHeight(460);
            scrollPane.setWidth(360);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setScrollbarsVisible(true);
            scrollPane.getStyle().background = titleDrawable;

            StageManager.addActor(scrollPane);

            //Title of Shop
            shopLabel = new TextField(shop.getName(), Constants.DEFAULT_SKIN);
            shopLabel.setWidth(400);
            shopLabel.setHeight(40);
            shopLabel.setAlignment(Align.center);
            shopLabel.setDisabled(true);
            shopLabel.setPosition(pos.x, pos.y + 198 + 460);

            StageManager.addActor(shopLabel);

            //Title of item
            itemTitle = new TextField("", Constants.DEFAULT_SKIN);
            itemTitle.setWidth(260);
            itemTitle.setHeight(32);
            itemTitle.setAlignment(Align.center);
            itemTitle.setDisabled(true);
            itemTitle.setPosition(pos.x + 176, pos.y + 198 + 428);

            StageManager.addActor(itemTitle);

            //Level of item
            levelLabel = new TextField("", Constants.DEFAULT_SKIN);
            levelLabel.setWidth(260/2);
            levelLabel.setHeight(32);
            levelLabel.setAlignment(Align.center);
            levelLabel.setDisabled(true);
            levelLabel.setPosition(pos.x + 176, pos.y + 198 + 396);

            StageManager.addActor(levelLabel);

            //Level of item
            rarityLabel = new TextField("", Constants.DEFAULT_SKIN);
            rarityLabel.setWidth(260/2);
            rarityLabel.setHeight(32);
            rarityLabel.setAlignment(Align.center);
            rarityLabel.setDisabled(true);
            rarityLabel.setPosition(pos.x + 176 + 130, pos.y + 198 + 396);

            StageManager.addActor(rarityLabel);

            //Cost of item
            costLabel = new TextField("", Constants.DEFAULT_SKIN);
            costLabel.setWidth(260);
            costLabel.setHeight(32);
            costLabel.setAlignment(Align.center);
            costLabel.setDisabled(true);
            costLabel.setPosition(pos.x + 176, pos.y + 198 + 364);

            StageManager.addActor(costLabel);

            //Attributes of item
            attributesArea = new TextArea("", Constants.DEFAULT_SKIN);
            attributesArea.setWidth(260);
            attributesArea.setHeight(240);
            attributesArea.setAlignment(Align.center);
            attributesArea.setDisabled(true);
            attributesArea.setPosition(pos.x + 176, pos.y + 198 + 126);

            StageManager.addActor(attributesArea);

            //amout of item
            amountSlider = new Slider(1, 100, 1, false, Constants.DEFAULT_SKIN);
            amountSlider.setWidth(260);
            amountSlider.setLayoutEnabled(true);
            amountSlider.setHeight(40);
            amountSlider.setPosition(pos.x + 176, pos.y + 296);
            amountSlider.setValue(1);

            amountSlider.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    priceTotal.setText((int)(amountSlider.getValue() * ItemManager.getCostOfItem(getItemFromShop(shop, itemTitle.getText()))) + "x " + ItemManager.getItemId(shop.getCurrency()).getName());
                    buyButton.setText("Buy x" + (int)amountSlider.getValue());
                }
            });

            StageManager.addActor(amountSlider);

            //price of item
            priceTotal = new TextField("", Constants.DEFAULT_SKIN);
            priceTotal.setWidth(260);
            priceTotal.setLayoutEnabled(true);
            priceTotal.setHeight(40);
            priceTotal.setDisabled(true);
            priceTotal.setPosition(pos.x + 176, pos.y + 264);

            StageManager.addActor(priceTotal);

            //Buy item
            buyButton = new TextButton("", Constants.DEFAULT_SKIN);
            buyButton.setWidth(260);
            buyButton.setHeight(60);
            buyButton.setPosition(pos.x + 176, pos.y + 200);
            buyButton.setColor(Color.ROYAL);

            buyButton.addListener( new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (Objects.nonNull(itemTitle) && !itemTitle.getText().isEmpty()) {
                        Inventory playerInventory = PlayerManager.getCurrentPlayer().getInventory();
                        int total = (int) (amountSlider.getValue() * ItemManager.getCostOfItem(getItemFromShop(shop, itemTitle.getText())));

                        if (playerInventory.getItems().size() < 30 || (!getItemFromShop(shop, itemTitle.getText()).isStackable() &&
                                (playerInventory.getItems().size() + (int)amountSlider.getValue()) <= 30)) {
                            if (inventoryContainsCoins(playerInventory)) {
                                if (inventoryCountCoins(playerInventory) >= total) {
                                    playerInventory.removeCurrencyAmount(shop.getCurrency(), total);
                                    ChatManager.addChat("[Shop]: " + "You have purchased x" + (int)amountSlider.getValue() + " " + itemTitle.getText() + "'s for " + total + " " + ItemManager.getItemId(shop.getCurrency()).getName() + ".");

                                    for (int i = 0; i < (int)amountSlider.getValue(); i++) {
                                        Item item = ItemManager.createItemById(getItemFromShop(shop, itemTitle.getText()).getId(), 1);
                                        item.setAttributes(getItemFromShop(shop, itemTitle.getText()).getAttributes());
                                        item.setRarity(getItemFromShop(shop, itemTitle.getText()).getRarity());
                                        playerInventory.addItem(item);
                                    }
                                } else {
                                    ChatManager.addChat("[Shop]: " + "You do not have enough coins.");
                                }
                            } else {
                                ChatManager.addChat("[Shop]: " + "You do not have any coins");
                            }
                        } else {
                            ChatManager.addChat("[Shop]: " + "You do not have enough inventory space.");
                        }
                    }
                }
            });

            StageManager.addActor(buyButton);

            //close item
            closeButton = new TextButton("X", Constants.DEFAULT_SKIN);
            closeButton.setWidth(36);
            closeButton.setHeight(40);
            closeButton.setPosition(pos.x + 400, pos.y + 198 + 460);
            closeButton.setColor(Color.RED);

            closeButton.addListener( new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    closeShop();
                }
            });

            StageManager.addActor(closeButton);
        }
    }


    public static Shop getShop(int id) {
        ArrayList<Shop> shops = new ArrayList<>();
        JsonReader json = new JsonReader();
        Json jsonR = new Json();
        JsonValue base = json.parse(Gdx.files.internal("shops.json"));
        JsonValue monsterArray = base.get("shops");

        monsterArray.forEach(c -> {
            if (Objects.nonNull(c)) {
                if (c.getInt("id") == id) {
                    ArrayList<Item> items = new ArrayList<>();
                    JsonValue dropArray = c.get("items");

                    for (JsonValue v : dropArray) {
                        if (Objects.nonNull(v)) {
                            items.add(ItemManager.createItemById(v.getInt("id"), 1));
                        }
                    }

                    shops.add(new Shop(c.getInt("id"),
                            c.getInt("level"),
                            c.getInt("currency"),
                            c.getString("name"),
                            items));
                }
            }
        });

        if (shops.size() == 0) {
            return null;
        }

        return shops.get(0);
    }

    public static boolean isShopOpen() {
        return currentShopOpen != -1;
    }

    public static void closeShop() {
        currentShopOpen = -1;
        scrollPane.remove();
        shopLabel.remove();
        itemTitle.remove();
        levelLabel.remove();
        rarityLabel.remove();
        costLabel.remove();
        priceTotal.remove();
        amountSlider.remove();
        attributesArea.remove();
        buyButton.remove();
        closeButton.remove();
        shopList = null;

        if (UIManager.isInventoryOpen()) {
            UIManager.setCurrentTab(null);
        }
        ContextManager.close();
    }

    private static Item getItemFromShop(Shop shop, String item) {
        for (Item i : shop.getItems()) {
            if (Objects.nonNull(i)) {
                if (i.getName().equals(item)) {
                    return i;
                }
            }
        }

        return null;
    }

    private static boolean inventoryContainsCoins(Inventory inventory) {
        for (Item i : inventory.getItems()) {
            if (Objects.nonNull(i)) {
                if (i.getId() == getShop(currentShopOpen).getCurrency()) {
                    return true;
                }
            }
        }

        return false;
    }

    private static int inventoryCountCoins(Inventory inventory) {
        for (Item i : inventory.getItems()) {
            if (Objects.nonNull(i)) {
                if (i.getId() == getShop(currentShopOpen).getCurrency()) {
                    return i.getAmount();
                }
            }
        }

        return 0;
    }
}
