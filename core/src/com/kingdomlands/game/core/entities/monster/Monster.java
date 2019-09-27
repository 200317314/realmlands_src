package com.kingdomlands.game.core.entities.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.chat.ChatManager;
import com.kingdomlands.game.core.entities.projectile.Projectile;
import com.kingdomlands.game.core.entities.projectile.Projectiles;
import com.kingdomlands.game.core.entities.util.*;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.stages.StageManager;

import javax.jws.Oneway;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kingdomlands.game.core.entities.util.Methods.isMouseOverItem;

/**
 * Created by David K on Mar, 2019
 */
public class Monster extends Entity {
    private Image image;
    private MonsterAttributes monsterAttributes;
    private float time = 0;
    private double range;
    private boolean aggressive;
    private int id, rarity;
    private Vector2 movePoint = null;
    private boolean isMoving = false;
    private List<Drop> drops;
    private Projectiles projectile;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Monster(int id, EntityType entityType, String name, int x, int y, Image image, MonsterAttributes monsterAttributes, boolean aggressive, double range, int rarity, List<Drop> drops, Projectiles projectile) {
        super(entityType, name, x, y);
        this.id = id;
        this.image = image;
        this.monsterAttributes = monsterAttributes;
        this.aggressive = aggressive;
        this.range = range;
        this.rarity = rarity;
        this.drops = drops;
        this.projectile = projectile;

        if (rarity == -1) {
            this.rarity = getRarityRng();
            this.monsterAttributes.setAttackPower(randomizedAttr(this.monsterAttributes.getAttackPower(), this.rarity));
            this.monsterAttributes.setStrength(randomizedAttr(this.monsterAttributes.getStrength(), this.rarity));
            this.monsterAttributes.setStamina(randomizedAttr(this.monsterAttributes.getStamina(), this.rarity));
            this.monsterAttributes.setAgility(randomizedAttr(this.monsterAttributes.getAgility(), this.rarity));
            this.monsterAttributes.setIntellect(randomizedAttr(this.monsterAttributes.getIntellect(), this.rarity));
            this.monsterAttributes.setCrit(Methods.random((int)this.monsterAttributes.getCrit(), (int)this.monsterAttributes.getCrit()*this.rarity));
            this.monsterAttributes.setMaxHp(randomizedAttr(this.monsterAttributes.getMaxHp(), this.rarity));
            this.monsterAttributes.setMaxExp(randomizedAttr(this.monsterAttributes.getMaxExp(), this.rarity));
            this.monsterAttributes.setCurrentHp(this.monsterAttributes.getMaxHp());
            this.monsterAttributes.setCurrExp(this.monsterAttributes.getMaxExp());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        time += Gdx.graphics.getDeltaTime();
        this.setBounds(getX(), getY(), image.getImageWidth(), image.getImageHeight());
        image.setPosition(getX(), getY());
        image.draw(batch, parentAlpha);

        if (Objects.nonNull(this.getMoveTo()) || aggressive) {
            registerMove();
        }

        inCombat();
        //Gdx.app.postRunnable(() -> ambientMove());

        Player nearest = PlayerManager.getNearestPlayer(this);
        if (Objects.nonNull(nearest)) {
            GlyphLayout layout = new GlyphLayout();
            layout.setText(Constants.DEFAULT_FONT, getName() + " Lvl: " + (int)monsterAttributes.getLevel());

            getFont().draw(batch, getName() + " Lvl: " + (int)monsterAttributes.getLevel(), getX() - 12, getY() + 100);
            getFont().setColor(getRarityColor(rarity));
            getFont().draw(batch, "" + Methods.getRarityName(rarity), getX() + 4, getY() + 88);
            getFont().setColor(Color.RED);
            getFont().draw(batch, "Hp: " + Methods.convertNum((int) monsterAttributes.getCurrentHp()) + "/" + Methods.convertNum((int)monsterAttributes.getMaxHp()), getX() - 12, getY() + 76);
            getFont().setColor(Color.WHITE);
        }
    }

    @Override
    public void contextMenu() {
        ContextManager.setClickedEntity(this);
        //ContextManager.getContextMenu().setItems(getName(),"Attack", "Examine");
        com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);
        list.setItems("Choose:", "Attack", "Examine");
        //list.addListener(new ListListener());

        ScrollPane scrollPane = new ScrollPane(list, Constants.DEFAULT_SKIN);
        ContextManager.getTable().add(scrollPane);
        //ContextManager.getContextMenu().setSelected(getName());
    }

