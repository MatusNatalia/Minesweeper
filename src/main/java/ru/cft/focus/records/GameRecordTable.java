package ru.cft.focus.records;

import ru.cft.focus.model.GameType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class GameRecordTable {
    private static final String RECORDS_FILEPATH = new File(".").getAbsolutePath() + "\\scores.txt";
    private static final String ERROR_MESSAGE = "Error while working with records file. " +
            "Records may contain incorrect data. Cause: ";
    private static final String DEFAULT_NAME = "Unknown";
    private static final int DEFAULT_TIME = 999;
    private final Map<GameType, GameRecord> records = new HashMap<>();

    public GameRecordTable() throws GameRecordTableException {
        File recordsFile = new File(RECORDS_FILEPATH);
        try {
            recordsFile.createNewFile();
        } catch (IOException e) {
            throw new GameRecordTableException(e.getMessage());
        }
        try (Scanner scanner = new Scanner(recordsFile)) {
            while (scanner.hasNextLine()) {
                String[] record = scanner.nextLine().split(" ");
                if (record.length != 3) {
                    System.err.println(ERROR_MESSAGE + "invalid record format");
                } else {
                    GameType gameType = GameType.getGameTypeByName(record[0]);
                    records.put(gameType, new GameRecord(record[1], Integer.parseInt(record[2])));
                }
            }
        } catch (Exception e) {
            System.err.println(ERROR_MESSAGE + e.getMessage());
        }
    }

    public boolean checkIfRecord(GameType gameType, int gameTime) {
        GameRecord gameRecord = getRecordByGameType(gameType);
        if (gameRecord != null) {
            return gameTime < gameRecord.gameTime();
        }
        return true;
    }

    public GameRecord getRecordByGameType(GameType gameType) {
        if (records.get(gameType) == null) {
            return new GameRecord(DEFAULT_NAME, DEFAULT_TIME);
        }
        return records.get(gameType);
    }

    public void saveRecord(GameType gameType, String winnerName, int gameTime) {
        if (winnerName.isBlank()) {
            winnerName = DEFAULT_NAME;
        }
        records.put(gameType, new GameRecord(winnerName, gameTime));
        saveChanges();
    }

    private void saveChanges() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(RECORDS_FILEPATH))) {
            for (Map.Entry<GameType, GameRecord> entry : records.entrySet()) {
                bufferedWriter.write(entry.getKey() + " " + entry.getValue().winnerName() + " " +
                        entry.getValue().gameTime());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            System.err.println(ERROR_MESSAGE + e.getMessage());
        }
    }

}
