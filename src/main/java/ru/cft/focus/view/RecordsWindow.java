package ru.cft.focus.view;

import javax.swing.*;
import java.awt.*;

public class RecordsWindow extends JDialog {
    private RecordNameListener nameListener;

    public RecordsWindow(JFrame frame) {
        super(frame, "New Record", true);

        JTextField nameField = new JTextField();

        GridLayout layout = new GridLayout(3, 1);
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        JLabel label = new JLabel("Enter your name:");
        label.setForeground(new Color(83, 0, 250, 255));
        contentPane.add(label);
        contentPane.add(nameField);
        contentPane.add(createOkButton(nameField));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(210, 120));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    public void setNameListener(RecordNameListener nameListener) {
        this.nameListener = nameListener;
    }

    private JButton createOkButton(JTextField nameField) {
        JButton button = new JButton("OK");
        button.setForeground(new Color(83, 0, 250, 255));
        button.addActionListener(e -> {
            dispose();

            if (nameListener != null) {
                nameListener.onRecordNameEntered(nameField.getText());
            }
        });
        return button;
    }
}