    public void registerMove() {
        if (aggressive && !isInCombat()) {
            isMoving= true;
            Player nearest = PlayerManager.getNearestPlayer(this);
            if (Objects.nonNull(nearest)) {
                if (getDistance(nearest) <= range) {
                    isMoving = true;
                    move(nearest.getPosition(), monsterAttributes.getMovementSpeed());
                }
            }
        }
        isMoving = false;
    }

    public void ambientMove() {
        Player player = PlayerManager.getCurrentPlayer();
        if (!isMoving && !isInCombat() && !aggressive) {
            if (Objects.nonNull(movePoint)) {
                move(Methods.getCenter(movePoint), (int)monsterAttributes.getMovementSpeed());
            } if (this.getDistance(player) <= 768 && this.getDistance(player) > 64 && Objects.isNull(movePoint)) {
                if (Methods.random(1, 500) == 5) {
                    movePoint = getRandomPointAway(this, Methods.random(2, 8));
                }
            } else if (this.getDistanceVector(movePoint) < 128) {
                movePoint = null;
            }
        }
    }

    public void inCombat() {
        Player nearest = PlayerManager.getNearestPlayer(this);
        if (Objects.nonNull(nearest) && aggressive) {
            if (Objects.nonNull(projectile)) {
                if (getDistance(nearest) <= projectile.getRange()*64 && time >= monsterAttributes.getAttackSpeed()) {
                    time = 0;
                    int damage = (int) monsterAttributes.getProjectileDamage() + (int) monsterAttributes.getMagicalDamage();

                    StageManager.addActor(new Projectile(EntityType.GAMEOBJECT, "Arrow",
                            (int)this.getX(), (int)this.getY(), projectile, damage, nearest));
                }
            } else {
                if (isInCombat() && time >= monsterAttributes.getAttackSpeed()) {
                    time = 0;
                    int damage = (int) monsterAttributes.getPhysicalDamage();

                    if (Methods.random(1, 100) <= monsterAttributes.getCrit()) {
                        nearest.takeDamage(damage, this, true);
                    } else {
                        nearest.takeDamage(damage, this, false);
                    }
                }
            }
        }
    }

