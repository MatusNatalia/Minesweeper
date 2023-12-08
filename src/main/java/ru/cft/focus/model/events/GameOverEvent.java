package ru.cft.focus.model.events;

public record GameOverEvent(boolean win) implements ModelEvent {
}
