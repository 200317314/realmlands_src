package com.kingdomlands.game.core.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.monster.Monster;
import com.kingdomlands.game.core.entities.monster.MonsterManager;
import com.kingdomlands.game.core.entities.util.Attribute;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.entities.util.contextmenu.ContextManager;
import com.kingdomlands.game.core.stages.StageManager;
import com.kingdomlands.game.core.stages.StageRender;
import com.kingdomlands.game.core.stages.Stages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by David K on Mar, 2019
 */
public class PlayerManager {
    private static Player currentPlayer = null;
    private static String playerToken = "";
    private static int role;
    private static int premiumDays;

    public static List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        Stage currentStage = StageManager.getCurrentStage();

        if (Objects.nonNull(currentStage)) {
            for (Actor actor : currentStage.getActors()) {
                if (Objects.nonNull(actor)) {
                    if (actor instanceof Player) {
                        players.add((Player) actor);
                    }
                }
            }
        }

        return players;
    }

    public static Collection<Player> getFilteredPlayers(Predicate<Player> predicate) {
        Collection<Player> players = new ArrayList<>();

        getAllPlayers().forEach(p -> {
            if (Objects.nonNull(p)) {
                if (predicate.test(p)) {
                    players.add(p);
                }
            }
        });

        return players;
    }

    public static Player getFilteredPlayer(Predicate<Player> predicate) {
        List<Player> players = new ArrayList<>();

        getAllPlayers().forEach(p -> {
            if (Objects.nonNull(p)) {
                if (predicate.test(p)) {
                    players.add(p);
                }
            }
        });

        return players.get(0);
    }

    public static Player getCurrentPlayer() {
        for (Player p : getAllPlayers()) {
            if (Objects.nonNull(p)) {
                if (p.getName().equals(currentPlayer.getName())) {
                    return p;
                }
            }
        }
        return null;
    }

    public static String getPlayerToken() {
        return playerToken;
    }

    public static void setPlayerToken(String token) {
        playerToken = token;
    }

    public static void setCurrentPlayer(Player currentPlayer) {
        currentPlayer.setX(7265);
        currentPlayer.setY(7800);

        PlayerManager.currentPlayer = currentPlayer;

        StageManager.setCurrentStage(Stages.TOWN.getStage());
        StageRender.loadTiledMap(Stages.TOWN.getTiledMap());

        Gdx.input.setInputProcessor(StageManager.getCurrentStage());

        StageManager.addActor(currentPlayer);
        ContextManager.init();

        if (Methods.getLoading()) {
            Methods.setLoading();
        }
    }

    public static Player getNearestPlayer(Entity entity) {
        List<Player> players = getAllPlayers();
        Player nearest = null;

        for (Player p : players) {
            if (Objects.nonNull(p)) {
                if (!Objects.nonNull(nearest)) {
                    nearest = p;
                } else {
                    double nearestP = Math.hypot(entity.getX() - nearest.getX(), entity.getY() - nearest.getY());
                    double next = Math.hypot(entity.getX() - p.getX(), entity.getY() - p.getY());

                    if (next < nearestP) {
                        nearest = p;
                    }
                }
            }
        }

        return nearest;
    }

    public static void savePlayer() {
        Gdx.app.postRunnable(() -> {
            Json json = new Json();
            Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);

            Attribute.setAttributeValueFromList(getCurrentPlayer().getPlayerAttributes(), "CurrentExp", (int)(Attribute.getAttributeValueFromList(getCurrentPlayer().getPlayerAttributes(), "CurrentExp")));

            request.setContent(playerToken + ":" + json.toJson(getCurrentPlayer()));
            request.setUrl("https://realmlands.com/rest/players/update/");
            request.setHeader("Content-Type", "text/plain");
            //request.setUrl("http://localhost:8080/rest/players/update/");

            Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse (Net.HttpResponse httpResponse) {
                    //System.out.println(httpResponse.getResultAsString());
                    //System.out.println("Successfully saved player.");
                }

                @Override
                public void failed(Throwable t) {
                    System.out.println("Failed to save player [E1]");
                }

                @Override
                public void cancelled() {
                    System.out.println("Failed to save player [E2]");
                }
            });
        });
    }

    public static int getRole() {
        return role;
    }

    public static void setRole(int role) {
        PlayerManager.role = role;
    }

    public static String getRoleName() {
        if (getRole() == 2) {
            return "Founder";
        } else if (getRole() == 3) {
            return "Moderator";
        } else if (getRole() == 4) {
            return "Admin";
        } else if (getRole() == 5) {
            return "Owner";
        }

        return "Player";
    }

    public static int getPremiumDays() {
        return premiumDays;
    }

    public static void setPremiumDays(int premiumDays) {
        PlayerManager.premiumDays = premiumDays;
    }

    public static boolean isPremium() {
        return premiumDays > 0;
    }

    public static boolean isFounder() {
        return getRole() == 2;
    }

    public static double getExpModifier() {
        if (isFounder()) {
            return 1.20;
        } else if (isPremium()) {
            return 1.15;
        }

        return 1;
    }

    public static int getMaxBankSlots() {
        if (isFounder()) {
            return 1000;
        } else if (isPremium()) {
            return 750;
        }

        return 500;
    }

    public static boolean onScreenFromPlayer(Entity entity) {
        if (Objects.nonNull(getCurrentPlayer()) && Objects.nonNull(entity)) {
            if (Methods.getDistanceVector(getCurrentPlayer().getPosition(), entity.getPosition())/64 <= 12) {
                return true;
            }
        }

        return false;
    }
}
