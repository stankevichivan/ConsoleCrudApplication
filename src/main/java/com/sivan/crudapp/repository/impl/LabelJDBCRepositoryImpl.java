package com.sivan.crudapp.repository.impl;

import com.sivan.crudapp.db.ConnectionPool;
import com.sivan.crudapp.exception.JDBCRepositoryException;
import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.repository.JDBCLabelRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LabelJDBCRepositoryImpl implements JDBCLabelRepository {

    private static LabelJDBCRepositoryImpl INSTANCE;

    private static final String FIND_ALL = """
            select id, name from labels
            """;

    private static final String FIND_BY_ID = FIND_ALL + """
            where id = ?
            """;

    private static final String FIND_ALL_BY_POST_ID = FIND_ALL + """
            where post_id = ?
            """;

    private static final String INSERT = """
            insert into labels (name) VALUES (?);
            """;

    private static final String DELETE_BY_ID = """
            delete from labels where id = ?
            """;

    private static final String DELETE_ALL = """
            truncate table labels;
            """;

    private static final String UPDATE = """
            update labels set name = ? where id = ?
            """;

    private static final String ADD_LABEL_TO_POST = """
            update labels set post_id = ? where id = ?
            """;
    private static final String DELETE_LABEL_FROM_POST = """
            update labels set post_id = null where id = ?
            """;

    private LabelJDBCRepositoryImpl() {
    }

    public static synchronized LabelJDBCRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LabelJDBCRepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public Label create(Label label) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, label.getName());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                label.setId(generatedKeys.getLong(1));
            }
            return label;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public Optional<Label> getById(Long id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createLabel(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public List<Label> getAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            var labels = new ArrayList<Label>();
            while (resultSet.next()) {
                labels.add(createLabel(resultSet));
            }
            return labels;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public List<Label> getAllByPostId(Long postId) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_POST_ID)) {
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            var labels = new ArrayList<Label>();
            while (resultSet.next()) {
                labels.add(createLabel(resultSet));
            }
            return labels;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public void deleteLabelFromPost(Long labelId) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(DELETE_LABEL_FROM_POST)) {
            preparedStatement.setLong(1, labelId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public boolean update(Label label) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, label.getName());
            preparedStatement.setLong(2, label.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public void deleteAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(DELETE_ALL)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public boolean addLabelToPost(Long postId, Long labelId) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(ADD_LABEL_TO_POST)) {
            preparedStatement.setLong(1, postId);
            preparedStatement.setLong(2, labelId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    private static Label createLabel(ResultSet resultSet) throws SQLException {
        return Label.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
