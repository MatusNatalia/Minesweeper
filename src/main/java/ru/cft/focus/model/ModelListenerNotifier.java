package ru.cft.focus.model;

public interface ModelListenerNotifier {
    void notifyNewGameListeners(int rows, int cols);

    void notifyFirstStepListeners();

    void notifyCellChangeListeners(Cell cell);

    void notifyBombsChangeListeners(int bombsLeft);

    void notifyGameOverListeners(boolean win);

    void notifyGameFinishListeners();
}
