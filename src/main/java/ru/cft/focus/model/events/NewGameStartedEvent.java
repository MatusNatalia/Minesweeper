package ru.cft.focus.model.events;

public record NewGameStartedEvent(int numOfRows, int numOfCols) implements ModelEvent {
}
