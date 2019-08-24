package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.stages.StageManager;

import static com.kingdomlands.game.core.entities.util.Methods.isMouseOverItem;

/**
 * Created by David K on Feb, 2019
 */

public class DamageText {
    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/opensans.ttf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    private int x, y, damage, alpha;
    private DamageType damageType;
    private BitmapFont font;
    private Image icon;

    public DamageText(int x, int y, int damage, int alpha, DamageType damageType) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.alpha = 180;
        this.damageType = damageType;

        if (damageType == DamageType.EXP) {
            this.y = y - 20;
        }

        parameter.size = 16;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1.2f;
        this.font = generator.generateFont(parameter);
        this.font.setColor(damageType.getFont().getColor());

        this.icon = new Image(new Texture(Gdx.files.internal("damageicons/" + damageType.getName() + ".png")));
        StageManager.addActor(icon);
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void render(SpriteBatch spriteBatch) {
        GlyphLayout layout = new GlyphLayout();

        if (damageType.equals(DamageType.CRITICAL)) {
            if (alpha > 0) {
                alpha--;

                if (alpha >= 50) {
                    y ++;
                } else {
                    y ++;
                }

                layout.setText(Constants.DEFAULT_FONT, "-" + Methods.convertNum(damage) + " Crit!");
                font.draw(spriteBatch, "-" + Methods.convertNum(damage) + " Crit!", x, y);
            } else {
                icon.remove();
                DamageTextManager.remove(this);
            }
        } else if(!damageType.equals(DamageType.EXP) && !damageType.equals(DamageType.HPREGEN)) {
            if (alpha > 0) {
                alpha--;

                if (alpha >= 50) {
                    y ++;
                } else {
                    y ++;
                }

                layout.setText(Constants.DEFAULT_FONT, "-" + Methods.convertNum(damage));
                font.draw(spriteBatch, "-" + Methods.convertNum(damage), x, y);
            } else {
                icon.remove();
                DamageTextManager.remove(this);
            }
        } else if (damageType.equals(DamageType.EXP)) {
            if (alpha > 0) {
                alpha--;
                y ++;

                layout.setText(Constants.DEFAULT_FONT, "+" + Methods.convertNum(damage) + " Exp");
                font.draw(spriteBatch, "+" + Methods.convertNum(damage) + " Exp", x, y);
            } else {
                icon.remove();
                DamageTextManager.remove(this);
            }
        } else if (damageType.equals(DamageType.HPREGEN)) {
            if (alpha > 0) {
                alpha--;
                y ++;

                layout.setText(Constants.DEFAULT_FONT, "+" + Methods.convertNum(damage) + " Hp");
                font.draw(spriteBatch, "+" + Methods.convertNum(damage) + " Hp", x, y);
            } else {
                icon.remove();
                DamageTextManager.remove(this);
            }
        }

        icon.setPosition(x + layout.width + 18, y - 12);
    }
}