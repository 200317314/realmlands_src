package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kingdomlands.game.core.stages.StageManager;

import java.util.Objects;

/**
 * Created by David K on Mar, 2019
 */
public class AlertText {
    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/opensans.ttf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    private int x, y, alpha;
    private DamageType damageType;
    private BitmapFont font;
    private String message;
    private Image icon;

    public AlertText(int x, int y, String message, DamageType damageType) {
        this.x = x + 64;
        this.y = y + 32;
        this.alpha = 100;
        this.damageType = damageType;

        parameter.size = 16;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1.2f;
        this.font = generator.generateFont(parameter);
        this.font.setColor(damageType.getFont().getColor());

        this.message = message;

        if (damageType.equals(DamageType.LEVEL_UP)) {
            this.icon = new Image(new Texture(Gdx.files.internal("damageicons/" + damageType.getName() + ".png")));
            StageManager.addActor(icon);
        }
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

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void render(SpriteBatch spriteBatch) {
        if (alpha > 0) {
            alpha--;
            y += Methods.random(1, 2);

            font.draw(spriteBatch, message, x - 48, y);
        } else {
            AlertTextManager.remove(this);

            if (Objects.nonNull(icon)) {
                icon.remove();
            }
        }

        if (Objects.nonNull(icon)) {
            icon.setPosition(x - 62, y - 12);
        }
    }
}
