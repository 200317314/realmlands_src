package com.kingdomlands.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.monster.Monster;
import com.kingdomlands.game.core.entities.npc.Npc;
import com.kingdomlands.game.core.entities.objects.ObjectType;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.PlayerState;
import com.kingdomlands.game.core.entities.player.chat.ChatManager;
import com.kingdomlands.game.core.entities.player.shops.ShopManager;
import com.kingdomlands.game.core.entities.player.timers.PlayerSaveTimer;
import com.kingdomlands.game.core.entities.player.ui.UI;
import com.kingdomlands.game.core.entities.player.ui.UIManager;
import com.kingdomlands.game.core.entities.util.*;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.entities.util.pathing.AStarManager;
import com.kingdomlands.game.core.entities.util.pathing.EntityThread;
import com.kingdomlands.game.core.stages.StageManager;
import com.kingdomlands.game.core.stages.StageRender;
import com.kingdomlands.game.core.stages.Stages;
import com.kingdomlands.game.core.stages.login.LoginStage;

import java.util.Objects;

import static com.kingdomlands.game.core.entities.player.PlayerManager.*;
import static com.kingdomlands.game.core.entities.util.Methods.isMouseOverItem;
import static com.kingdomlands.game.core.entities.util.Methods.isMouseOverItemTab;

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private SpriteBatch hud;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private ItemHoverManager itemHoverManager;

	private LoginStage loginStage;
	
	@Override
	public void create () {
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursors/cursor.png")), 0 ,0));

		Methods.parameter.size = 14;
		Constants.DEFAULT_FONT = Methods.generator.generateFont(Methods.parameter);
		batch = new SpriteBatch();
		hud = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = Methods.generator.generateFont(Methods.parameter);

		Skin skin = new Skin();
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skins/uiskin.atlas"));
		skin.addRegions(atlas);
		skin.load(Gdx.files.internal("skins/uiskin.json"));
		Constants.DEFAULT_SKIN = skin;

		StageManager.init();
		ItemManager.init();

		StageManager.setCurrentStage(Stages.LOGIN.getStage());
		loginStage = new LoginStage();

		UIManager.init();

		itemHoverManager = new ItemHoverManager(hud, shapeRenderer);

		Methods.generator.dispose();
	}

	@Override
	public void render () {
		//Buffer the negative space black, then color buffer it
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.app.postRunnable(() -> {
			StageManager.getViewPort().setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		});

		if (!Methods.getLoading()) {
			StageRender.render();

			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

			StageManager.getAllEntityOfType(EntityType.MONSTER).forEach(m -> {
				((Monster) m).renderBackground();
			});

			StageManager.getAllEntityOfType(EntityType.NPC).forEach(m -> {
				((Npc) m).renderBackground();
			});

			shapeRenderer.end();

			if (Objects.nonNull(getCurrentPlayer())) {
				PlayerSaveTimer.autoSavePlayer();

				if (Objects.nonNull(PlayerManager.getCurrentPlayer())) {
					UIManager.updatePosition();
				}

				if (Objects.nonNull(StageManager.getCurrentStage())) {
					Gdx.app.postRunnable(() -> {
						StageManager.getCurrentStage().getActors().sort((o1, o2) -> {
							if (o1 instanceof Entity && o2 instanceof Entity) {
								Entity e1 = (Entity) o1;
								Entity e2 = (Entity) o2;

								if (e1.equals(e2)) {
									return 0;
								}

								return e1.getLayer() > e2.getLayer() ? 1 : -1;
							}

							return 0;
						});
					});

					StageManager.getCurrentStage().draw();
					StageManager.getCurrentStage().act();
				}

				batch.setProjectionMatrix(StageManager.getCurrentStage().getCamera().combined);

				batch.begin();
				DamageTextManager.render(batch);
				AlertTextManager.render(batch);
				batch.end();

				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.setColor(Color.RED);
				shapeRenderer.rect(116, 747, Methods.getWidthBasedOnPercent((int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "CurrentHp").getCurrentHp(getCurrentPlayer().getPlayerAttributes()), (int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "MaxHp").getMaxHp(getCurrentPlayer().getPlayerAttributes()), 136), 14);

				shapeRenderer.setColor(Color.GREEN);
				shapeRenderer.rect(139, 707, Methods.getWidthBasedOnPercent((int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "CurrentExp").getCurrentExp(getCurrentPlayer().getPlayerAttributes()), (int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "MaxExp").getMaxExp(getCurrentPlayer().getPlayerAttributes()), 120), 20);

				shapeRenderer.setColor(Color.BLUE);
				shapeRenderer.rect(121, 673, Methods.getWidthBasedOnPercent((int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "CurrentMana").getCurrMana(getCurrentPlayer().getPlayerAttributes()), (int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "MaxMana").getMaxMana(getCurrentPlayer().getPlayerAttributes()), 134), 15);
				shapeRenderer.end();

				hud.begin();
				hud.draw(Images.getHud(), 0 ,0);
				ChatManager.draw(hud, 100);

				UIManager.getIcons().forEach(i -> {
					if (Objects.nonNull(i)) {
						i.getImage().setPosition(i.getX(), i.getY());
						i.getImage().draw(hud, 100);
					}
				});

				font.setColor(Color.WHITE);
				font.setColor(Color.BLACK);
				font.draw(hud, "Lv: " + (int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "Level").getLevel(getCurrentPlayer().getPlayerAttributes()), 50, 655);
				font.getData().setScale(1);
				font.setColor(Color.WHITE);

				font.draw(hud, "" + Methods.convertNum((int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "CurrentHp").getCurrentHp(getCurrentPlayer().getPlayerAttributes())) + " / " + Methods.convertNum((int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "MaxHp").getMaxHp(getCurrentPlayer().getPlayerAttributes())), 136, 759);
				font.draw(hud, "" + Methods.convertNum((int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "CurrentExp").getCurrExp(getCurrentPlayer().getPlayerAttributes())) + " / " + Methods.convertNum((int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "MaxExp").getMaxExp(getCurrentPlayer().getPlayerAttributes())), 148, 722);
				font.draw(hud, "" + Methods.convertNum((int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "CurrentMana").getCurrMana(getCurrentPlayer().getPlayerAttributes())) + " / " + Methods.convertNum((int)Attribute.getAttributeFromList(getCurrentPlayer().getPlayerAttributes(), "MaxMana").getMaxMana(getCurrentPlayer().getPlayerAttributes())), 138, 684);

				getCurrentPlayer().getInventory().render();
				hud.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);

				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				if (Objects.nonNull(isMouseOverItem())) {
					if (!getCurrentPlayer().getInventory().getItems().contains(isMouseOverItem())) {
						GlyphLayout layout = new GlyphLayout();
						layout.setText(Constants.DEFAULT_FONT, isMouseOverItem().getDescription());
						ItemHoverManager.renderBackground(76 + isMouseOverItem().getAttributes().size()*12, (layout.width > 240) ? (int) layout.width : 240, isMouseOverItem().getRarity());
					}
				}

				if (Objects.nonNull(isMouseOverItemTab())) {
					if (!StageManager.getAllEntityOfType(EntityType.ITEM).contains(isMouseOverItemTab())) {
						GlyphLayout layout = new GlyphLayout();
						if (Objects.nonNull(isMouseOverItemTab().getDescription())) {
							layout.setText(Constants.DEFAULT_FONT, isMouseOverItemTab().getDescription());
						}

						if (Objects.nonNull(layout) && Objects.nonNull(layout.width)) {
							if (Objects.nonNull(Objects.requireNonNull(isMouseOverItemTab()).getAttributes())) {
								ItemHoverManager.renderBackground(76 + Objects.requireNonNull(isMouseOverItemTab()).getAttributes().size()*12, (layout.width > 240) ? (int) layout.width : 240, isMouseOverItemTab().getRarity());
							}
						}
					}
				}
				shapeRenderer.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);

				hud.begin();
				if (Objects.nonNull(isMouseOverItem())) {
					Item item = isMouseOverItem();

					if (!getCurrentPlayer().getInventory().getItems().contains(item)) {
						ItemHoverManager.renderItemHover(item);
					}
				}

				if (Objects.nonNull(isMouseOverItemTab())) {
					Item item = isMouseOverItemTab();

					if (!StageManager.getAllEntityOfType(EntityType.ITEM).contains(item)) {
						ItemHoverManager.renderItemHover(item);
					}
				}
				hud.end();
			} else {
				if (Objects.nonNull(StageManager.getCurrentStage()) && StageManager.getCurrentStage() == Stages.LOGIN.getStage()) {
					loginStage.render(hud);
				}
			}
		} else {
			//Loading
			hud.begin();
			UI.LOADING.getImage().draw(hud, 100);
			hud.end();
		}

		StageManager.getCurrentStage().act();
	}
	
	@Override
	public void dispose () {
		//PlayerManager.savePlayer();
		batch.dispose();
		hud.dispose();

		if (Objects.nonNull(SoundManager.getCurrentSound())) {
			SoundManager.getCurrentSound().dispose();
		}

		SoundManager.getBackGroundSound().dispose();

		if (Objects.nonNull(StageManager.getCurrentStage())) {
            StageManager.getCurrentStage().dispose();
		}

		Gdx.app.postRunnable(PlayerManager::savePlayer);
	}
}
