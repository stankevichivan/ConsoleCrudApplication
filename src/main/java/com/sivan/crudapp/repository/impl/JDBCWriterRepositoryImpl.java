package com.sivan.crudapp.repository.impl;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.util.ConnectionUtil;
import com.sivan.crudapp.exception.JDBCRepositoryException;
import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.repository.WriterRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCWriterRepositoryImpl implements WriterRepository {

    public static final String INSERT = """
            insert into writers (first_name, last_name) VALUES (?, ?);
            """;
    public static final String FIND_ALL = """
            select id, first_name, last_name from writers
            """;
    public static final String FIND_BY_ID = FIND_ALL + """
            where id = ?
            """;
    public static final String UPDATE = """
            update writers set first_name = ?, last_name = ? where id = ?
            """;
    public static final String DELETE_BY_ID = """
            delete from writers where id = ?
            """;
    public static final String DELETE_ALL = """
            truncate table writers
            """;

    @Override
    public Writer create(Writer writer) {
        try (var connection = ConnectionUtil.get();
             var preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                writer.setId(generatedKeys.getLong(1));
            }
            return writer;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public Optional<Writer> getById(Long id) {
        try (var connection = ConnectionUtil.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createWriter(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public List<Writer> getAll() {
        try (var connection = ConnectionUtil.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            var writers = new ArrayList<Writer>();
            while (resultSet.next()) {
                writers.add(createWriter(resultSet));
            }
            return writers;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public boolean update(Writer writer) {
        try (var connection = ConnectionUtil.get();
             var preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());
            preparedStatement.setLong(3, writer.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (var connection = ConnectionUtil.get();
             var preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public void deleteAll() {
        try (var connection = ConnectionUtil.get();
             var preparedStatement = connection.prepareStatement(DELETE_ALL)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    private Writer createWriter(ResultSet resultSet) throws SQLException {
        return new Writer();
        //        return Writer.builder()
//                .id(resultSet.getLong("id"))
//                .firstName(resultSet.getString("first_name"))
//                .lastName(resultSet.getString("last_name"))
//                .build();
    }
}
