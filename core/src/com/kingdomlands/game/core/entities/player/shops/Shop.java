package com.kingdomlands.game.core.entities.player.shops;

import com.kingdomlands.game.core.entities.item.Item;

import java.util.List;

public class Shop {
    private int id, level, currency;
    private String name;
    private List<Item> items;

    public Shop(int id, int level, int currency, String name, List<Item> items) {
        this.id = id;
        this.level = level;
        this.currency = currency;
        this.name = name;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrency() {
        return currency;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }
}
