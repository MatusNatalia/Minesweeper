package ru.cft.focus.model.events;

import ru.cft.focus.model.Cell;

public record CellChangedEvent(Cell cell) implements ModelEvent {
}
