package com.kingdomlands.game.core.entities.player.equipment;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.SlotType;

import java.util.ArrayList;
import java.util.Objects;

public class EquipmentSlot implements Json.Serializable {
    private String slot;
    private int x, y;
    private SlotType slotType;
    private Item item;

    public EquipmentSlot(String slot, int x, int y, SlotType slotType, Item item) {
        this.slot = slot;
        this.x = x;
        this.y = y;
        this.slotType = slotType;
        this.item = item;
    }

    public EquipmentSlot() {

    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }


    @Override
    public void write(Json json) {
        //if (Objects.nonNull(getItem())) {
            json.writeValue("slot", getSlot());
            json.writeValue("x", getX());
            json.writeValue("y", getY());
            json.writeValue("slotType", getSlotType());
            json.writeValue("item", getItem());
            //json.writeValue("itemId", getItem().getId());
            //json.writeValue("itemAttributes", getItem().getAttributes());
        //}
    }

    @Override
    public void read(Json json, JsonValue jsonData) { //new EquipmentSlot("Helmet", 1096, 408, SlotType.HELMET, null)
        if (jsonData.has("slot")) {
            setSlot(jsonData.getString("slot"));
            setX(jsonData.getInt("x"));
            setY(jsonData.getInt("y"));
            setSlotType(SlotType.valueOf(jsonData.getString("slotType")));
            setItem(json.fromJson(Item.class, jsonData.get("item").prettyPrint(JsonWriter.OutputType.json, 0)));

            if (Objects.nonNull(getItem())) {
                if (getItem().getName().equals("BROKEN ITEM")) {
                    if (getSlot().equals(SlotType.HELMET)) {
                        EquipmentSlot es = new EquipmentSlot("Helmet", 1096, 408, SlotType.HELMET, null);

                        setSlot(es.getSlot());
                        setX(es.getX());
                        setY(es.getY());
                        setSlotType(es.getSlotType());
                        setItem(null);
                    }

                    setItem(null);
                }
            }
        } else {
            setSlot("Helmet");
            setX(1096);
            setY(408);
            setSlotType(SlotType.HELMET);
            setItem(null);
        }
    }
}
