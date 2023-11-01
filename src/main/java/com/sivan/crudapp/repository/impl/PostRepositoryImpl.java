package com.sivan.crudapp.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.PostStatus;
import com.sivan.crudapp.repository.PostRepository;
import com.sivan.crudapp.repository.adapter.LocalDateTypeAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostRepositoryImpl implements PostRepository {
    private final Path path;
    private final Gson gson;

    public PostRepositoryImpl() {
        path = Paths.get("src/main/resources/posts.json");
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
                .create();
    }

    @Override
    public Post create(Post post) {
        var posts = getAll();
        posts.stream()
                .map(Post::getId).max(Long::compareTo)
                .ifPresentOrElse(nextId -> post.setId(nextId + 1), () -> post.setId(1L));
        posts.add(post);
        saveAll(posts);
        return post;
    }

    @Override
    public Post getById(Long id) {
        var posts = getAll();
        var result = posts.stream().filter(post -> post.getId() == id).findFirst();
        return result.orElse(null);
    }

    @Override
    public List<Post> getAll() {
        try (var bufferedReader = Files.newBufferedReader(path)) {
            var type = new TypeToken<List<Post>>() {
            }.getType();
            List<Post> posts = gson.fromJson(bufferedReader, type);
            return posts == null ? new ArrayList<>() : new ArrayList<>(posts);
        } catch (IOException e) {
            System.out.println("Error on reading json File");
            return new ArrayList<>();
        }
    }

    @Override
    public void update(Post post) {
        var posts = getAll();
        posts.stream().filter(val -> val.getId() == post.getId())
                .findFirst()
                .ifPresentOrElse(val -> {
                    val.setContent(post.getContent());
                    val.setPostStatus(post.getPostStatus());
                    val.setUpdated(post.getUpdated());
                    val.setLabels(post.getLabels());
                }, () -> System.out.println("post not found"));
        saveAll(posts);
    }

    @Override
    public Post deleteById(Long id) {
        var posts = getAll();
        var result = posts.stream().filter(post -> post.getId() == id).findFirst();
        if (result.isPresent()) {
            result.get().setPostStatus(PostStatus.DELETED);
            saveAll(posts);
            return result.get();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        throw new AssertionError("not supported");
    }

    private void saveAll(List<Post> posts) {
        try (var bufferedWriter = Files.newBufferedWriter(path)) {
            var jsonTree = gson.toJsonTree(posts);
            gson.toJson(jsonTree, bufferedWriter);
        } catch (IOException e) {
            System.out.println("Error on saving list of posts in Json File!");
        }
    }
}
