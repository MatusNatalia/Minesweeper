package ru.cft.focus.controller;

import ru.cft.focus.model.GameType;
import ru.cft.focus.model.Model;

public class Controller {

    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void onNewGame(GameType gameType) {
        model.startNewGame(gameType);
    }

    public void onCellLeftClick(int x, int y) {
        model.openCell(x, y);
    }

    public void onCellRightClick(int x, int y) {
        model.markCell(x, y);
    }

    public void onCellMiddleClick(int x, int y) {
        model.openCellAndAround(x, y);
    }

    public void onEndGame() {
        model.finishGame();
    }
}
