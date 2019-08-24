package com.kingdomlands.game.core.entities.player.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.ui.UIManager;
import com.kingdomlands.game.core.entities.util.*;
import com.kingdomlands.game.core.stages.StageManager;

import javax.jws.Oneway;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.kingdomlands.game.core.entities.player.ui.UIManager.openInventoryTab;

/**
 * Created by David K on Mar, 2019
 */
public class Inventory extends Actor implements Json.Serializable {
    private final int MAX_SLOTS = 30;

    private String playerId;
    private Item[] slots;
    private boolean open;
    private int timeout = 0;

    public Inventory(String playerId, List<Item> items) {
        this.playerId = playerId;
        this.slots = new Item[MAX_SLOTS];

        items.forEach(this::addItem);
    }

    public Inventory() {
        this.slots = new Item[MAX_SLOTS];
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (timeout > 0) {
            timeout -= Gdx.graphics.getDeltaTime();
        }
    }

    public boolean addItem(Item item) {
        int i = getFirstFreeSlotIndex();

        if (i != -1) {
            if ((item.getWeight()*item.getAmount()) + getInventoryWeight() < getMaxCarryWeight() * 1.75) {
                StageManager.removeActor(item);

                if (item.isStackable() && inventoryContainsItem(item)) {
                    item.setAmount(item.getAmount() + getItem(getItemIndexId(item.getId())).getAmount());
                    addItemSlot(item, getItemIndexId(item.getId()));
                } else {
                    slots[i] = item;
                }

                if (UIManager.isInventoryOpen()) {
                    UIManager.openInventoryTab();
                }

                return true;
            } else {
                AlertTextManager.add(new AlertText((int)item.getX() + 32, (int)item.getY() + 32, "Item is too heavy.", DamageType.DEFAULT));
                return false;
            }
        } else {
            AlertTextManager.add(new AlertText((int)item.getX() + 32, (int)item.getY() + 32, "Inventory is full.", DamageType.DEFAULT));
            return false;
        }
    }

    public void addItemSlot(Item item, int slot) {
        slots[slot] = item;

        if (UIManager.isInventoryOpen()) {
            UIManager.openInventoryTab();
        }
    }

    public int getFirstFreeSlotIndex() {
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (Objects.isNull(slots[i])) return i;
        }

