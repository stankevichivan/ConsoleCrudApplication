package com.sivan.crudapp.repository.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sivan.crudapp.repository.LabelRepository;
import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.model.Status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LabelRepositoryImpl implements LabelRepository {
    private final Path path;
    private final Gson gson;

    public LabelRepositoryImpl() {
        path = Paths.get("src/main/resources/labels.json");
        gson = new Gson();
    }

    @Override
    public Label create(Label label) {
        var labels = getAll();
        labels.stream()
                .map(Label::getId).max(Long::compareTo)
                .ifPresentOrElse(nextId -> {
                    label.setId(nextId + 1);
                    label.setStatus(Status.ACTIVE);
                }, () -> {
                    label.setId(1L);
                    label.setStatus(Status.ACTIVE);
                });
        labels.add(label);
        saveAll(labels);
        return label;
    }

    @Override
    public Label getById(Long id) {
        var labels = getAll();
        var result = labels.stream().filter(label -> label.getId() == id).findFirst();
        return result.orElse(null);
    }

    @Override
    public List<Label> getAll() {
        try (var bufferedReader = Files.newBufferedReader(path)) {
            var type = new TypeToken<List<Label>>() {
            }.getType();
            List<Label> labels = gson.fromJson(bufferedReader, type);
            return labels == null ? new ArrayList<>() : new ArrayList<>(labels);
        } catch (IOException e) {
            System.out.println("Error on reading json File");
            return new ArrayList<>();
        }
    }

    @Override
    public void update(Label label) {
        var labels = getAll();
        labels.stream().filter(val -> val.getId() == label.getId())
                .findFirst()
                .ifPresentOrElse(val -> {
                    val.setName(label.getName());
                    val.setStatus(Status.ACTIVE);
                }, () -> System.out.println("label not found"));
        saveAll(labels);
    }

    @Override
    public Label deleteById(Long id) {
        var labels = getAll();
        var result = labels.stream().filter(label -> label.getId() == id).findFirst();
        if (result.isPresent()) {
            result.get().setStatus(Status.DELETED);
            saveAll(labels);
            return result.get();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        var labels = getAll();
        labels.forEach(label -> label.setStatus(Status.DELETED));
        saveAll(labels);
    }

    private void saveAll(List<Label> labels) {
        try (var bufferedWriter = Files.newBufferedWriter(path)) {
            var jsonTree = gson.toJsonTree(labels);
            gson.toJson(jsonTree, bufferedWriter);
        } catch (IOException e) {
            System.out.println("Error on saving list of labels in Json File!");
        }
    }
}
