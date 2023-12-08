package ru.cft.focus.model;

public class Cell {
    private CellState cellState = CellState.CLOSED;
    private final int x;
    private final int y;
    private boolean isMined = false;
    private int bombsAround;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public CellState getCellState() {
        return cellState;
    }

    public boolean isMined() {
        return isMined;
    }

    public int getBombsAround() {
        return bombsAround;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public void setMine() {
        isMined = true;
    }

    public void setBombsAround(int bombsAround) {
        this.bombsAround = bombsAround;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
