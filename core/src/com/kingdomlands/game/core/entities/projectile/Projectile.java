package com.kingdomlands.game.core.entities.projectile;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.monster.Monster;
import com.kingdomlands.game.core.entities.player.Player;

/**
 * Created by David K on Apr, 2019
 */
public class Projectile extends Entity {
    private Projectiles projectiles;
    private Image image;
    private int damage;
    private Entity target;

    public Projectile(EntityType entityType, String name, int x, int y, Projectiles projectiles, int damage, Entity target) {
        super(entityType, name, x + 64, y);
        this.projectiles = projectiles;
        this.image = projectiles.getImage();
        this.damage = damage;
        this.target = target;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.setBounds(getX(), getY(), 64, 64);
        image.setPosition(getX(), getY());
        float angle = MathUtils.radiansToDegrees * MathUtils.atan2(target.getY() - this.getY(), target.getX() - this.getX());
        image.setRotation(angle);
        image.draw(batch, parentAlpha);

        update();
    }

    @Override
    public void contextMenu() {

    }

    private void update() {
        if (this.getDistance(target) < 46) {
            if (target instanceof Monster) {
                ((Monster) target).takeDamage(damage, false, projectiles);
            } else if (target instanceof Player) {
                ((Player) target).takeDamage(damage, this, false);
            }

            this.remove();
        } else {
            this.moveP((int)target.getPosition().x, (int)target.getPosition().y, projectiles.getSpeed());
        }
    }

    public void moveP(int x, int y, double speed) {
        double destX = x - getX();
        double destY = y - getY();

        double dist = Math.sqrt(destX * destX + destY * destY);
        destX = destX / dist;
        destY = destY / dist;

        double travelX = destX * speed;
        double travelY = destY * speed;

        this.setX(getX() + (int)travelX);
        this.setY(getY() + (int)travelY);
    }

    public Projectiles getProjectiles() {
        return projectiles;
    }

    public Image getImage() {
        return image;
    }

    public int getDamage() {
        return damage;
    }

    public Entity getTarget() {
        return target;
    }
}
