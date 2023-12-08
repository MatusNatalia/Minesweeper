package ru.cft.focus.model.listeners;

import ru.cft.focus.model.events.GameOverEvent;

public interface GameOverListener extends ModelListener {
    void handleGameOver(GameOverEvent event);
}
