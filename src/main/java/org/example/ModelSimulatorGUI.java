package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ModelSimulatorGUI {
    private JFrame frame;
    private Controller controller;
    private String currentScript;
    private JTable table;
    private DefaultTableModel tableModel;

    public ModelSimulatorGUI() {
        frame = new JFrame("Model Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton loadModelButton = new JButton("Load Data");
        JButton runModelButton = new JButton("Run Model");
        JButton loadScriptButton = new JButton("Load Script");
        JButton runScriptButton = new JButton("Run Script");
        JButton createScriptButton = new JButton("Create Script");

        buttonPanel.add(loadModelButton);
        buttonPanel.add(runModelButton);
        buttonPanel.add(loadScriptButton);
        buttonPanel.add(runScriptButton);
        buttonPanel.add(createScriptButton);

        frame.add(buttonPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        loadModelButton.addActionListener(new LoadDataAction());
        runModelButton.addActionListener(new RunModelAction());
        loadScriptButton.addActionListener(new LoadScriptAction());
        runScriptButton.addActionListener(new RunScriptAction());
        createScriptButton.addActionListener(new CreateScriptAction());

        frame.setVisible(true);
    }

    private void loadResultsIntoTable() {
        // We receive data in TSV format
        String tsvData = controller.getResultsAsTsv();

        // Separate the lines
        String[] rows = tsvData.split("\n");

        // The first row is the column header.
        String[] columns = rows[0].split(" ");
        for (String column : columns) {
            tableModel.addColumn(column);
        }

        // Add the remaining lines as data
        for (int i = 1; i < rows.length; i++) {
            String[] rowValues = rows[i].split(" ");
            tableModel.addRow(rowValues);
        }
    }

    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        model.setColumnCount(0);
    }

    private class LoadDataAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Data loading logic
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                // Upload model
                String filePath = fileChooser.getSelectedFile().getPath();
                controller = new Controller("Model1");
                controller.readDataFrom(filePath);
            }
        }
    }

    private class RunModelAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (controller != null) {
                controller.runModel();
                clearTable();
                loadResultsIntoTable();
            } else {
                JOptionPane.showMessageDialog(frame, "Please load a data first." ,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class LoadScriptAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open a window to select a script file
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                // We receive the selected file
                String scriptFilePath = fileChooser.getSelectedFile().getPath();
                try {
                    // Loading the script
                    StringBuilder scriptContent = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new FileReader(scriptFilePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            scriptContent.append(line).append("\n");
                        }
                    }
                    // We save the contents of the script for further execution
                    currentScript = scriptContent.toString();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Script execution error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class RunScriptAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentScript != null && !currentScript.isEmpty()) {
                try {
                    System.out.println("Script content: " + currentScript);
                    Controller.runScript(currentScript);
                    clearTable();
                    loadResultsIntoTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Script execution error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println(ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Enter the script first.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class CreateScriptAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame editorFrame = new JFrame("Editor for Script");
            editorFrame.setSize(600, 400);  // Размер окна
            editorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Create a JTextArea to input the script
            JTextArea scriptArea = new JTextArea(20, 50);
            scriptArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            scriptArea.setLineWrap(true);
            scriptArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(scriptArea);
            editorFrame.add(scrollPane, BorderLayout.CENTER);

            JButton runButton = new JButton("Run Script");
            runButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String script = scriptArea.getText();
                    if (script != null && !script.isEmpty()) {
                        try {
                            Controller.runScript(script);
                            clearTable();
                            loadResultsIntoTable();
                            editorFrame.dispose();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(editorFrame, "Script execution error: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(editorFrame, "Enter the script first.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            editorFrame.add(runButton, BorderLayout.SOUTH);
            editorFrame.setVisible(true);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(ModelSimulatorGUI::new);
    }
}
