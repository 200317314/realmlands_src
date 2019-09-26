package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.objects.GameObject;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.ui.HUD;
import com.kingdomlands.game.core.entities.player.ui.UIManager;
import com.kingdomlands.game.core.entities.projectile.Projectile;
import com.kingdomlands.game.core.entities.util.pathing.AStar;
import com.kingdomlands.game.core.stages.StageManager;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Created by David K on Jan, 2019
 */
public class Methods {
    private static SecureRandom secureRandom = new SecureRandom();
    public static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/opensans.ttf"));
    public static FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    private static boolean loading = false, proceed = false;

    public static int getWidthBasedOnPercent(int curr, int max, int width) {
        double percent = ((float)curr/max);
        return (int)(percent*width);
    }

    public static int random(int min, int max) {
        if (min == 0) {
            min = 1;
        }

        if (max == 0) {
            max = 3;
        }

        int integer = secureRandom.nextInt(max) + min;
        return (integer == 0) ? 1 : integer;
    }

    public static SecureRandom getSecureRandom() {
        return secureRandom;
    }

    public static Vector2 getCenter(Vector2 vector) {
        return new Vector2(vector.x + 32, vector.y + 32);
    }

    public static boolean isMouseOverMe(Rectangle rectangle) {
        if (rectangle.contains(StageManager.getCurrentStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(),Gdx.input.getY())))) {
            return true;
        }

        return false;
    }

    public static String convertNum(double num) {
        DecimalFormat df = new DecimalFormat("##.##");
        if (num >= 1000000.00) {
            return df.format(((double)num/1000000.00)) + "m";
        } else if (num >= 100000.00) {
            return df.format((num/1000.00)) + "k";
        } else if (num < 100000.00) {
            return "" + (int)num;
        }
        return "0k";
    }

    public static String getRarityName(int rarity) {
        if (rarity == 2) {
            return "Unique ";
        } else if (rarity == 3) {
            return "Rare ";
        } else if (rarity == 4) {
            return "Mystic ";
        } else if (rarity == 5) {
            return "Legendary ";
        } else if (rarity == 6) {
            return "Demi-God ";
        } else if (rarity == 7) {
            return "God ";
        }

        return "Common ";
    }

    public static Color getRarityColor(int rarity) {
        if (rarity == 2) {
            return Color.GREEN;
        } else if (rarity == 3) {
            return Color.BLUE;
        } else if (rarity == 4) {
            return Color.PURPLE;
        } else if (rarity == 5) {
            return Color.ORANGE;
        } else if (rarity == 6) {
            return Color.FIREBRICK;
        } else if (rarity == 7) {
            return Color.GOLD;
        }

        return Color.WHITE;
    }

    public static Item isMouseOverItem() {
        for (Entity i : StageManager.getAllEntityOfType(EntityType.ITEM)) {
            Item item = (Item) i;

            if (Objects.nonNull(UIManager.getCurrentTab()) && Objects.requireNonNull(UIManager.getBounds()).contains(new Vector2(Gdx.input.getX(),800 - Gdx.input.getY()))) {
                return null;
            }

            if (item.getBounds().contains(StageManager.getCurrentStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY())))) {
                return item;
            }
        }

        return null;
    }

    public static GameObject isMouseOverGameObject() {
        for (Entity i : StageManager.getAllEntityOfType(EntityType.GAMEOBJECT)) {
            if (i instanceof Projectile) {

            } else {
                GameObject gameObject = (GameObject) i;

                if (Objects.nonNull(UIManager.getCurrentTab()) && Objects.requireNonNull(UIManager.getBounds()).contains(new Vector2(Gdx.input.getX(),800 - Gdx.input.getY()))) {
                    return null;
                }

                if (gameObject.getBounds().contains(StageManager.getCurrentStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY())))) {
                    return gameObject;
                }
            }
        }

        return null;
    }

    public static Item isMouseOverItemTab() {
        for (Item item : PlayerManager.getCurrentPlayer().getInventory().getItems()) {
            if (Objects.nonNull(UIManager.getCurrentTab()) && UIManager.isMouseOverTab()) {
                if (UIManager.getCurrentTab().equals(HUD.ATTRIBUTE)) {
                    return null;
                }
            }

            if (Objects.nonNull(UIManager.getCurrentTab())) {
                if (item.getBounds().contains(new Vector2(Gdx.input.getX(), Gdx.input.getY())) || item.isHovered()) {
                    return item;
                }
            }
        }

        for (Item item : PlayerManager.getCurrentPlayer().getEquipment().getItems()) {
            if (Objects.nonNull(UIManager.getCurrentTab())) {
                if (Objects.nonNull(UIManager.getBounds())) {
                    if (Objects.requireNonNull(UIManager.getBounds()).contains(new Vector2(Gdx.input.getX(), Constants.WINDOW_HEIGHT - Gdx.input.getY()))) {
                        if (UIManager.getCurrentTab().equals(HUD.ATTRIBUTE) || UIManager.getCurrentTab().equals(HUD.INVENTORY)) {
                            return null;
                        }
                    }
                }
            }

            if (Objects.nonNull(UIManager.getCurrentTab())) {
                if (item.getBounds().contains(new Vector2(Gdx.input.getX(), 800 - Gdx.input.getY())) || item.isHovered()) {
                    return item;
                }
            }
        }
        return null;
    }

    public static double getDistanceVector(Vector2 vector2, Vector2 entity) {
        if (Objects.nonNull(vector2)) {
            return Math.hypot(entity.x-vector2.x, entity.y-vector2.y);
        }

        return -1;
    }

    public static void renderBackground(Vector2 pos , int height, int width, ShapeRenderer shapeRenderer) {
        int INVERT = 245;

        if (pos.x <= 1028) {
            Gdx.gl20.glLineWidth(4);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.4f));
            shapeRenderer.rect(pos.x, pos.y, width + 4, height + 4);
            shapeRenderer.rect(pos.x - 4, pos.y - 4, width + 4, height + 4);

            shapeRenderer.setColor(new Color(0, 0, 0, 0.5f));
            shapeRenderer.rect(pos.x, pos.y, width, height);
        } else {
            Gdx.gl20.glLineWidth(4);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.4f));
            shapeRenderer.rect(pos.x - INVERT, pos.y, width + 4, height + 4);
            shapeRenderer.rect(pos.x - INVERT - 4, pos.y - 4, width + 4, height + 4);

            shapeRenderer.setColor(new Color(0, 0, 0, 0.5f));
            shapeRenderer.rect(pos.x - INVERT, pos.y, width, height);
        }
    }

    public static Vector2 getRandomPointAway(Vector2 vec, int unitsAway) {
        int base = 64;
        int distance = Methods.random(1, unitsAway);
        int rng_x = Methods.getSecureRandom().nextInt(2);
        int rng_y = Methods.getSecureRandom().nextInt(2);
        int y;
        int x;
        if (rng_x == 0) {
            x = Methods.random(64, distance * base) + (int)vec.x;
        } else {
            x = -Methods.random(64, (distance * base)) + (int)vec.x;
        }

        if (rng_y == 0) {
            y = Methods.random(64, distance * base) + (int)vec.y;
        } else {
            y = -Methods.random(64, (distance * base)) + (int)vec.y;
        }

        if (x >= 15600) {
            x = 15600;
        }

        if (x <= 66) {
            x = 66;
        }

        if (y >= 15600) {
            y = 15600;
        }

        if (y <= 66) {
            y = 66;
        }

        return new Vector2(x, y);
    }

    public static Vector2 getCloseWalkableTile(Entity target) {
        Vector2 randPoint = getRandomPointAway(target.getPosition(), 2);
        //AStar aStar = new AStar(PlayerManager.getCurrentPlayer().getPosition(), target.getPosition(), PlayerManager.getCurrentPlayer(), target);
        return randPoint;
    }

    public static void setLoading() {
        if (Objects.nonNull(PlayerManager.getCurrentPlayer())) {
            PlayerManager.getCurrentPlayer().resetMovement();
            PlayerManager.getCurrentPlayer().resetTilePoint();
        }

        if (!loading) {
            loading = true;
        } else {
            loading = false;
        }
    }

    public static boolean getLoading() {
        return loading;
    }
}