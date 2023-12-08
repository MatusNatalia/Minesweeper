package ru.cft.focus.client.view;

import ru.cft.focus.controller.Controller;
import ru.cft.focus.model.Cell;
import ru.cft.focus.model.GameType;
import ru.cft.focus.model.ModelListenerRecorder;
import ru.cft.focus.model.events.GameOverEvent;
import ru.cft.focus.model.listeners.BombsChangeListener;
import ru.cft.focus.model.listeners.CellChangeListener;
import ru.cft.focus.model.listeners.GameOverListener;
import ru.cft.focus.model.events.CellChangedEvent;
import ru.cft.focus.model.events.NewGameStartedEvent;
import ru.cft.focus.model.events.NumOfBombsChangedEvent;
import ru.cft.focus.model.listeners.NewGameListener;
import ru.cft.focus.records.GameRecordTable;
import ru.cft.focus.timer.GameTimer;
import ru.cft.focus.timer.TimerListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class View implements NewGameListener, GameOverListener, CellChangeListener, BombsChangeListener, TimerListener {
    private static final GameType DEFAULT_GAME_TYPE = GameType.NOVICE;

    private final MainWindow mainWindow;
    private final LoseWindow loseWindow;
    private final WinWindow winWindow;
    private final RecordsWindow recordsWindow;
    private final HighScoresWindow highScoresWindow;
    private final GameRecordTable gameRecordTable;
    private GameType currentGameType = DEFAULT_GAME_TYPE;

    public View(ModelListenerRecorder model, Controller controller, GameTimer gameTimer, GameRecordTable gameRecordTable) {
        model.addNewGameListener(this);
        model.addCellChangeListener(this);
        model.addGameOverListener(this);
        model.addBombsChangeListener(this);

        gameTimer.addListener(this);

        this.gameRecordTable = gameRecordTable;

        mainWindow = new MainWindow();

        SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
        settingsWindow.setGameTypeListener(gameType -> {
            currentGameType = gameType;
            controller.onNewGame(gameType);
        });

        highScoresWindow = new HighScoresWindow(mainWindow, gameRecordTable);

        loseWindow = new LoseWindow(mainWindow);
        loseWindow.setNewGameListener(e -> controller.onNewGame(currentGameType));
        loseWindow.setExitListener(e -> {
            mainWindow.dispose();
            controller.onEndGame();
        });

        winWindow = new WinWindow(mainWindow);
        winWindow.setNewGameListener(e -> controller.onNewGame(currentGameType));
        winWindow.setExitListener(e -> {
            mainWindow.dispose();
            controller.onEndGame();
        });

        recordsWindow = new RecordsWindow(mainWindow);
        recordsWindow.setNameListener(name -> {
            gameRecordTable.saveRecord(currentGameType, name, mainWindow.getTimerValue());
            highScoresWindow.setRecord(currentGameType, name, mainWindow.getTimerValue());
        });

        mainWindow.setNewGameMenuAction(e -> controller.onNewGame(currentGameType));

        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));

        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));

        mainWindow.setExitMenuAction(e -> {
            mainWindow.dispose();
            controller.onEndGame();
        });

        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainWindow.dispose();
                controller.onEndGame();
            }
        });

        mainWindow.setCellListener((x, y, buttonType) -> {
            switch (buttonType) {
                case LEFT_BUTTON -> controller.onCellLeftClick(x, y);
                case RIGHT_BUTTON -> controller.onCellRightClick(x, y);
                case MIDDLE_BUTTON -> controller.onCellMiddleClick(x, y);
            }
        });

        mainWindow.createGameField(DEFAULT_GAME_TYPE.getNumOfRows(), DEFAULT_GAME_TYPE.getNumOfCols());
        mainWindow.setBombsCount(DEFAULT_GAME_TYPE.getNumOfBombs());

        controller.onNewGame(DEFAULT_GAME_TYPE);
        mainWindow.setVisible(true);
    }

    @Override
    public void handleTimeChange(int seconds) {
        mainWindow.setTimerValue(seconds);
    }

    @Override
    public void handleBombsChange(NumOfBombsChangedEvent event) {
        mainWindow.setBombsCount(event.bombsLeft());
    }

    @Override
    public void handleCellChange(CellChangedEvent event) {
        Cell cell = event.cell();
        mainWindow.setCellImage(cell.getX(), cell.getY(), CellToImageConverter.convertCellToImage(cell));
    }

    @Override
    public void handleGameOver(GameOverEvent event) {
        if (!event.win()) {
            loseWindow.setVisible(true);
        } else {
            if (gameRecordTable.checkIfRecord(currentGameType, mainWindow.getTimerValue())) {
                recordsWindow.setVisible(true);
            }
            winWindow.setVisible(true);
        }
    }

    @Override
    public void handleNewGame(NewGameStartedEvent event) {
        mainWindow.createGameField(event.numOfRows(), event.numOfCols());
    }

}
