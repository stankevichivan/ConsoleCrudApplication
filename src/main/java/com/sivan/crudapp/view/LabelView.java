package com.sivan.crudapp.view;

import com.sivan.crudapp.controller.LabelController;
import com.sivan.crudapp.model.Label;

import java.io.BufferedReader;
import java.io.IOException;

public class LabelView {
    public static final String ENTER_LABEL_ID = "Enter labelId: ";
    private static final String ENTER_NAME = "Enter name: ";
    private final LabelController labelController;

    public LabelView() {
        this.labelController = new LabelController();
    }

    public void startOperations(BufferedReader reader) throws IOException {
        System.out.println("Label Operations:");
        boolean run = true;
        while (run) {
            System.out.println("""
                    1. Create Label
                    2. Get Label by ID
                    3. Get All Labels
                    4. Update Label
                    5. Delete Label By Id
                    6. Delete All labels
                    0. Exit to Main Menu
                    """);

            var value = reader.readLine();
            switch (value) {
                case "1" -> createLabel(reader);
                case "2" -> getLabelById(reader);
                case "3" -> getAllLabels();
                case "4" -> updateLabel(reader);
                case "5" -> deleteLabelById(reader);
                case "6" -> deleteAllLabels();
                case "0" -> run = false;
                default -> System.out.println("Please try again\n");
            }
        }
    }

    private void deleteAllLabels() {
        labelController.deleteAllLabels();
    }

    private void deleteLabelById(BufferedReader reader) throws IOException {
        System.out.println(ENTER_LABEL_ID);
        var labelId = reader.readLine();
        System.out.println(labelController.deleteLabelById(Long.valueOf(labelId)));
    }

    private void updateLabel(BufferedReader reader) throws IOException {
        System.out.println(ENTER_LABEL_ID);
        var labelId = reader.readLine();
        System.out.println(ENTER_NAME);
        var labelName = reader.readLine();
        var label = new Label();
        label.setId(Long.parseLong(labelId));
        label.setName(labelName);
        System.out.println(labelController.updateLabel(label));
    }

    private void getLabelById(BufferedReader reader) throws IOException {
        System.out.println(ENTER_LABEL_ID);
        var labelId = reader.readLine();
        labelController.getLabelById(Long.valueOf(labelId))
                .ifPresentOrElse(System.out::println, () -> System.out.println("Label does not exists"));
    }

    private void getAllLabels() {
        labelController.getAllLabels().forEach(System.out::println);
    }

    private void createLabel(BufferedReader reader) throws IOException {
        System.out.println(ENTER_NAME);
        var name = reader.readLine();
        Label label = new Label();
        label.setName(name);
        System.out.println(labelController.createLabel(label));
    }
}
