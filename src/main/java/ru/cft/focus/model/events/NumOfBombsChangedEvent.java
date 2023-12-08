package ru.cft.focus.model.events;

public record NumOfBombsChangedEvent(int bombsLeft) implements ModelEvent {
}
