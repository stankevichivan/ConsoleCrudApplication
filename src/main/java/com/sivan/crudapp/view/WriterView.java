package com.sivan.crudapp.view;

import com.sivan.crudapp.controller.WriterController;
import com.sivan.crudapp.model.Writer;

import java.io.BufferedReader;
import java.io.IOException;

public class WriterView {
    private final WriterController controller;

    public WriterView() {
        this.controller = new WriterController();
    }

    public void startOperations(BufferedReader reader) throws IOException {
        System.out.println("Writer Operations:");
        boolean run = true;
        while (run) {
            System.out.println("""
                    1. Create Writer
                    2. Get Writer by ID
                    3. Get All Writers
                    4. Update Writer
                    5. Delete Writer By Id
                    6. Add post to writer
                    7. Delete post from writer
                    0. Exit to Main Menu
                    """);

            var value = reader.readLine();
            switch (value) {
                case "1" -> createWriter(reader);
                case "2" -> getWriterById(reader);
                case "3" -> getAllWriters();
                case "4" -> updateWriter(reader);
                case "5" -> deleteWriterById(reader);
                case "6" -> addPostToWriter(reader);
                case "7" -> deletePostFromWriter(reader);
                case "0" -> run = false;
                default -> System.out.println("Please try again\n");
            }
        }
    }

    private void deletePostFromWriter(BufferedReader reader) throws IOException {
        var postId = PostView.getPostId(reader);
        controller.deletePostFromWriter(postId);
    }

    private void addPostToWriter(BufferedReader reader) throws IOException {
        var postId = PostView.getPostId(reader);
        var writerId = getWriterId(reader);
        System.out.println(controller.addPostToWriter(postId, writerId));
    }

    private void deleteWriterById(BufferedReader reader) throws IOException {
        var writerId = getWriterId(reader);
        System.out.println(controller.deleteWriterById(writerId));
    }

    private void updateWriter(BufferedReader reader) throws IOException {
        var writerId = getWriterId(reader);
        System.out.println("Enter writer first Name");
        var firstName = reader.readLine();
        System.out.println("Enter writer last Name");
        var lastName = reader.readLine();
        controller.updateWriter(Writer.builder().
                id(writerId)
                .firstName(firstName)
                .lastName(lastName)
                .build());
    }

    private void getAllWriters() {
        controller.getAllWriters().forEach(System.out::println);
    }

    private void getWriterById(BufferedReader reader) throws IOException {
        var writerId = getWriterId(reader);
        controller.getWriterById(writerId)
                .ifPresentOrElse(System.out::println,
                        () -> System.out.println("Writer does not exists"));
    }

    private void createWriter(BufferedReader reader) throws IOException {
        System.out.println("Enter writer first Name");
        var firstName = reader.readLine();
        System.out.println("Enter writer last Name");
        var lastName = reader.readLine();

        System.out.println(controller.createWriter(Writer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build()));
    }

    private static long getWriterId(BufferedReader reader) throws IOException {
        System.out.println("enter writer Id");
        return Long.parseLong(reader.readLine());
    }
}
