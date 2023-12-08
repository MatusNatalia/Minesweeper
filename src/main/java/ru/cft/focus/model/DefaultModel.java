package ru.cft.focus.model;

import ru.cft.focus.model.events.*;
import ru.cft.focus.model.listeners.*;

import java.util.*;

public class DefaultModel implements Model, ModelListenerRecorder, ModelListenerNotifier {
    private final Map<EventType, List<ModelListener>> listeners = new HashMap<>();
    private Cell[][] cells;
    private int rows;
    private int cols;
    private int numberOfBombs;
    private int bombsLeft;
    private int openedCells;
    private boolean isFieldInitialized;
    private boolean newGameStarted;

    @Override
    public void startNewGame(GameType gameType) {
        rows = gameType.getNumOfRows();
        cols = gameType.getNumOfCols();
        numberOfBombs = gameType.getNumOfBombs();
        bombsLeft = numberOfBombs;
        openedCells = 0;
        cells = new Cell[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
        isFieldInitialized = false;
        newGameStarted = true;
        notifyNewGameListeners(rows, cols);
        notifyBombsChangeListeners(bombsLeft);
    }

    private void initializeField(int openedX, int openedY) {
        setBombs(numberOfBombs, openedX, openedY);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!cells[row][col].isMined()) {
                    countBombsAround(row, col);
                }
            }
        }
    }

    private void setBombs(int bombsCount, int openedX, int openedY) {
        Random rand = new Random();
        int x, y;
        for (int i = 0; i < bombsCount; i++) {
            do {
                x = rand.nextInt(rows);
                y = rand.nextInt(cols);
                if (!cells[x][y].isMined() && cells[x][y].getCellState() != CellState.OPENED
                        && !isAroundOpenedCell(x, y, openedX, openedY)) {
                    cells[x][y].setMine();
                    break;
                }
            } while (true);
        }
    }

    private boolean isAroundOpenedCell(int x, int y, int openedX, int openedY) {
        return Math.abs(x - openedX) == 1 || Math.abs(y - openedY) == 1;
    }

    private void countBombsAround(int x, int y) {
        int bombsAround = 0;
        for (int i = Math.max(0, x - 1); i <= Math.min(x + 1, rows - 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(y + 1, cols - 1); j++) {
                if (i != x || j != y) {
                    if (cells[i][j].isMined()) {
                        bombsAround++;
                    }
                }
            }
        }
        cells[x][y].setBombsAround(bombsAround);
    }

    @Override
    public void openCell(int x, int y) {
        if (cells[x][y].getCellState() == CellState.CLOSED) {
            if (cells[x][y].isMined()) {
                openField();
                notifyBombsChangeListeners(0);
                notifyGameOverListeners(false);
            } else {
                cells[x][y].setCellState(CellState.OPENED);
                openedCells++;
                if (!isFieldInitialized) {
                    initializeField(x, y);
                    notifyFirstStepListeners();
                    isFieldInitialized = true;
                    newGameStarted = false;
                }
                if (cells[x][y].getBombsAround() == 0) {
                    openAdjacentCells(x, y);
                }
                notifyCellChangeListeners(cells[x][y]);
                if (openedCells == rows * cols - numberOfBombs) {
                    openField();
                    notifyBombsChangeListeners(0);
                    notifyGameOverListeners(true);
                }
            }
        }
    }

    private void openField() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].getCellState() != CellState.OPENED) {
                    cells[row][col].setCellState(CellState.OPENED);
                    notifyCellChangeListeners(cells[row][col]);
                }
            }
        }
    }

    private void openAdjacentCells(int x, int y) {
        loop:
        for (int i = Math.max(0, x - 1); i <= Math.min(x + 1, rows - 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(y + 1, cols - 1); j++) {
                if (i != x || j != y) {
                    if (newGameStarted) {
                        break loop;
                    }
                    openCell(i, j);
                }
            }
        }
    }

    @Override
    public void markCell(int x, int y) {
        switch (cells[x][y].getCellState()) {
            case CLOSED -> {
                if (bombsLeft > 0) {
                    cells[x][y].setCellState(CellState.MARKED);
                    notifyCellChangeListeners(cells[x][y]);
                    notifyBombsChangeListeners(--bombsLeft);
                }
            }
            case MARKED -> {
                cells[x][y].setCellState(CellState.CLOSED);
                notifyCellChangeListeners(cells[x][y]);
                notifyBombsChangeListeners(++bombsLeft);
            }
        }
    }

    @Override
    public void openCellAndAround(int x, int y) {
        if (cells[x][y].getCellState() == CellState.OPENED) {
            int flagsAround = 0;
            for (int i = Math.max(0, x - 1); i <= Math.min(x + 1, rows - 1); i++) {
                for (int j = Math.max(0, y - 1); j <= Math.min(y + 1, cols - 1); j++) {
                    if (i != x || j != y) {
                        if (cells[i][j].getCellState() == CellState.MARKED) {
                            flagsAround++;
                        }
                    }
                }
            }
            if (flagsAround == cells[x][y].getBombsAround()) {
                openAdjacentCells(x, y);
            }
        }
    }

    @Override
    public void finishGame() {
        notifyGameFinishListeners();
    }

    @Override
    public void addNewGameListener(NewGameListener listener) {
        listeners.computeIfAbsent(EventType.NEW_GAME, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public void addFirstStepListener(FirstStepListener listener) {
        listeners.computeIfAbsent(EventType.FIRST_STEP, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public void addCellChangeListener(CellChangeListener listener) {
        listeners.computeIfAbsent(EventType.CELL_CHANGE, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public void addBombsChangeListener(BombsChangeListener listener) {
        listeners.computeIfAbsent(EventType.BOMBS_CHANGE, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public void addGameOverListener(GameOverListener listener) {
        listeners.computeIfAbsent(EventType.GAME_OVER, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public void addGameFinishListener(GameFinishListener listener) {
        listeners.computeIfAbsent(EventType.GAME_FINISH, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public void notifyNewGameListeners(int rows, int cols) {
        var event = new NewGameStartedEvent(rows, cols);
        listeners.getOrDefault(EventType.NEW_GAME, Collections.emptyList())
                .forEach(listener -> {
                    ((NewGameListener) listener).handleNewGame(event);
                });
    }

    @Override
    public void notifyFirstStepListeners() {
        listeners.getOrDefault(EventType.FIRST_STEP, Collections.emptyList())
                .forEach(listener -> {
                    ((FirstStepListener) listener).handleFirstStep();
                });
    }

    @Override
    public void notifyCellChangeListeners(Cell cell) {
        var event = new CellChangedEvent(cell);
        listeners.getOrDefault(EventType.CELL_CHANGE, Collections.emptyList())
                .forEach(listener -> {
                    ((CellChangeListener) listener).handleCellChange(event);
                });
    }

    @Override
    public void notifyBombsChangeListeners(int bombsLeft) {
        var event = new NumOfBombsChangedEvent(bombsLeft);
        listeners.getOrDefault(EventType.BOMBS_CHANGE, Collections.emptyList())
                .forEach(listener -> {
                    ((BombsChangeListener) listener).handleBombsChange(event);
                });
    }

    @Override
    public void notifyGameOverListeners(boolean win) {
        var event = new GameOverEvent(win);
        listeners.getOrDefault(EventType.GAME_OVER, Collections.emptyList())
                .forEach(listener -> {
                    ((GameOverListener) listener).handleGameOver(event);
                });
    }

    @Override
    public void notifyGameFinishListeners() {
        listeners.getOrDefault(EventType.GAME_FINISH, Collections.emptyList())
                .forEach(listener -> {
                    ((GameFinishListener) listener).handleGameFinish();
                });
    }
}
