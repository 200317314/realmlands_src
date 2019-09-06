package com.kingdomlands.game.core.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.entities.util.pathing.AStar;
import com.kingdomlands.game.core.stages.StageManager;

import java.security.SecureRandom;
import java.util.Objects;

/**
 * Created by David K on Mar, 2019
 */
public abstract class Entity extends Actor {
    private EntityType entityType;
    private final SecureRandom secureRandom = new SecureRandom();
    private Vector2 moveTo = null;

    public Entity(EntityType entityType, String name, int x, int y) {
        this.setName(name);
        this.setX(x);
        this.setY(y);
        this.entityType = entityType;
        this.setBounds(x, y, 0, 0);

        Entity entity = this;
        /*this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (entity instanceof Monster) {
                    PlayerManager.getCurrentPlayer().setTarget(entity);
                }
            }
        });*/

        this.addListener(new ClickListener(Input.Buttons.RIGHT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               /* if (entity instanceof Monster) {
                    if (Objects.nonNull(PlayerManager.getCurrentPlayer().getTarget())) {
                        if (PlayerManager.getCurrentPlayer().getTarget().equals(entity)) {
                            PlayerManager.getCurrentPlayer().setTarget(null);
                        }
                    }
                }*/

                /*if (entity instanceof Item) {
                    PlayerManager.getCurrentPlayer().setTarget(null);
                    PlayerManager.getCurrentPlayer().setTarget(entity);
                }*/

               /* if (entity instanceof GameObject) {
                    PlayerManager.getCurrentPlayer().setTarget(null);
                    PlayerManager.getCurrentPlayer().setTarget(entity);
                }*/

                if (!ContextManager.getTable().isVisible()) {
                    ContextManager.open();

                    ContextManager.getTable().setVisible(true);

                    ContextManager.getTable().setPosition(getX() - 100, getY() - 200);
                    ContextManager.getTable().setWidth(400);
                    ContextManager.getTable().setHeight(400);

                    contextMenu();
                } else {
                    ContextManager.close();
                }
            }
        });
    }

    public abstract void draw(Batch batch, float parentAlpha);

    public abstract void contextMenu();

    public EntityType getEntityType() {
        return entityType;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), 64, 64);
    }

    public boolean collided(Entity entity) {
        for (Entity e : StageManager.getAllEntities()) {
            if (Objects.nonNull(e)) {
                if (e.getBounds().overlaps(entity.getBounds())) {
                    if (!entity.getName().equals(e.getName())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public double getDistance(Entity entity) {
        if (Objects.nonNull(entity)) {
            return (int)Math.hypot(this.getX()-entity.getX(), this.getY()-entity.getY());
        }

        return -1;
    }

    public double getDistanceVector(Vector2 vector2) {
        if (Objects.nonNull(vector2)) {
            return Math.hypot(this.getX()-vector2.x, this.getY()-vector2.y);
        }

        return -1;
    }

    public boolean move(Vector2 target, double speed) {
        Gdx.app.postRunnable(() -> {
            AStar aStar;
            if (this instanceof Player) {
                aStar = new AStar(this.getPosition(), target, this, PlayerManager.getCurrentPlayer().getTarget());
            } else {
                aStar = new AStar(this.getPosition(), target, this, null);
            }

            Vector2 next = aStar.getNextNode();

            if (Objects.nonNull(next)) {
                double destX = next.y*64 - this.getX();
                double destY = next.x*64 - this.getY();

                double dist = Math.sqrt(destX * destX + destY * destY);
                destX = destX / dist;
                destY = destY / dist;

                double travelX = (destX * speed);
                double travelY = (destY * speed);

                if (speed < dist) {
                    this.moveBy((int) travelX, (int)travelY);
                }
            } else {
                moveTo = null;
            }
        });


        return false;
    }

    public BitmapFont getFont() {
        return Constants.DEFAULT_FONT;
    }

    public int getRarityRng() {
        int chance = secureRandom.nextInt(1000) + 1;

        if (chance == 1) {
            return 5;
        } else if (chance <= 10) {
            return 4;
        } else if (chance <= 50) {
            return 3;
        } else if (chance <= 100) {
            return 2;
        }

        return 1;
    }

    public Color getRarityColor(int rarity) {
        return Methods.getRarityColor(rarity);
    }

    public Vector2 getRandomPointAway(Entity entity, int unitsAway) {
        int base = 64;
        int distance = Methods.random(1, unitsAway);
        int rng_x = Methods.getSecureRandom().nextInt(2);
        int rng_y = Methods.getSecureRandom().nextInt(2);
        int y;
        int x;
        if (rng_x == 0) {
            x = Methods.random(64, distance * base) + (int)entity.getX();
        } else {
            x = -Methods.random(64, (distance * base)) + (int)entity.getX();
        }

        if (rng_y == 0) {
            y = Methods.random(64, distance * base) + (int)entity.getY();
        } else {
            y = -Methods.random(64, (distance * base)) + (int)entity.getY();
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

    public Vector2 getPosition() {
        return new Vector2((int)this.getX() + 32, (int)this.getY() + 32);
    }

    public Vector2 getMoveTo() {
        return moveTo;
    }

    public int getLayer() {
        return this.getEntityType().getLayer();
    }

    @Override
    public String toString() {
        return getName() + " [x:"+getX() + "y:"+getY()+"] Layer: " + this.getZIndex();
    }
}
