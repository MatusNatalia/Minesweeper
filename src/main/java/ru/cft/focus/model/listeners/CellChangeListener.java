package ru.cft.focus.model.listeners;

import ru.cft.focus.model.events.CellChangedEvent;

public interface CellChangeListener extends ModelListener {
    void handleCellChange(CellChangedEvent event);
}
