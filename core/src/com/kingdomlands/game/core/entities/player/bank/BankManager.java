package com.kingdomlands.game.core.entities.player.bank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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

import java.util.Objects;

public class BankManager {
    private static ScrollPane scrollPane;
    private static TextField bankTitle, itemTitle, levelLabel, rarityLabel, costLabel, priceTotal;
    private static TextArea attributesArea;
    private static Slider amountSlider;
    private static TextButton withdrawButton, closeButton;
    private static Table root;
    private static Item clickedItem;
    private static boolean bankOpen = false;

    private static final TextureRegionDrawable scrollPaneDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("widget_bgs/scrollpane_bg.png")), 0, 0, 360, 460));
    private static final TextureRegionDrawable titleDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("widget_bgs/title_bg.png")), 0, 0, 400, 40));

    public static void addItem(Item item, int amount) {
        if (item.isStackable()) {
            if (bankContainsItem(item)) {
                Item bankItem = bankGetItem(item);

                if (Objects.nonNull(bankItem)) {
                    PlayerManager.getCurrentPlayer().getInventory().removeCurrencyAmount(item.getId(), amount);
                    bankItem.setAmount(bankItem.getAmount() + amount);
                }

                refreshBank();
            } else {
                if ((getBankSize() + 1) <= PlayerManager.getMaxBankSlots()) {
                    PlayerManager.getCurrentPlayer().getInventory().removeCurrencyAmount(item.getId(), amount);

                    Item addedItem = ItemManager.createItemById(item.getId(), amount);
                    addedItem.setRarity(item.getRarity());
                    addedItem.setAttributes(item.getAttributes());

                    PlayerManager.getCurrentPlayer().getBank().add(addedItem);

                    refreshBank();
                } else {
                    ChatManager.addChat("[Bank]: " + "You do not have enough bank slots to add this item.");
                }
            }
        } else {
            if ((getBankSize() + item.getAmount()) <= PlayerManager.getMaxBankSlots()) {
                PlayerManager.getCurrentPlayer().getBank().add(item);
                PlayerManager.getCurrentPlayer().getInventory().deleteItem(PlayerManager.getCurrentPlayer().getInventory().getItemIndex(item));

                refreshBank();
            } else {
                ChatManager.addChat("[Bank]: " + "You do not have enough bank slots to add this item.");
            }
        }
    }

    public static void removeItem(int amount) {
        if (Objects.nonNull(clickedItem)) {
            Item item = ItemManager.createItemById(clickedItem.getId(), amount);
            item.setRarity(clickedItem.getRarity());
            item.setAttributes(clickedItem.getAttributes());

            PlayerManager.getCurrentPlayer().getInventory().addItem(item);
            ChatManager.addChat("[Bank]: " + "You have withdrawn " + clickedItem.getName() + " from the bank.");

            if (clickedItem.getAmount() - amount <= 0) {
                PlayerManager.getCurrentPlayer().getBank().remove(clickedItem);
            } else {
                clickedItem.setAmount(clickedItem.getAmount() - amount);
            }

            itemTitle.setText("");
            levelLabel.setText("");
            rarityLabel.setColor(Methods.getRarityColor(1));
            rarityLabel.setText("");
            costLabel.setColor(Color.YELLOW);
            costLabel.setText("");
            priceTotal.setText("");
            withdrawButton.setText("Withdraw");
            attributesArea.setText("");

            refreshBank();
        }
    }

    public static void openBank() {
        ContextManager.close();

        if (isBankOpen()) {
            closeBank();
        }

        bankOpen = true;

        if (!UIManager.isInventoryOpen()) {
            UIManager.setCurrentTab(HUD.INVENTORY);
        }

        //Bank List
        Vector2 pos = new Vector2(PlayerManager.getCurrentPlayer().getX() - 300, PlayerManager.getCurrentPlayer().getY() - 400);

        refreshBank();

        //Title of item
        bankTitle = new TextField("" + PlayerManager.getCurrentPlayer().getName() + "'s bank [" + getBankSize() + "/" + PlayerManager.getMaxBankSlots() +"]", Constants.DEFAULT_SKIN);
        bankTitle.setWidth(400);
        bankTitle.setHeight(32);
        bankTitle.setAlignment(Align.center);
        bankTitle.setDisabled(true);
        bankTitle.setPosition(pos.x, pos.y + 198 + 460);

        bankTitle.getStyle().background = scrollPaneDrawable;

        StageManager.addActor(bankTitle);

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
        costLabel.setColor(Color.YELLOW);
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
                priceTotal.setText((int)amountSlider.getValue() + "x");
                withdrawButton.setText("Withdraw");
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
        withdrawButton = new TextButton("", Constants.DEFAULT_SKIN);
        withdrawButton.setWidth(260);
        withdrawButton.setHeight(60);
        withdrawButton.setPosition(pos.x + 176, pos.y + 200);
        withdrawButton.setColor(Color.CORAL);
        withdrawButton.setText("Withdraw");

        withdrawButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Objects.nonNull(itemTitle) && !itemTitle.getText().isEmpty()) {
                    Inventory playerInventory = PlayerManager.getCurrentPlayer().getInventory();

                    if (playerInventory.getItems().size() < 30 || (!getBankItem().isStackable() &&
                            (playerInventory.getItems().size() + (int)amountSlider.getValue()) <= 30)) {

                            if (Objects.nonNull(clickedItem)) {
                                if (clickedItem.isStackable()) {
                                    if (amountSlider.getValue() >= clickedItem.getAmount()) {
                                        removeItem(clickedItem.getAmount());
                                    } else {
                                     removeItem((int) amountSlider.getValue());
                                    }
                                } else {
                                    removeItem(1);
                                }
                            }
                    } else {
                        ChatManager.addChat("[Bank]: " + "You do not have enough inventory space.");
                    }
                }
            }
        });

        StageManager.addActor(withdrawButton);

        //close item
        closeButton = new TextButton("X", Constants.DEFAULT_SKIN);
        closeButton.setWidth(36);
        closeButton.setHeight(40);
        closeButton.setPosition(pos.x + 400, pos.y + 198 + 460);
        closeButton.setColor(Color.RED);

        closeButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeBank();
            }
        });

        StageManager.addActor(closeButton);
    }

    public static void closeBank() {
        scrollPane.remove();
        bankTitle.remove();
        itemTitle.remove();
        levelLabel.remove();
        rarityLabel.remove();
        costLabel.remove();
        priceTotal.remove();
        amountSlider.remove();
        attributesArea.remove();
        withdrawButton.remove();
        closeButton.remove();
        bankOpen = false;

        if (UIManager.isInventoryOpen()) {
            UIManager.setCurrentTab(HUD.INVENTORY);
        }
        ContextManager.close();
    }

    public static int getBankSize() {
        return Objects.requireNonNull(PlayerManager. getCurrentPlayer()).getBank().size();
    }

    public static boolean bankContainsItem(Item item) {
        if (Objects.nonNull(item) && item.isStackable()) {
            for (Item i : Objects.requireNonNull(PlayerManager.getCurrentPlayer()).getBank()) {
                if (Objects.nonNull(i)) {
                    if (i.getId() == item.getId()) {
                        return true;
                    }
                }
            }
        }

        return Objects.requireNonNull(PlayerManager.getCurrentPlayer()).getBank().contains(item);
    }

    public static Item bankGetItem(Item item) {
        if (Objects.nonNull(item) && item.isStackable()) {
            for (Item i : Objects.requireNonNull(PlayerManager.getCurrentPlayer()).getBank()) {
                if (Objects.nonNull(i)) {
                    if (i.getId() == item.getId()) {
                        return i;
                    }
                }
            }
        }

        return null;
    }

    public static Item getBankItem() {
        return clickedItem;
    }

    public static boolean isBankOpen() {
        return bankOpen;
    }

    public static void refreshBank() {
        Vector2 pos = new Vector2(PlayerManager.getCurrentPlayer().getX() - 300, PlayerManager.getCurrentPlayer().getY() - 400);

        if (Objects.nonNull(bankTitle)) {
            bankTitle.setText(PlayerManager.getCurrentPlayer().getName() + "'s bank [" + getBankSize() + "/" + PlayerManager.getMaxBankSlots() +"]");
        }

        if (Objects.nonNull(scrollPane)) {
            scrollPane.remove();
        }

        root = new Table(Constants.DEFAULT_SKIN);

        for (Item it : PlayerManager.getCurrentPlayer().getBank()) {
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
                            costLabel.setText("" + it.getAmount() + "x");
                            priceTotal.setText((int)amountSlider.getValue() + "x");
                            withdrawButton.setText("Withdraw");
                            amountSlider.setRange(1, it.getAmount());

                            for (Attribute a : it.getAttributes()) {
                                if (Objects.nonNull(a)) {
                                    attributesArea.appendText(a.getName() + ": +" + a.getValue() + "\n");
                                }
                            }

                            clickedItem = it;
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

        StageManager.addActor(scrollPane);
    }
}
