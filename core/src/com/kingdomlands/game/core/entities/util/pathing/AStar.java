package com.kingdomlands.game.core.entities.util.pathing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.player.PlayerManager;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.stages.StageManager;
import com.kingdomlands.game.core.stages.StageRender;
import com.kingdomlands.game.core.stages.Stages;

import java.util.*;

/**
 * Created by David K on Mar, 2019
 */
public class AStar {
    //costs for diagonal and vertical/horizontal moves
    public static final int DIAGONAL_COST = 14, V_H_COST = 10, ENTITY_COST = 100;

    //cells of our grid
    private Cell[][] grid;

    //the set of nodes to be evaluated, lowest cost cells in first
    private PriorityQueue<Cell> openCells;

    //the set of nodes already evaluated
    private boolean[][] closedCells;

    //start cell
    private int startX, startY;

    //end cell
    private int endX, endY;

    public AStar(Vector2 start, Vector2 end, Entity entity, Entity clicked) {
        grid = new Cell[250][250];
        closedCells = new boolean[250][250];
        openCells = new PriorityQueue<>(Comparator.comparingInt(Cell::getFinalCost));

        //init heuristic
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].setHeuristicCost(Math.abs(i - (int)end.y) + Math.abs(j - (int)end.x));
                grid[i][j].setSolution(false);
            }
        }

        startCell((int)(start.y/64), (int)(start.x/64));
        endCell((int)(end.y/64), (int)(end.x/64));

        grid[startY][startX].setFinalCost(0);

        for (Vector2 e : StageManager.getAllEntityPositions(entity, clicked)) {
            addBlockOnCell((int)e.x, (int)e.y);
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

        process();
    }

    public void addBlockOnCell(int x, int y) {
        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        grid[y][x] = null;
    }

    public void startCell(int x, int y) {
        startX = x;
        startY = y;
    }

    public void endCell(int x, int y) {
        endX = x;
        endY = y;

        if (x < 0) {
            endX = 0;
        }

        if (y < 0) {
            endY = 0;
        }
    }

    public void updateCostIfNeeded(Cell current, Cell t, int cost) {
        if (Objects.isNull(t) || closedCells[t.getX()][t.getY()]) {
            return;
        }

        int tFinalCost = t.getHeuristicCost() + cost;
        boolean isOpen = openCells.contains(t);

        if (!isOpen || tFinalCost < t.getFinalCost()) {
            t.setFinalCost(tFinalCost);
            t.setParent(current);

            if (!isOpen)
                openCells.add(t);
        }
    }

    public void process() {
        if (Objects.nonNull(grid[startX][startY])) {
            openCells.add(grid[startX][startY]);
        }

        Cell current;

        while (true) {
            current = openCells.poll();

            if (Objects.isNull(current)) {
                break;
            }

            closedCells[current.getX()][current.getY()] = true;

            if (current.equals(grid[endX][endY])) {
                return;
            }

            Cell t;

            if (current.getX() - 1 >= 0) {
                t = grid[current.getX() - 1][current.getY()];
                updateCostIfNeeded(current, t, current.getFinalCost() + V_H_COST);

                //Diag
                /*if (current.getY() - 1 >= 0) {
                    t = grid[current.getX() - 1][current.getY() - 1];
                    updateCostIfNeeded(current, t, current.getFinalCost() + DIAGONAL_COST);
                }

                if (current.getY() + 1 < grid[0].length) {
                    t = grid[current.getX() - 1][current.getY() + 1];
                    updateCostIfNeeded(current, t, current.getFinalCost() + DIAGONAL_COST);
                }*/
            }

            if (current.getY() - 1 >= 0) {
                t = grid[current.getX()][current.getY() - 1];
                updateCostIfNeeded(current, t, current.getFinalCost() + V_H_COST);
            }

            if (current.getY() + 1 < grid[0].length) {
                t = grid[current.getX()][current.getY() + 1];
                updateCostIfNeeded(current, t, current.getFinalCost() + V_H_COST);
            }

            if (current.getX() + 1 < grid.length) {
                t = grid[current.getX() + 1][current.getY()];
                updateCostIfNeeded(current, t, current.getFinalCost() + V_H_COST);

                //Diag
                /*if (current.getY() - 1 >= 0) {
                    t = grid[current.getX() + 1][current.getY() - 1];
                    updateCostIfNeeded(current, t, current.getFinalCost() + DIAGONAL_COST);
                }

                if (current.getY() + 1 > grid[0].length) {
                    t = grid[current.getX() + 1][current.getY() + 1];
                    updateCostIfNeeded(current, t, current.getFinalCost() + DIAGONAL_COST);
                }*/
            }
        }
    }


    public Vector2 getNextNode() {
        List<Vector2> path = new ArrayList<>();

        if (closedCells[endX][endY]) {
            //We track back the path
            Cell current = grid[endX][endY];
            grid[current.getX()][current.getY()].setSolution(true);

            while (current.getParent() != null) {
                path.add(new Vector2(current.getParent().getX(), current.getParent().getY()));
                grid[current.getParent().getX()][current.getParent().getY()].setSolution(true);
                current = current.getParent();
            }
        } else {
            return null;
        }

        if (path.size() != 0) {
            Collections.reverse(path);

            if (path.size() == 1) {

                if (Methods.getDistanceVector(path.get(0), new Vector2(endX, endY)) > 1) {
                    return path.get(0);
                } else {
                    return new Vector2(endX, endY);
                }
            } else {
                Vector2 next = path.get(1);
                path.remove(0);

                return next;
            }
        } else {
            return null;
        }
    }

    public List<Vector2> getPath() {
        List<Vector2> path = new ArrayList<>();

        if (closedCells[endX][endY]) {
            Cell current = grid[endX][endY];
            grid[current.getX()][current.getY()].setSolution(true);

            while (current.getParent() != null) {
                path.add(new Vector2(current.getParent().getX(), current.getParent().getY()));
                grid[current.getParent().getX()][current.getParent().getY()].setSolution(true);
                current = current.getParent();
            }
        } else {
            return null;
        }

        return path;
    }
}
