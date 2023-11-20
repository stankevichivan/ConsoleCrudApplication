package com.sivan.crudapp.context;

import com.sivan.crudapp.util.ConnectionUtil;
import com.sivan.crudapp.view.LabelView;
import com.sivan.crudapp.view.PostView;
import com.sivan.crudapp.view.WriterView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApplicationContext {
    private final PostView postView;
    private final WriterView writerView;
    private final LabelView labelView;

    public ApplicationContext() {
        this.labelView = new LabelView();
        this.writerView = new WriterView();
        this.postView = new PostView();
    }

    public void start() {

        System.out.println("Hello, what do you want?\n");

        boolean run = true;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (run) {
                System.out.println("""
                        1. Label Operations
                        2. Post Operations
                        3. Writer Operations
                        0. Exit
                        """);
                switch (reader.readLine()) {
                    case "1" -> labelView.startOperations(reader);
                    case "2" -> postView.startOperations(reader);
                    case "3" -> writerView.startOperations(reader);
                    case "0" -> run = false;
                    default -> System.out.println("Please try again\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error with console input");
        }
    }
}
