package ru.cft.focus.timer;

import ru.cft.focus.model.ModelListenerRecorder;
import ru.cft.focus.model.events.GameOverEvent;
import ru.cft.focus.model.events.NewGameStartedEvent;
import ru.cft.focus.model.listeners.FirstStepListener;
import ru.cft.focus.model.listeners.GameFinishListener;
import ru.cft.focus.model.listeners.GameOverListener;
import ru.cft.focus.model.listeners.NewGameListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GameTimer implements NewGameListener, GameOverListener, GameFinishListener, FirstStepListener {
    private static final long DELAY = 1000;
    private final List<TimerListener> listeners = new ArrayList<>();
    private final Timer timer;

    private final AtomicInteger seconds = new AtomicInteger(0);
    private final AtomicBoolean pause = new AtomicBoolean(true);

    public GameTimer(ModelListenerRecorder model) {
        model.addNewGameListener(this);
        model.addGameOverListener(this);
        model.addGameFinishListener(this);
        model.addFirstStepListener(this);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!pause.get()) {
                    notifyListeners(seconds.incrementAndGet());
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, DELAY, DELAY);
    }

    private void startGameTimer() {
        seconds.set(0);
        pause.set(false);
    }

    private void pauseGameTimer() {
        pause.set(true);
    }

    @Override
    public void handleNewGame(NewGameStartedEvent event) {
        pauseGameTimer();
    }

    @Override
    public void handleFirstStep() {
        startGameTimer();
    }

    @Override
    public void handleGameOver(GameOverEvent event) {
        pauseGameTimer();
    }

    @Override
    public void handleGameFinish() {
        timer.cancel();
    }

    public void addListener(TimerListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners(int seconds) {
        for (TimerListener listener : listeners) {
            listener.handleTimeChange(seconds);
        }
    }

}
