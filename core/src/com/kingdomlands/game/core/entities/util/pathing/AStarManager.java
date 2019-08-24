package com.kingdomlands.game.core.entities.util.pathing;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.kingdomlands.game.core.stages.StageRender;

import java.util.Objects;

public class AStarManager {
    private static Cell[][] grid;

    public static void initGrid() {
        grid = new Cell[250][250];

        //init heuristic
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].setHeuristicCost(Math.abs(i) + Math.abs(j));
                grid[i][j].setSolution(false);
            }
        }

        if (Objects.nonNull(StageRender.getTiledMap())
                && Objects.nonNull(StageRender.getTiledMap().getLayers().get("Obstacles"))) {

            MapObjects objects = StageRender.getTiledMap().getLayers().get("Obstacles").getObjects();
            for (MapObject mo : objects) {
                if (mo instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) mo).getRectangle();
                    addBlockOnCell((int)rect.getX()/64, (int)(rect.getY())/64);
                }
            }
        }
    }

    private static void addBlockOnCell(int x, int y) {
        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        grid[y][x] = null;
    }

    public static Cell[][] getGrid() {
        return grid;
    }
}