        return -1;
    }

    public Item getItem(int index) {
        if (Objects.nonNull(slots[index])) {
            return slots[index];
        }
        return null;
    }

    public boolean isFreeSlot(int index) {
        return Objects.isNull(slots[index]);
    }

    public void removeItem(int index) {
        if (Objects.nonNull(slots[index])) {
            slots[index].setX(getPlayer().getX());
            slots[index].setY(getPlayer().getY());

            if (slots[index].isStackable() && slots[index].getAmount() > 1) {
                slots[index].setAmount(slots[index].getAmount() - 1);

                Item dropItem = ItemManager.createItemById(slots[index].getId(), 1);
                dropItem.setRarity(slots[index].getRarity());
                dropItem.setAttributes(slots[index].getAttributes());
                dropItem.setPosition((int)getPlayer().getX(), (int)getPlayer().getY());

                StageManager.addActor(dropItem);
            } else {
                StageManager.addActor(slots[index]);
                slots[index] = null;
            }

            if (UIManager.isInventoryOpen()) {
                UIManager.openInventoryTab();
            }
        }
    }

    public void removeItem(int index, int amt) {
        if (Objects.nonNull(slots[index])) {
            slots[index].setX(getPlayer().getX());
            slots[index].setY(getPlayer().getY());

            if (slots[index].isStackable() && slots[index].getAmount() > 1) {
                slots[index].setAmount(slots[index].getAmount() - amt);

                Item dropItem = ItemManager.createItemById(slots[index].getId(), amt);
                dropItem.setRarity(slots[index].getRarity());
                dropItem.setAttributes(slots[index].getAttributes());
                dropItem.setPosition((int)getPlayer().getX(), (int)getPlayer().getY());

                StageManager.addActor(dropItem);

                if (slots[index].getAmount() == 0) {
                    deleteItem(index);
                }
            } else {
                StageManager.addActor(slots[index]);
                slots[index] = null;
            }

            if (UIManager.isInventoryOpen()) {
                UIManager.openInventoryTab();
            }
        }
    }

    public void deleteItem(int index) {
        if (index == -1) {
            return;
        }

        if (Objects.nonNull(slots[index])) {
            slots[index] = null;
        }

        if (UIManager.isInventoryOpen()) {
            UIManager.openInventoryTab();
        }
    }

    public void deleteItem(Item item) {
        int index = getItemIndex(item);

        if (index == -1) {
            return;
        }

        if (slots[index].isStackable() && slots[index].getAmount() > 1) {
            slots[index].setAmount(slots[index].getAmount() - 1);
        } else {
            slots[index] = null;
        }

        if (UIManager.isInventoryOpen()) {
            UIManager.openInventoryTab();
        }
    }

    public int getItemIndex(Item item) {
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (Objects.nonNull(getItem(i))) {
                if (getItem(i).equals(item)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public int getItemIndexId(int id) {
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (Objects.nonNull(getItem(i))) {
                if (getItem(i).getId() == id) {
                    return i;
                }
            }
        }

        return -1;
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < MAX_SLOTS; i++) {
            if (Objects.nonNull(getItem(i))) {
                items.add(getItem(i));
            }
        }

        return items;
    }

    public void setItems(List<Item> items) {
        Deque<Item> deque = new ArrayDeque<>();
        items.forEach(i -> deque.add(i));

        for (int i = 0; i < MAX_SLOTS; i++) {
            if (!deque.isEmpty()) {
                slots[i] = deque.pop();
            }
        }

        if (UIManager.isInventoryOpen()) {
            UIManager.openInventoryTab();
        }
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    private Player getPlayer() {
        return PlayerManager.getFilteredPlayer(p -> Objects.nonNull(p) && p.getName().equals(getPlayerId()));
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen() {
        if (timeout <= 0) {
            if (open) {
                open = false;
                Constants.ROW = 0;
                getItems().forEach(i -> i.remove());
            } else {
                open = true;
            }

            timeout = 30;
        }
    }

    public Item[] getSlots() {
        return slots;
    }

    public void render() {
        if (timeout > 0) {
            timeout -= Gdx.graphics.getDeltaTime();
        }
    }

    public boolean inventoryContainsItem(Item item) {
        for (Item i : getItems()) {
            if (Objects.nonNull(i)) {
                if (i.getName().equals(item.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

    public double getPercentageAboveMaxWeight() {
            return BigDecimal.valueOf((this.getInventoryWeight() / this.getMaxCarryWeight()) - 1)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public double getInventoryWeight() {
        return getItems().stream().mapToInt(i -> i.getWeight()*i.getAmount()).sum();
    }

    public int getMaxCarryWeight() {
        return ((int)Attribute.getAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), "Strength"))*10 + 100;
    }

    public void removeCurrencyAmount(int currency, int amount) {
        for (Item i : getItems()) {
            if (Objects.nonNull(i)) {
                if (currency == i.getId()) {
                    if (amount <= i.getAmount()) {
                        i.setAmount(i.getAmount() - amount);

                        if (i.getAmount() == 0) {
                            deleteItem(i);
                            return;
                        }
                    } else if (amount == i.getAmount()) {
                        deleteItem(i);
                    }
                }
            }
        }

        if (UIManager.isInventoryOpen()) {
            UIManager.openInventoryTab();
        }
    }

    public void addCurrencyAmount(int currency, int amount) {
        addItem(ItemManager.createItemById(currency, amount));
    }

    @Override
    public void write(Json json) {
        json.writeValue("playerId", getPlayerId());
        json.writeValue("items", getItems());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue inventory = jsonData.get("inventory");
        setPlayerId(inventory.getString("playerId"));
        List<Item> items = new ArrayList<>();

        for (JsonValue v : inventory.get("items")) {
            if (Objects.nonNull(v) && !v.toString().isEmpty()) {
                items.add(json.fromJson(Item.class, v.toString()));
            }
        }

        setItems(items);
    }
}
