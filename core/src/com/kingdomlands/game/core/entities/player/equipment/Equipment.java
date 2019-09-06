package com.kingdomlands.game.core.entities.player.equipment;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.item.SlotType;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.ui.UIManager;
import com.kingdomlands.game.core.entities.util.Attribute;
import com.kingdomlands.game.core.entities.util.Skill;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Equipment implements Json.Serializable {
    private List<EquipmentSlot> equipmentSlots;

    public Equipment(List<EquipmentSlot> equipmentSlots) {
        if (equipmentSlots.isEmpty()) {
            this.equipmentSlots = new ArrayList<>();
            this.equipmentSlots.add(new EquipmentSlot("Helmet", 1096, 408, SlotType.HELMET, null));
            this.equipmentSlots.add(new EquipmentSlot("Necklace", 1094, 344, SlotType.NECKLACE, null));
            this.equipmentSlots.add(new EquipmentSlot("Chestplate", 1093, 280, SlotType.CHESTPLATE, null));
            this.equipmentSlots.add(new EquipmentSlot("Platelegs", 1094, 216, SlotType.PLATELEGS, null));
            this.equipmentSlots.add(new EquipmentSlot("Boots", 1093, 150, SlotType.BOOTS, null));
            this.equipmentSlots.add(new EquipmentSlot("Righthand", 1030, 280, SlotType.RIGHTHAND, null));
            this.equipmentSlots.add(new EquipmentSlot("Lefthand", 1156, 280, SlotType.LEFTHAND, null));
            this.equipmentSlots.add(new EquipmentSlot("Cape", 1028, 344, SlotType.CAPE, null));
            this.equipmentSlots.add(new EquipmentSlot("Gloves", 1156, 216, SlotType.GLOVES, null));
            this.equipmentSlots.add(new EquipmentSlot("Ring", 1156, 216, SlotType.RING, null));
        } else {
            this.equipmentSlots = equipmentSlots;
        }
    }

    public Equipment() {

    }

    public void addItem(Item item) {
        if (Objects.nonNull(item)) {
            EquipmentSlot equipmentSlot = getEquipmentBySlot(item.getSlotType());

            if (Objects.nonNull(equipmentSlot)) {
                if (Objects.nonNull(equipmentSlot.getItem()) && !equipmentSlot.getItem().getName().equals("BROKEN ITEM")) {
                    //PlayerManager.getCurrentPlayer().getInventory().addItem(equipmentSlot.getItem());
                    PlayerManager.getCurrentPlayer().getInventory().deleteItem(PlayerManager.getCurrentPlayer().getInventory().getItemIndex(item));
                    removeItem(equipmentSlot.getItem());
                    addAttributeValues(item.getAttributes());

                    Item i = ItemManager.createItemById(item.getId(), item.getAmount());
                    i.setAttributes(item.getAttributes());
                    i.setRarity(item.getRarity());

                    equipmentSlot.setItem(i);
                } else {
                    addAttributeValues(item.getAttributes());
                    PlayerManager.getCurrentPlayer().getInventory().deleteItem(PlayerManager.getCurrentPlayer().getInventory().getItemIndex(item));

                    Item i = ItemManager.createItemById(item.getId(), item.getAmount());
                    i.setAttributes(item.getAttributes());
                    i.setRarity(item.getRarity());

                    equipmentSlot.setItem(i);
                }
            }
        }
    }

    public void removeItem(Item item) {
        if (Objects.nonNull(item)) {
            removeAttributeValues(item.getAttributes());

            Item i = ItemManager.createItemById(item.getId(), item.getAmount());
            i.setRarity(item.getRarity());
            i.setAttributes(item.getAttributes());

            PlayerManager.getCurrentPlayer().getInventory().addItem(i);

            for (EquipmentSlot e : equipmentSlots) {
                if (Objects.nonNull(e) && Objects.nonNull(e.getItem())) {
                   if (e.getItem().equals(item)) {
                       e.setItem(null);
                   }
                }
            }

            UIManager.openEquipmentTab();
        }
    }

    public void deleteItem(Item item) {
        if (Objects.nonNull(item)) {
            for (EquipmentSlot e : equipmentSlots) {
                if (Objects.nonNull(e) && Objects.nonNull(e.getItem())) {
                    if (e.getItem().equals(item)) {
                        e.setItem(null);
                    }
                }
            }
        }
    }

    public void removeQuantityOne(SlotType slotType) {
        for (Item i : getItems()) {
            if (i.getSlotType().equals(slotType)) {
                if (i.getAmount() == 1) {
                    deleteItem(i);
                } else {
                    i.setAmount(i.getAmount() - 1);
                }
            }
        }

        UIManager.openEquipmentTab();
    }

    public List<EquipmentSlot> getEquipmentSlots() {
        return this.equipmentSlots;
    }

    public void setEquipmentSlots(List<EquipmentSlot> equipmentSlots) {
        this.equipmentSlots = equipmentSlots;
    }

    private EquipmentSlot getEquipmentBySlot(SlotType slotType) {
        for (EquipmentSlot e : this.equipmentSlots) {
            if (Objects.nonNull(e)) {
                if (e.getSlotType().equals(slotType)) {
                    return e;
                }
            }
        }

        return null;
    }

    private void addAttributeValues(List<Attribute> attributes) {
        for (Attribute a : attributes) {
            if (Objects.nonNull(a)) {
                double currVal = Attribute.getAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), a.getName());

                Attribute.setAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), a.getName(), (int) (currVal + a.getValue()));

                if (containsSkillBuff(a)) {
                    Skill.addLevelToSkillFromList(PlayerManager.getCurrentPlayer().getPlayerSkills(), a.getName(), a.getValue());
                }
            }
        }
    }

    private void removeAttributeValues(List<Attribute> attributes) {
        for (Attribute a : attributes) {
            if (Objects.nonNull(a)) {
                double currVal = Attribute.getAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), a.getName());

                Attribute.setAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), a.getName(), (int) (currVal - a.getValue()));

                if (containsSkillBuff(a)) {
                    Skill.removeLevelToSkillFromList(PlayerManager.getCurrentPlayer().getPlayerSkills(), a.getName(), a.getValue());
                }
            }
        }
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        for (EquipmentSlot e : getEquipmentSlots()) {
            if (Objects.nonNull(e.getItem())) {
                items.add(e.getItem());
            }
        }
        return items;
    }

    @Override
    public void write(Json json) {
        json.writeValue("equipment", getEquipmentSlots());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue equipment = jsonData.get("equipment");
        List<EquipmentSlot> equipmentSlots = new ArrayList<>();

        for (JsonValue v : equipment.get("equipment")) {
            equipmentSlots.add(json.fromJson(EquipmentSlot.class, v.toString()));
        }

        setEquipmentSlots(equipmentSlots);
    }

    public Item getItemFromSlot(SlotType slotType) {
        for (EquipmentSlot e : getEquipmentSlots()) {
            if (Objects.nonNull(e.getItem())) {
                if (e.getSlotType() == slotType) {
                   return e.getItem();
                }
            }
        }

        return null;
    }

    public boolean containsSkillBuff(Attribute a) {
        if (Objects.nonNull(a)) {
            for (Skill s : PlayerManager.getCurrentPlayer().getPlayerSkills()) {
                if (Objects.nonNull(s)) {
                    if (s.getName().equals(a.getName())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
