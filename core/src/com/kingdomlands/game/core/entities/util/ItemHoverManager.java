package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;

import java.util.List;
import java.util.Objects;

import static com.kingdomlands.game.core.entities.util.Methods.isMouseOverItem;

/**
 * Created by David K on Apr, 2019
 */
public class ItemHoverManager {
    private static Batch batch = null;
    private static final BitmapFont font = Constants.DEFAULT_FONT;
    private static ShapeRenderer shapeRenderer;
    private static int INVERT = 245;

    public ItemHoverManager(Batch batch, ShapeRenderer shapeRenderer) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
    }

    public static void renderBackground(int height, int width) {
        Vector2 pos = new Vector2(Gdx.input.getX() + 6 + 18, (800 - Gdx.input.getY()) - height + 16 + 24);

        if (pos.x <= 1028) {
            Gdx.gl20.glLineWidth(4);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.4f));
            shapeRenderer.rect(pos.x, pos.y, width + 4, height + 4);
            shapeRenderer.rect(pos.x - 4, pos.y - 4, width + 4, height + 4);

            shapeRenderer.setColor(new Color(0, 0, 0, 0.4f));
            shapeRenderer.rect(pos.x, pos.y, width, height);
        } else {
            Gdx.gl20.glLineWidth(4);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.4f));
            shapeRenderer.rect(pos.x - INVERT, pos.y, width + 4, height + 4);
            shapeRenderer.rect(pos.x - INVERT - 4, pos.y - 4, width + 4, height + 4);

            shapeRenderer.setColor(new Color(0, 0, 0, 0.4f));
            shapeRenderer.rect(pos.x - INVERT, pos.y, width, height);
        }
    }

    public static void renderItemHover(Item item) {
        if (Objects.nonNull(item)) {
            Vector2 pos = new Vector2(Gdx.input.getX() + 18, 800 - Gdx.input.getY() + 24);
            if (pos.x <= 1028) {
                font.setColor(Methods.getRarityColor(item.getRarity()));
                font.draw(batch, item.getName(), (int) pos.x + 12, (int) pos.y + 15);
                font.setColor(Color.WHITE);

                GlyphLayout layout = new GlyphLayout(), ree = new GlyphLayout();
                ree.setText(Constants.DEFAULT_FONT, item.getDescription());
                float width = ree.width > 200 ? ree.width : 200;

                layout.setText(Constants.DEFAULT_FONT, "Lvl: " + item.getLevel());

                font.setColor(Color.RED);
                font.draw(batch,  "Type: " + item.getItemType().toString().toLowerCase(), pos.x + (int) width/2 - 40, (int) pos.y + 2);
                font.setColor(Color.WHITE);
                font.draw(batch, item.getDescription(), (int) pos.x + 12, (int) pos.y - 10);

                if (item.getAttributes().size() != 0) {
                    font.setColor(Color.SCARLET);

                    int offSet = 12;
                    int multi = 0;
                    for (Attribute a : item.getAttributes()) {
                        multi++;
                        if (!a.getName().equals("AttackSpeed") || !a.getName().equals("AttackPower")) {
                            font.draw(batch, a.getName() + ": +" + Methods.convertNum((int) a.getValue()), (int) pos.x + 12, (int) pos.y - (16 + offSet * multi));
                        }
                    }
                }

                font.setColor(Color.GREEN);
                font.draw(batch,  "Requires level: " + item.getLevel(), pos.x +12, (int) pos.y - (32 + (item.getAttributes().size()*12)));
                font.setColor(Color.YELLOW);
                font.draw(batch,  "Price: " + Methods.convertNum(ItemManager.getCostOfItem(item)) + "g", pos.x +12, (int) pos.y - (44 + (item.getAttributes().size()*12)));
            } else {
                font.setColor(Methods.getRarityColor(item.getRarity()));
                font.draw(batch, item.getName(), (int) pos.x + 12, (int) pos.y + 15 - INVERT);
                font.setColor(Color.WHITE);

                GlyphLayout layout = new GlyphLayout(), ree = new GlyphLayout();
                ree.setText(Constants.DEFAULT_FONT, item.getDescription());
                float width = ree.width > 200 ? ree.width : 200;

                layout.setText(Constants.DEFAULT_FONT, "Lvl: " + item.getLevel());

                font.setColor(Color.RED);
                font.draw(batch,  "Type: " + item.getItemType().toString().toLowerCase(), pos.x + (int) width/2 - 40, (int) pos.y + 2 - INVERT);
                font.setColor(Color.WHITE);
                font.draw(batch, item.getDescription(), (int) pos.x + 12, (int) pos.y - 10 - INVERT);

                if (item.getAttributes().size() != 0) {
                    font.setColor(Color.SCARLET);

                    int offSet = 12;
                    int multi = 0;
                    for (Attribute a : item.getAttributes()) {
                        multi++;
                        if (!a.getName().equals("AttackSpeed") || !a.getName().equals("AttackPower")) {
                            font.draw(batch, a.getName() + ": +" + Methods.convertNum((int) a.getValue()), (int) pos.x + 12, (int) pos.y - (16 + offSet * multi) - INVERT);
                        }
                    }
                }

                font.setColor(Color.GREEN);
                font.draw(batch,  "Requires level: " + item.getLevel(), pos.x +12, (int) pos.y - (32 + (item.getAttributes().size()*12)) - INVERT);
                font.setColor(Color.YELLOW);
                font.draw(batch,  "Price: " + Methods.convertNum(ItemManager.getCostOfItem(item)) + "g", pos.x +12, (int) pos.y - (44 + (item.getAttributes().size()*12)) - INVERT);
            }
        }
    }
}