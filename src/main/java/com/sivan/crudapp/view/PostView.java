package com.sivan.crudapp.view;

import com.sivan.crudapp.controller.PostController;
import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.PostStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.sivan.crudapp.view.LabelView.ENTER_LABEL_ID;

public class PostView {
    private final PostController postController;

    public PostView() {
        postController = new PostController();
    }

    public void startOperations(BufferedReader reader) throws IOException {
        System.out.println("Post Operations:");
        boolean run = true;
        while (run) {
            System.out.println("""
                    1. Create Post
                    2. Get Post by ID
                    3. Get All Posts
                    4. Update Post
                    5. Delete Post By Id
                    6. Add label to post
                    7. Delete label from post
                    0. Exit to Main Menu
                    """);

            var value = reader.readLine();
            switch (value) {
                case "1" -> createPost(reader);
                case "2" -> getPostById(reader);
                case "3" -> getAllPosts();
                case "4" -> updatePost(reader);
                case "5" -> deletePostById(reader);
                case "6" -> addLabelToPost(reader);
                case "7" -> deleteLabelFromPost(reader);
                case "0" -> run = false;
                default -> System.out.println("Please try again\n");
            }
        }
    }

    private void deleteLabelFromPost(BufferedReader reader) throws IOException {
        var postId = getPostId(reader);
        System.out.println(ENTER_LABEL_ID);
        var labelId = reader.readLine();
        postController.deleteLabelFromPost(postId, Long.valueOf(labelId));
    }

    private void addLabelToPost(BufferedReader reader) throws IOException {
        var postId = getPostId(reader);
        System.out.println(ENTER_LABEL_ID);
        var labelId = reader.readLine();
        System.out.println(postController.addLabelToPost(postId, Long.valueOf(labelId)));
    }

    private void deletePostById(BufferedReader reader) throws IOException {
        var postId = getPostId(reader);
        postController.deletePostById(postId).ifPresentOrElse(System.out::println, () -> System.out.println("Post does not exists"));
    }

    private void updatePost(BufferedReader reader) throws IOException {
        var postId = getPostId(reader);
        System.out.println("Enter post content");
        var postContent = reader.readLine();
        var postStatus = getPostStatus(reader);
        postController.updatePost(Post.builder().
                id(postId)
                .content(postContent)
                .postStatus(postStatus)
                .updated(LocalDateTime.now())
                .build());
    }

    private void getAllPosts() {
        postController.getAllPosts().forEach(System.out::println);
    }

    private void getPostById(BufferedReader reader) throws IOException {
        var postId = getPostId(reader);
        postController.getPostById(postId).ifPresentOrElse(System.out::println, () -> System.out.println("Post does not exists"));
    }

    private void createPost(BufferedReader reader) throws IOException {
        System.out.println("Enter post content");
        var content = reader.readLine();

        var postStatus = getPostStatus(reader);

        System.out.println(postController.createPost(Post.builder()
                .content(content)
                .postStatus(postStatus)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build()));
    }

    private static PostStatus getPostStatus(BufferedReader reader) throws IOException {
        System.out.println("""
                Enter number post status:
                0 - ACTIVE
                1 - UNDER
                2 - REVIEW
                3 - DELETED""");

        var postStatusNumber = reader.readLine();
        var status = Arrays.stream(PostStatus.values())
                .filter(postStatus -> postStatus.ordinal() == Integer.parseInt(postStatusNumber))
                .findFirst();
        return status.orElse(PostStatus.ACTIVE);
    }

    public static Long getPostId(BufferedReader reader) throws IOException {
        System.out.println("enter post Id");
        return Long.valueOf(reader.readLine());
    }
}
