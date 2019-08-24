//package com.kingdomlands.game.core.entities.chest;
//
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.kingdomlands.game.core.entities.Entity;
//import com.kingdomlands.game.core.entities.EntityType;
//import com.kingdomlands.game.core.entities.monster.Drop;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Travis T
// */
//public class Chest extends Entity {
//
//
//
//    private Image image;
//    private int id, chestRarity, itemRarity;
//    private String description;
//    private Map<Integer, Map<Integer, List<Drop>>> drops;
//
//    public Chest(int id, EntityType entityType, String name, int x, int y, Image image, int chestRarity, int itemRarity, String description, Map<Integer, Map<Integer, >> drops) {
//        super(entityType, name, x, y);
//        this.id = id;
//        this.image = image;
//        this.chestRarity = chestRarity;
//        this.itemRarity = itemRarity;
//        this.description = description;
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//
//    }
//}
