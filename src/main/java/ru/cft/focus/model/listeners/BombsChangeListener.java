package ru.cft.focus.model.listeners;

import ru.cft.focus.model.events.NumOfBombsChangedEvent;

public interface BombsChangeListener extends ModelListener {
    void handleBombsChange(NumOfBombsChangedEvent event);
}
