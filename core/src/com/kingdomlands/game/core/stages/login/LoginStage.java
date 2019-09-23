package com.kingdomlands.game.core.stages.login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Timer;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.player.chat.ChatManager;
import com.kingdomlands.game.core.entities.util.Images;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.stages.StageManager;

import java.util.Objects;

public class LoginStage {
    private TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("widget_bgs/title_bg.png")), 0, 0, 400, 40));
    private String token = "";
    private Player player = null;
    private TextField usernameField, passwordField;
    private ImageButton loginButton, registerButton;
    private Skin skin;
    private String test = "";

    private static final TextureRegionDrawable loginDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("hud/login_button.png")), 0, 0, 120, 46));
    private static final TextureRegionDrawable registerDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("hud/register_button.png")), 0, 0, 120, 46));

    public LoginStage() {
        skin = Constants.DEFAULT_SKIN;

        usernameField = new TextField("", skin);
        usernameField.setPosition(500,420);
        usernameField.setWidth(290);
        usernameField.setHeight(32);
        usernameField.setMessageText("Username");

        usernameField.getStyle().background = drawable;

        passwordField = new TextField("", skin);
        passwordField.setPosition(500,380);
        passwordField.setWidth(290);
        passwordField.setHeight(32);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('X');

        loginButton = new ImageButton(loginDrawable);
        loginButton.setPosition(500, 300);
        loginButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                loginButton.getImage().setColor(Color.RED);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
                loginButton.getImage().setColor(Color.WHITE);
            }
        });


        registerButton = new ImageButton(registerDrawable);
        registerButton.setPosition(670, 300);
        registerButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                registerButton.getImage().setColor(Color.RED);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
                registerButton.getImage().setColor(Color.WHITE);
            }
        });

        loginButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                username = username.toLowerCase();
                String password = passwordField.getText();

                if (username.isEmpty() || password.isEmpty()) {
                    promptMessage("Username or Password cannot be empty.");
                } else {
                    Methods.setLoading();
                    String loginRes = login(username, password);

                    if (!loginRes.isEmpty()) {
                        Player p = retrievePlayer(loginRes);

                        if (Objects.nonNull(p)) {
                            PlayerManager.setPlayerToken(loginRes);
                            PlayerManager.setCurrentPlayer(p);
                        }
                    }
                }

                Gdx.app.postRunnable(() -> {
                    if (Methods.getLoading()) {
                        Methods.setLoading();
                    }
                });
           }
        });

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://realmlands.com/register");
            }
        });

        StageManager.addActor(new Image(Images.LOGIN.getTexture()));
        StageManager.addActor(usernameField);
        StageManager.addActor(passwordField);
        StageManager.addActor(loginButton);
        StageManager.addActor(registerButton);
    }

    public void render(Batch batch) {
        batch.begin();
        StageManager.getCurrentStage().draw();
        batch.end();
    }

    private void promptMessage(String message) {
        Dialog dialog = new Dialog(message, skin);
        dialog.setHeight(32);
        dialog.setPosition(500, 700);
        dialog.show(StageManager.getCurrentStage());

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                dialog.remove();
            }
        }, 1);
    }

    private String login(String username, String password) {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setHeader("Content-Type", "json/application");
        //request.setUrl("http://localhost:8080/rest/players/login/" + username + "/" + password);
        request.setUrl("https://realmlands.com/rest/players/login/" + username + "/" + password);

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();
                if (!response.contains("No account")) {
                    token = response;
                    PlayerManager.setPlayerToken(token);
                    retrievePlayer(token);
                } else {
                    promptMessage("No account found.");
                }
            }

            @Override
            public void failed(Throwable t) {
                promptMessage("Failed to login, please wait and retry.");
            }

            @Override
            public void cancelled() {
                promptMessage("Failed to login, please wait and retry.");
            }
        });

        return token;
    }

    private Player retrievePlayer(String token) {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setContent(token);
        request.setHeader("Content-Type", "text/plain");
        request.setUrl("https://realmlands.com/rest/players/get/");
        //request.setUrl("http://localhost:8080/rest/players/get/");

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();
                //System.out.println("content: " + httpResponse.getResultAsString());

                if (!response.isEmpty()) {
                    JsonReader reader = new JsonReader();
                    Json json = new Json();
                    JsonValue value = reader.parse(response);
                    //player = json.fromJson(Player.class, value.getString("playerJson"));
                    PlayerManager.setPremiumDays(value.getInt("premium"));
                    PlayerManager.setRole(Integer.parseInt(value.getString("role")));

                    Gdx.app.postRunnable(() -> {
                        player = json.fromJson(Player.class, value.getString("playerJson"));

                        PlayerManager.setCurrentPlayer(player);
                        ChatManager.addChat("[Login]: " + "Welcome back " + player.getName() + ".");

                        if (PlayerManager.getRole() == 2) {
                            ChatManager.addChat("[Login]: Thank you for supporting the game, enjoy the lifetime premium.");
                        }

                        if (PlayerManager.getPremiumDays() != 0) {
                            ChatManager.addChat("[Login]: You have " + PlayerManager.getPremiumDays() + " premium days left.");
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
                promptMessage("Failed to login, please wait and retry.");
            }

            @Override
            public void cancelled() {
                promptMessage("Failed to login, please wait and retry.");
            }
        });

        return player;
    }
}
