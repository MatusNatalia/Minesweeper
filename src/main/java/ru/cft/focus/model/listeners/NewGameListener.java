package ru.cft.focus.model.listeners;

import ru.cft.focus.model.events.NewGameStartedEvent;

public interface NewGameListener extends ModelListener {
    void handleNewGame(NewGameStartedEvent event);
}
