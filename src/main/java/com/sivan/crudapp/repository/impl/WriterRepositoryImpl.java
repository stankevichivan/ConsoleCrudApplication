package com.sivan.crudapp.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sivan.crudapp.repository.WriterRepository;
import com.sivan.crudapp.repository.adapter.LocalDateTypeAdapter;
import com.sivan.crudapp.model.Status;
import com.sivan.crudapp.model.Writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WriterRepositoryImpl implements WriterRepository {
    private final Path path;
    private final Gson gson;

    public WriterRepositoryImpl() {
        path = Paths.get("src/main/resources/writers.json");
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
                .create();
    }

    @Override
    public Writer create(Writer writer) {
        var writers = getAll();
        writers.stream()
                .map(Writer::getId).max(Long::compareTo)
                .ifPresentOrElse(nextId -> {
                    writer.setId(nextId + 1);
                    writer.setStatus(Status.ACTIVE);
                }, () -> {
                    writer.setId(1);
                    writer.setStatus(Status.ACTIVE);
                });
        writers.add(writer);
        saveAll(writers);
        return writer;
    }

    @Override
    public Writer getById(Long id) {
        var writers = getAll();
        var result = writers.stream().filter(post -> post.getId() == id).findFirst();
        return result.orElse(null);
    }

    @Override
    public List<Writer> getAll() {
        try (var bufferedReader = Files.newBufferedReader(path)) {
            var type = new TypeToken<List<Writer>>() {
            }.getType();
            List<Writer> writers = gson.fromJson(bufferedReader, type);
            return writers == null ? new ArrayList<>() : new ArrayList<>(writers);
        } catch (IOException e) {
            System.out.println("Error on reading json File");
            return new ArrayList<>();
        }
    }

    @Override
    public void update(Writer writer) {
        var writers = getAll();
        writers.stream().filter(val -> val.getId() == writer.getId())
                .findFirst()
                .ifPresentOrElse(val -> {
                    val.setFirstName(writer.getFirstName());
                    val.setLastName(writer.getLastName());
                    val.setPosts(writer.getPosts());
                }, () -> System.out.println("writer not found"));
        saveAll(writers);
    }

    @Override
    public Writer deleteById(Long id) {
        var writers = getAll();
        var result = writers.stream().filter(writer -> writer.getId() == id).findFirst();
        if (result.isPresent()) {
            result.get().setStatus(Status.DELETED);
            saveAll(writers);
            return result.get();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        throw new AssertionError("not supported");

    }

    private void saveAll(List<Writer> writers) {
        try (var bufferedWriter = Files.newBufferedWriter(path)) {
            var jsonTree = gson.toJsonTree(writers);
            gson.toJson(jsonTree, bufferedWriter);
        } catch (IOException e) {
            System.out.println("Error on saving list of writers in Json File!");
        }
    }
}
