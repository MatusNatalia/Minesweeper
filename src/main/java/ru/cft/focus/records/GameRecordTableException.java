package ru.cft.focus.records;

public class GameRecordTableException extends Exception {
    private static final String ERROR_MSG = "GameRecordTableException: ";
    private final String message;

    public GameRecordTableException(String message) {
        this.message = ERROR_MSG + message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
