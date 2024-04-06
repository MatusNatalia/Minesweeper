package ru.cft.focus;

import ru.cft.focus.controller.Controller;
import ru.cft.focus.model.DefaultModel;
import ru.cft.focus.records.GameRecordTable;
import ru.cft.focus.timer.GameTimer;
import ru.cft.focus.view.View;

public class Main {
    public static void main(String[] args) {
        try {
            DefaultModel model = new DefaultModel();
            Controller controller = new Controller(model);
            GameTimer gameTimer = new GameTimer(model);
            GameRecordTable gameRecordTable = new GameRecordTable();
            View view = new View(model, controller, gameTimer, gameRecordTable);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
