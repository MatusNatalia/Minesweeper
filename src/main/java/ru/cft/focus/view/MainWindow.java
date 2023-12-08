package ru.cft.focus.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWindow extends JFrame {
    private final Container contentPane;
    private final GridBagLayout mainLayout;

    private JMenuItem newGameMenu;
    private JMenuItem highScoresMenu;
    private JMenuItem settingsMenu;
    private JMenuItem exitMenu;

    private CellEventListener listener;

    private JButton[][] cellButtons;
    private JLabel timerLabel;
    private JLabel bombsCounterLabel;

    public MainWindow() {
        super("Miner");
        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        createMenu();

        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);

        contentPane.setBackground(new Color(241, 154, 222, 255));
    }

    private void createMenu() {
        Color textColor = new Color(83, 0, 250, 255);
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        gameMenu.setForeground(textColor);
        gameMenu.add(newGameMenu = new JMenuItem("New Game")).setForeground(textColor);
        gameMenu.addSeparator();
        gameMenu.add(highScoresMenu = new JMenuItem("High Scores")).setForeground(textColor);
        gameMenu.add(settingsMenu = new JMenuItem("Settings")).setForeground(textColor);
        gameMenu.addSeparator();
        gameMenu.add(exitMenu = new JMenuItem("Exit")).setForeground(textColor);

        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    public void setNewGameMenuAction(ActionListener listener) {
        newGameMenu.addActionListener(listener);
    }

    public void setHighScoresMenuAction(ActionListener listener) {
        highScoresMenu.addActionListener(listener);
    }

    public void setSettingsMenuAction(ActionListener listener) {
        settingsMenu.addActionListener(listener);
    }

    public void setExitMenuAction(ActionListener listener) {
        exitMenu.addActionListener(listener);
    }

    public void setCellListener(CellEventListener listener) {
        this.listener = listener;
    }

    public void setCellImage(int x, int y, GameImage gameImage) {
        cellButtons[x][y].setIcon(gameImage.getImageIcon());
    }

    public void setBombsCount(int bombsCount) {
        bombsCounterLabel.setText(String.valueOf(bombsCount));
    }

    public void setTimerValue(int value) {
        timerLabel.setText(String.valueOf(value));
    }

    public int getTimerValue() {
        return Integer.parseInt(timerLabel.getText());
    }

    public void createGameField(int rowsCount, int colsCount) {
        contentPane.removeAll();
        setPreferredSize(new Dimension(30 * colsCount + 70, 30 * rowsCount + 110));

        addButtonsPanel(createButtonsPanel(rowsCount, colsCount));
        addTimerImage();
        addTimerLabel(timerLabel = new JLabel("0"));
        addBombCounter(bombsCounterLabel = new JLabel("0"));
        addBombCounterImage();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createButtonsPanel(int numberOfRows, int numberOfCols) {
        cellButtons = new JButton[numberOfRows][numberOfCols];
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(30 * numberOfCols, 30 * numberOfRows));
        buttonsPanel.setLayout(new GridLayout(numberOfRows, numberOfCols, 0, 0));

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                final int x = col;
                final int y = row;

                cellButtons[y][x] = new JButton(GameImage.CLOSED.getImageIcon());
                cellButtons[y][x].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (listener == null) {
                            return;
                        }

                        switch (e.getButton()) {
                            case MouseEvent.BUTTON1:
                                listener.onMouseClick(y, x, ButtonType.LEFT_BUTTON);
                                break;
                            case MouseEvent.BUTTON2:
                                listener.onMouseClick(y, x, ButtonType.MIDDLE_BUTTON);
                                break;
                            case MouseEvent.BUTTON3:
                                listener.onMouseClick(y, x, ButtonType.RIGHT_BUTTON);
                                break;
                            default:
                                // Other mouse buttons are ignored
                        }
                    }
                });
                buttonsPanel.add(cellButtons[y][x]);
            }
        }

        return buttonsPanel;
    }

    private void addButtonsPanel(JPanel buttonsPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 20, 5, 20);
        mainLayout.setConstraints(buttonsPanel, gbc);
        contentPane.add(buttonsPanel);
    }

    private void addTimerImage() {
        JLabel label = new JLabel(GameImage.TIMER.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 20, 0, 0);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    private void addTimerLabel(JLabel timerLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 5, 0, 0);
        mainLayout.setConstraints(timerLabel, gbc);
        contentPane.add(timerLabel);
    }

    private void addBombCounter(JLabel bombsCounterLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 0.7;
        mainLayout.setConstraints(bombsCounterLabel, gbc);
        contentPane.add(bombsCounterLabel);
    }

    private void addBombCounterImage() {
        JLabel label = new JLabel(GameImage.BOMB_ICON.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 5, 0, 20);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }
}
