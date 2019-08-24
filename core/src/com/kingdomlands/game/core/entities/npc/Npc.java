package com.kingdomlands.game.core.entities.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.ui.UI;
import com.kingdomlands.game.core.entities.player.ui.UIManager;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.stages.StageManager;

import java.util.Objects;

public class Npc extends Entity {
    private Image image;
    private NpcType npcType;
    private int shopId;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Npc(EntityType entityType, String name, int x, int y, Image image, NpcType npcType, int shopId) {
        super(entityType, name, x, y);
        this.image = image;
        this.npcType = npcType;
        this.shopId = shopId;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.setBounds(getX(), getY(), image.getImageWidth() + 64, image.getImageHeight() + 64);
        if (Objects.nonNull(image)) {
            image.setPosition(getX(), getY());
            image.draw(batch, 100);
        }

        getFont().setColor(Color.YELLOW);
        getFont().draw(batch, getName(), getX() - 12, getY() + 100);
        getFont().setColor(Color.WHITE);
        getFont().draw(batch, npcType.name(), getX() - 12, getY() + 80);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public NpcType getNpcType() {
        return npcType;
    }

    public void setNpcType(NpcType npcType) {
        this.npcType = npcType;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    @Override
    public void contextMenu() {
        if (this.getNpcType() == NpcType.TRADER) {
            ContextManager.setClickedEntity(this);
            com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);
            list.setItems("Choose:", "Open Shop", "Examine");

            ScrollPane scrollPane = new ScrollPane(list, Constants.DEFAULT_SKIN);
            ContextManager.getTable().add(scrollPane);
        } else if (this.getNpcType() == NpcType.BANKER) {
            ContextManager.setClickedEntity(this);
            com.badlogic.gdx.scenes.scene2d.ui.List<String> list = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(Constants.DEFAULT_SKIN);
            list.setItems("Choose:", "Open Bank", "Examine");

            ScrollPane scrollPane = new ScrollPane(list, Constants.DEFAULT_SKIN);
            ContextManager.getTable().add(scrollPane);
        }
    }

    public void renderBackground() {
        int height = 40;
        int width = 100;

        GlyphLayout layout = new GlyphLayout();
        layout.setText(Constants.DEFAULT_FONT, getName());

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
