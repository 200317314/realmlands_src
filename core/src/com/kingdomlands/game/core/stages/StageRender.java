package com.kingdomlands.game.core.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.player.Player;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.util.Methods;

import java.util.Objects;

public class StageRender {
    private static TiledMapRenderer tiledMapRenderer;
    private static TiledMap tiledMap;

    public static void loadTiledMap(TiledMap tiledMap) {
        if (Objects.nonNull(tiledMap)) {
            StageRender.tiledMap = tiledMap;

            tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1);
        } else {
            StageRender.tiledMap.dispose();
            tiledMapRenderer = null;
        }
    }

    public static void render() {
        if (Objects.nonNull(tiledMapRenderer)) {
            OrthographicCamera cam = (OrthographicCamera) StageManager.getCurrentStage().getCamera();
            cam.position.x = Math.round(cam.position.x);
            cam.position.y = Math.round(cam.position.y);
            cam.position.z = Math.round(cam.position.z);

            tiledMapRenderer.setView(cam);
            tiledMapRenderer.render();
        }
    }

    public static TiledMap getTiledMap() {
        return tiledMap;
    }
}
