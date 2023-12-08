package ru.cft.focus.model;

import ru.cft.focus.model.listeners.*;

public interface ModelListenerRecorder {
    void addNewGameListener(NewGameListener listener);

    void addFirstStepListener(FirstStepListener listener);

    void addCellChangeListener(CellChangeListener listener);

    void addBombsChangeListener(BombsChangeListener listener);

    void addGameOverListener(GameOverListener listener);

    void addGameFinishListener(GameFinishListener listener);
}
