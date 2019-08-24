package com.kingdomlands.game.core.entities.util.pathing;

/**
 * Created by David K on Mar, 2019
 */
public class Cell {
    //coords
    private int x, y;

    //parent cell for path
    private Cell parent;

    //heuristic cost of the current cell
    private int heuristicCost;

    //final cost
    private int finalCost;
    /*
        G(n) the cost of the path from the start node to n
        H(n) the heuristic that estimates the cost of the cheapest path from n to the goal
     */

    private boolean solution;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Cell getParent() {
        return parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public int getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public int getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(int finalCost) {
        this.finalCost = finalCost;
    }

    public boolean isSolution() {
        return solution;
    }

    public void setSolution(boolean solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        return "[x" + x + ", y" + y + "]";
    }
}
