package ru.cft.focus.model;

public interface Model {

    void startNewGame(GameType gameType);

    void openCell(int x, int y);

    void openCellAndAround(int x, int y);

    void markCell(int x, int y);

    void finishGame();

}
