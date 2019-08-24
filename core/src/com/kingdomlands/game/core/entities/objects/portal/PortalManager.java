package com.kingdomlands.game.core.entities.objects.portal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.monster.Monster;
import com.kingdomlands.game.core.entities.npc.Npc;
import com.kingdomlands.game.core.entities.objects.GameObject;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.chat.ChatManager;
import com.kingdomlands.game.core.entities.player.ui.UIManager;
import com.kingdomlands.game.core.entities.util.Attribute;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.entities.util.groups.Group;
import com.kingdomlands.game.core.entities.util.groups.GroupManager;
import com.kingdomlands.game.core.stages.StageManager;
import com.kingdomlands.game.core.stages.StageRender;
import com.kingdomlands.game.core.stages.Stages;

import java.util.Objects;

public class PortalManager {
    private static ScrollPane scrollPane, groupScrollPane;
    private static TextField portalTitle, realmTitle, levelLabel;
    private static TextArea attributesArea;
    private static TextButton withdrawButton, closeButton;
    private static Table root, rootGroup;

    private static boolean portalOpen = false;
    private static Realms selectedRealm = null;

    private static final TextureRegionDrawable scrollPaneDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("widget_bgs/scrollpane_bg.png")), 0, 0, 360, 460));
    private static final TextureRegionDrawable titleDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("widget_bgs/title_bg.png")), 0, 0, 400, 40));

    public static void openPortal() {
        ContextManager.close();
        Vector2 pos = new Vector2(PlayerManager.getCurrentPlayer().getX() - 300, PlayerManager.getCurrentPlayer().getY() - 400);
        portalOpen = true;

        //Title of portal
        portalTitle = new TextField("Portal of Realms", Constants.DEFAULT_SKIN);
        portalTitle.setWidth(400);
        portalTitle.setHeight(32);
        portalTitle.setAlignment(Align.center);
        portalTitle.setDisabled(true);
        portalTitle.setPosition(pos.x, pos.y + 198 + 460);
        portalTitle.getStyle().background = scrollPaneDrawable;

        StageManager.addActor(portalTitle);

        //close portal
        closeButton = new TextButton("X", Constants.DEFAULT_SKIN);
        closeButton.setWidth(36);
        closeButton.setHeight(40);
        closeButton.setPosition(pos.x + 400, pos.y + 198 + 460);
        closeButton.setColor(Color.RED);

        closeButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closePortal();
            }
        });

        StageManager.addActor(closeButton);

        //Title of realm
        realmTitle = new TextField("", Constants.DEFAULT_SKIN);
        realmTitle.setWidth(260);
        realmTitle.setHeight(32);
        realmTitle.setAlignment(Align.center);
        realmTitle.setDisabled(true);
        realmTitle.setPosition(pos.x + 176, pos.y + 198 + 428);

        StageManager.addActor(realmTitle);

        //Level of realm
        levelLabel = new TextField("", Constants.DEFAULT_SKIN);
        levelLabel.setWidth(260);
        levelLabel.setHeight(32);
        levelLabel.setAlignment(Align.center);
        levelLabel.setDisabled(true);
        levelLabel.setPosition(pos.x + 176, pos.y + 198 + 396);

        StageManager.addActor(levelLabel);

        //Attributes of item
        attributesArea = new TextArea("", Constants.DEFAULT_SKIN);
        attributesArea.setWidth(260);
        attributesArea.setHeight(240);
        attributesArea.setAlignment(Align.center);
        attributesArea.setDisabled(true);
        attributesArea.setPosition(pos.x + 176, pos.y + 198 + 156);

        StageManager.addActor(attributesArea);

        root = new Table(Constants.DEFAULT_SKIN);

        for (Realms realm : Realms.values()) {
            if (Objects.nonNull(realm)) {
                Label label = new Label("" + realm.getName(), Constants.DEFAULT_SKIN);

                root.add(realm.getIcon());
                root.add(label);
                root.add(" | Lvl: " + realm.getLevel());
                root.row();

                label.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        attributesArea.clear();
                        attributesArea.setText("");
                        selectedRealm = realm;

                        if (Objects.nonNull(realm)) {
                            realmTitle.setText(String.valueOf(label.getText()));
                            levelLabel.setText("Level: " + realm.getLevel());
                            withdrawButton.setText("Enter Portal");
                        }

                        attributesArea.appendText(realm.getDescription());

                        if (Objects.nonNull(groupScrollPane)) {
                            groupScrollPane.remove();
                        }

                        rootGroup = new Table(Constants.DEFAULT_SKIN);

                        for (Group group : realm.getGroups()) {
                            if (Objects.nonNull(group)) {
                                Label label1 = new Label("" + group.getName(), Constants.DEFAULT_SKIN);

                                rootGroup.add(label1);
                                rootGroup.row();
                            }
                        }

                        groupScrollPane = new ScrollPane(rootGroup, Constants.DEFAULT_SKIN);
                        groupScrollPane.setPosition(pos.x + 176, pos.y + 198 + 60);
                        groupScrollPane.setHeight(100);
                        groupScrollPane.setWidth(260);
                        groupScrollPane.setFadeScrollBars(false);
                        groupScrollPane.setScrollbarsVisible(true);

                        StageManager.addActor(groupScrollPane);
                    }
                });
            }
        }

        scrollPane = new ScrollPane(root, Constants.DEFAULT_SKIN);
        scrollPane.setPosition(pos.x - 184, pos.y + 198);
        scrollPane.setHeight(460);
        scrollPane.setWidth(360);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsVisible(true);

        StageManager.addActor(scrollPane);

        withdrawButton = new TextButton("", Constants.DEFAULT_SKIN);
        withdrawButton.setWidth(260);
        withdrawButton.setHeight(60);
        withdrawButton.setPosition(pos.x + 176, pos.y + 200);
        withdrawButton.setColor(Color.RED);
        withdrawButton.setText("Enter Portal");

        withdrawButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Objects.nonNull(selectedRealm)) {
                    if ((int)Attribute.getAttributeValueFromList(PlayerManager.getCurrentPlayer().getPlayerAttributes(), "Level") >= selectedRealm.getLevel()) {
                        Player player = PlayerManager.getCurrentPlayer();
                        Methods.setLoading();

                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                StageManager.getAllEntities().forEach(e -> {
                                    if (e instanceof GameObject || e instanceof Npc || e instanceof Monster || e instanceof Item) {
                                        e.remove();
                                    }
                                });

                                StageRender.loadTiledMap(selectedRealm.getStages().getTiledMap());

                                Gdx.input.setInputProcessor(StageManager.getCurrentStage());
                                StageManager.addActor(player);
                                player.setPosition(8000, 8000);


                                GroupManager.initGenerator(selectedRealm);
                                ChatManager.init();

                                Methods.setLoading();
                            }
                        });
                    } else {
                        ChatManager.addChat("[Battle]: " + "You are not high enough level for this realm.");
                    }

                    closePortal();
                }
            }
        });

        StageManager.addActor(withdrawButton);
    }

    public static void closePortal() {
        scrollPane.remove();
        portalTitle.remove();
        realmTitle.remove();
        levelLabel.remove();
        portalTitle.remove();
        attributesArea.remove();
        withdrawButton.remove();
        closeButton.remove();
        root.remove();

        if (Objects.nonNull(groupScrollPane)) {
            groupScrollPane.remove();
        }

        if (UIManager.isInventoryOpen()) {
            UIManager.setCurrentTab(null);
        }
        ContextManager.close();

        portalOpen = false;
    }

    public static boolean isPortalOpen() {
        return portalOpen;
    }
}
