package com.sivan.crudapp.repository.impl;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.util.ConnectionUtil;
import com.sivan.crudapp.exception.JDBCRepositoryException;
import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.PostStatus;
import com.sivan.crudapp.repository.PostRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPostRepositoryImpl implements PostRepository {

    public static final String INSERT = """
            insert into posts (content, created, updated, status) VALUES (?, ?, ?, ?);
            """;
    public static final String FIND_ALL = """
            select id, content, created, updated, status from posts
            """;
    public static final String FIND_BY_ID = FIND_ALL + """
            where id = ?
            """;

    private static final String FIND_ALL_BY_POST_ID = FIND_ALL + """
            where writer_id = ?
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

    private static final String DELETE_POST_FROM_WRITER = """
            update posts set writer_id = null where id = ?
            """;

    private static final String ADD_POST_TO_WRITER = """
            update posts set writer_id = ? where id = ?
            """;

    @Override
    public Post create(Post post) {
        try (var connection = ConnectionUtil.get();
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
        try (var connection = ConnectionUtil.get();
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
        try (var connection = ConnectionUtil.get();
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
        try (var connection = ConnectionUtil.get();
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

    private Post createPost(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("id"));
        post.setContent(resultSet.getString("content"));
        post.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
        post.setUpdated(resultSet.getTimestamp("updated").toLocalDateTime());
        post.setPostStatus(PostStatus.valueOf(resultSet.getString("status")));
        return post;
    }

    @Override
    public boolean addPostToWriter(Long postId, Long writerId) {
        try (var connection = ConnectionUtil.get();
             var preparedStatement = connection.prepareStatement(ADD_POST_TO_WRITER)) {
            preparedStatement.setLong(1, writerId);
            preparedStatement.setLong(2, postId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }

    @Override
    public List<Post> getAllByWriterId(Long writerId) {
        try (var connection = ConnectionUtil.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_POST_ID)) {
            preparedStatement.setLong(1, writerId);
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
    public void deletePostFromWriter(Long postId, Long writerId) {
        try (var connection = ConnectionUtil.get();
             var preparedStatement = connection.prepareStatement(DELETE_POST_FROM_WRITER)) {
            preparedStatement.setLong(1, postId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new JDBCRepositoryException(e);
        }
    }
}