    public void takeDamage(int damage, boolean crit, Projectiles projectile) {
        DamageType damageType = DamageType.PHYSICAL;

        if (!aggressive) {
            aggressive = true;
        }

        damage = PlayerManager.getCurrentPlayer().getDamageMultiplier(damage);

        if (Objects.nonNull(projectile)) {
            if (projectile.isMagical()) {
                damageType = DamageType.MAGICAL;
                SoundManager.playSoundFx(Gdx.audio.newSound(Gdx.files.internal("sounds/magic_attack.wav")));
            } else {
                damageType = DamageType.RANGED;
                SoundManager.playSoundFx(Gdx.audio.newSound(Gdx.files.internal("sounds/player_attack.wav")));
            }
        } else {
            SoundManager.playSoundFx(Gdx.audio.newSound(Gdx.files.internal("sounds/player_attack.wav")));
        }

        if (crit) {
            DamageTextManager.add(new DamageText((int)getX() + 16, (int)getY() + 16, ((int)monsterAttributes.getNegatedDamage(damage * 2)), 100, DamageType.CRITICAL));
            monsterAttributes.setCurrentHp(monsterAttributes.getCurrentHp() - (int)monsterAttributes.getNegatedDamage(damage * 2));
            ChatManager.addChat("[Battle]: " + "You attack a " + this.getName() + " for " + (int)monsterAttributes.getNegatedDamage(damage * 2) + " damage.");
        } else {
            DamageTextManager.add(new DamageText((int)getX() + 16, (int)getY() + 16, (int)monsterAttributes.getNegatedDamage(damage), 100, damageType));
            monsterAttributes.setCurrentHp(monsterAttributes.getCurrentHp() - (int)monsterAttributes.getNegatedDamage(damage));
            ChatManager.addChat("[Battle]: " + "You attack a " + this.getName() + " for " + (int)monsterAttributes.getNegatedDamage(damage) + " damage.");
        }

        if (monsterAttributes.getCurrentHp() <= 0) {
            Entity entity = PlayerManager.getCurrentPlayer().getTarget();
            if (Objects.nonNull(entity)) {
                if (entity instanceof Monster) {
                    Monster m = (Monster) entity;
                    if (m.equals(this)) {
                        PlayerManager.getCurrentPlayer().setTarget(null);
                        DamageTextManager.add(new DamageText((int)getX() + 16, (int)getY() + 16, (int) monsterAttributes.getCurrExp(), 100, DamageType.EXP));
                    }
                }
            }

            List<Item> dropped = new ArrayList<>();

            drops.forEach(d -> {
                if (Objects.nonNull(d)) {
                    int chance = Methods.getSecureRandom().nextInt(1000) + 1;

                    if (chance <= d.chance) {
                        Item droppable = ItemManager.createItemById(d.id, Methods.random(d.min, d.max));
                        droppable.setPosition(this.getX(), this.getY());
                        StageManager.addActor(droppable);
                        dropped.add(droppable);
                    }
                }
            });

            if (PlayerManager.isPremium() || PlayerManager.isFounder()) {
                drops.forEach(d -> {
                    if (Objects.nonNull(d)) {
                        int chance = Methods.getSecureRandom().nextInt(1000) + 1;

                        if (chance <= d.chance) {
                            Item droppable = ItemManager.createItemById(d.id, Methods.random(d.min, d.max));
                            droppable.setPosition(this.getX(), this.getY());
                            StageManager.addActor(droppable);
                            dropped.add(droppable);
                        }
                    }
                });
            }

            this.remove();

            monsterAttributes.setCurrExp(monsterAttributes.getCurrExp() * PlayerManager.getExpModifier());

            if (Objects.nonNull(PlayerManager.getNearestPlayer(this))) {
                if (!dropped.isEmpty()) {
                    String line = "";

                    for (Item i : dropped) {
                        if (Objects.nonNull(i)) {
                            line = line + i.getName() + " x" + i.getAmount() + " ";
                        }
                    }

                    ChatManager.addChat("[Loot]: " + "You gained " + Math.round(monsterAttributes.getCurrExp()) + " exp and looted, " + line + " from " + this.getName() + ".");
                } else {
                    ChatManager.addChat("[Loot]: " + "You gained " + Math.round(monsterAttributes.getCurrExp()) + " exp from " + this.getName() + ".");
                }

                SoundManager.playSoundFx(Gdx.audio.newSound(Gdx.files.internal("sounds/" + "expgain.wav")));
                PlayerManager.getNearestPlayer(this).addExp(monsterAttributes.getCurrExp());
            }
        }
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public MonsterAttributes getMonsterAttributes() {
        return monsterAttributes;
    }

    public void setMonsterAttributes(MonsterAttributes monsterAttributes) {
        this.monsterAttributes = monsterAttributes;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Projectiles getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectiles projectile) {
        this.projectile = projectile;
    }

    public boolean isInCombat() {
        Player nearest = PlayerManager.getNearestPlayer(this);

        if (Objects.nonNull(nearest)) {
            if (aggressive && this.getDistance(nearest) <= 64) {
                return true;
            }
        }
        return false;
    }

    private int randomizedAttr(double attr, int rarity) {
        return Methods.random((int)attr, (int)(attr*rarity));
    }

    public void renderBackground() {
        int height = 40;
        int width = 100;

        GlyphLayout layout = new GlyphLayout();
        layout.setText(Constants.DEFAULT_FONT, getName() + " Lvl: " + (int)monsterAttributes.getLevel());

        if (layout.width > width) {
            width = (int)layout.width + 6;
        }

        Vector2 pos = this.getPosition();
        pos.x = pos.x - 46;
        pos.y = pos.y + 30;

        shapeRenderer.setProjectionMatrix(StageManager.getCurrentStage().getCamera().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl20.glLineWidth(4);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.4f));
        shapeRenderer.rect(pos.x, pos.y, width + 4, height + 4);
        shapeRenderer.rect(pos.x - 4, pos.y - 4, width + 4, height + 4);

        shapeRenderer.setColor(new Color(0, 0, 0, 0.4f));
        shapeRenderer.rect(pos.x, pos.y, width, height);
        shapeRenderer.end();
    }
}
