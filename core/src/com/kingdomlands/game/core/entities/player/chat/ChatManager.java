package com.kingdomlands.game.core.entities.player.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.util.DamageType;
import com.kingdomlands.game.core.stages.StageManager;

import java.util.*;
import java.util.List;

public class ChatManager {
    private static Table container, table;
    private static ScrollPane scrollPane;
    private static List<String> chat = new ArrayList<>();
    private static List<String> chatLog = new ArrayList<>();
    private static Stage stage;
    private static TextField textField;
    private static TextButton button;

    private static final TextureRegionDrawable scrollPaneDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("widget_bgs/scrollpane_bg.png")), 0, 0, 360, 460));
    private static final TextureRegionDrawable titleDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("widget_bgs/title_bg.png")), 0, 0, 400, 40));

    public static void addChat(String cha) {
        //if (cha.charAt(0) == '/') {
            if (isCommand(cha)) {
                if (canUseCmd(cha)) {

                } else {
                    ChatManager.addChat("[System]: " + "You do not have permission for this command.");
                }
            }
        //}
        chat.add(cha);

            if (Objects.nonNull(scrollPane)) {
                scrollPane.setScrollPercentY(100);
            }
    }

    public static void init() {
        if (Objects.nonNull(scrollPane)) {
            scrollPane.remove();
        }

        if (Objects.nonNull(textField)) {
            textField.remove();
        }

        if (Objects.nonNull(button)) {
            button.remove();
        }

        container = new Table(Constants.DEFAULT_SKIN);
        table = new Table();
        stage = new Stage();
        scrollPane = new ScrollPane(table, Constants.DEFAULT_SKIN);
        scrollPane.setScrollbarsVisible(true);
        scrollPane.setFadeScrollBars(false);
        scrollPane.layout();

        stage.addActor(container);
        container.setFillParent(false);

        button = new TextButton("Send", Constants.DEFAULT_SKIN);

        textField = new TextField("", Constants.DEFAULT_SKIN);

        StageManager.addActor(scrollPane);
        StageManager.addActor(textField);
        StageManager.addActor(button);
    }

    public static void draw(Batch batch, float alpha) {
        if (Objects.isNull(container)) {
            init();
        } else {
            scrollPane.setPosition(PlayerManager.getCurrentPlayer().getX() - 636, PlayerManager.getCurrentPlayer().getY() - 366);
            scrollPane.setWidth(380);
            scrollPane.setHeight(160);
            scrollPane.getStyle().background = scrollPaneDrawable;

            textField.setPosition(PlayerManager.getCurrentPlayer().getX() - 636, PlayerManager.getCurrentPlayer().getY() - 400);
            textField.setWidth(300);
            textField.setHeight(32);
            textField.getStyle().background = titleDrawable;

            button.setPosition(PlayerManager.getCurrentPlayer().getX() - 336, PlayerManager.getCurrentPlayer().getY() - 400);
            button.setWidth(80);
            button.setHeight(32);

            table.clear();
            chat.forEach(s -> {
                Label label = new Label(s, Constants.DEFAULT_SKIN);
                label.setColor(DamageType.getColor(s));
                label.setAlignment(Align.left);
                table.add(label).width(360);
                table.row();
            });

            scrollPane.act(Gdx.graphics.getDeltaTime());

            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!textField.getText().isEmpty()) {
                        if (PlayerManager.getRole() != 1) {
                            addChat("[" + PlayerManager.getCurrentPlayer().getName() + " - " + PlayerManager.getRoleName() +"]: " + textField.getText());
                        } else {
                            addChat("[" + PlayerManager.getCurrentPlayer().getName() + "]: " + textField.getText());
                        }

                        textField.setText("");
                        scrollPane.setScrollPercentY(100);
                    }
                }
            });

            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                if (!textField.getText().isEmpty()) {
                    if (PlayerManager.getRole() != 1) {
                        addChat("[" + PlayerManager.getCurrentPlayer().getName() + " - " + PlayerManager.getRoleName() +"]: " + textField.getText());
                    } else {
                        addChat("[" + PlayerManager.getCurrentPlayer().getName() + "]: " + textField.getText());
                    }

                    textField.setText("");
                    scrollPane.setScrollPercentY(100);
                }
            }

            //container.setPosition(PlayerManager.getCurrentPlayer().getX() - 400, PlayerManager.getCurrentPlayer().getY() - 200);
        }
    }

    private static boolean isCommand(String cmd) {
        for (Commands c : Commands.values()) {
            if (cmd.contains(c.getCommand())) {
                return true;
            }
        }

        return false;
    }

    private static boolean canUseCmd(String cmd) {
        for (Commands c : Commands.values()) {
            if (cmd.contains(c.getCommand())) {
                if (Arrays.asList(c.getRoles()).contains(PlayerManager.getRole())) {
                    return true;
                }
            }
        }

        return false;
    }
}
