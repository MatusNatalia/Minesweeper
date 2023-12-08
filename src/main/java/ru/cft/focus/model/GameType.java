package ru.cft.focus.model;

public enum GameType {
    NOVICE(9, 9, 10),
    MEDIUM(16, 16, 40),
    EXPERT(16, 30, 99);

    private final int numOfRows;
    private final int numOfCols;
    private final int numOfBombs;

    GameType(int numOfRows, int numOfCols, int numOfBombs) {
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
        this.numOfBombs = numOfBombs;
    }

    public static GameType getGameTypeByName(String name) {
        switch (name) {
            case "NOVICE" -> {
                return NOVICE;
            }
            case "MEDIUM" -> {
                return MEDIUM;
            }
            case "EXPERT" -> {
                return EXPERT;
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public int getNumOfCols() {
        return numOfCols;
    }

    public int getNumOfBombs() {
        return numOfBombs;
    }
}
