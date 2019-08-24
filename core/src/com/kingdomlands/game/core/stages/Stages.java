package com.kingdomlands.game.core.stages;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by David K on Mar, 2019
 */
public enum Stages {
    ADVENTURE_MAP ("Adventure map", new Stage(StageManager.getViewPort()), new TmxMapLoader().load("maps/main.tmx")),
    TOWN ("Town map", new Stage(StageManager.getViewPort()), new TmxMapLoader().load("maps/town.tmx")),
    LOGIN("Login screen", new Stage(StageManager.getViewPort()), null);


    private final String name;
    private final Stage stage;
    private final TiledMap tiledMap;

    Stages(String name, Stage stage, TiledMap tiledMap) {
        this.name = name;
        this.stage = stage;
        this.tiledMap = tiledMap;
    }

    public String getName() {
        return name;
    }

    public Stage getStage() {
        return stage;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
