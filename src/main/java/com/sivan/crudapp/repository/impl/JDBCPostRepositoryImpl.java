package com.sivan.crudapp.repository.impl;

import com.sivan.crudapp.db.ConnectionPool;
import com.sivan.crudapp.exception.JDBCRepositoryException;
import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.PostStatus;
import com.sivan.crudapp.repository.JDBCPostRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPostRepositoryImpl implements JDBCPostRepository {

    public static final String INSERT = """
            insert into posts (content, created, updated, status) VALUES (?, ?, ?, ?);
            """;
    public static final String FIND_ALL = """
            select id, id, content, created, updated, status from posts
            """;
    public static final String FIND_BY_ID = FIND_ALL + """
            where id = ?
            """;
    public static final String UPDATE = """
            update posts set content = ?,
                created = ?,
                updated = ?,
                status =?
            where id = ?
            """;
    public static final String DELETE_BY_ID = """
            delete from posts where id = ?
            """;
    public static final String DELETE_ALL = """
            truncate table posts
            """;

    @Override
    public Post create(Post post) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, post.getContent());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(post.getCreated()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(post.getUpdated()));
            preparedStatement.setString(4, post.getPostStatus().toString());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                post.setId(generatedKeys.getLong(1));
            }
            return post;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public Optional<Post> getById(Long id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createPost(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public List<Post> getAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            var posts = new ArrayList<Post>();
            while (resultSet.next()) {
                posts.add(createPost(resultSet));
            }
            return posts;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public boolean update(Post post) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, post.getContent());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(post.getCreated()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(post.getUpdated()));
            preparedStatement.setString(4, post.getPostStatus().toString());
            preparedStatement.setLong(5, post.getId());
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

    private Post createPost(ResultSet resultSet) throws SQLException {
        return Post.builder()
                .id(resultSet.getLong("id"))
                .content(resultSet.getString("content"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .updated(resultSet.getTimestamp("updated").toLocalDateTime())
                .postStatus(PostStatus.valueOf(resultSet.getString("status")))
                .build();
    }
}
