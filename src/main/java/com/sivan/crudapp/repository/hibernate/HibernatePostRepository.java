package com.sivan.crudapp.repository.hibernate;

import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.repository.PostRepository;
import com.sivan.crudapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HibernatePostRepository implements PostRepository {

    @Override
    public Post create(Post post) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.persist(post);
            transaction.commit();
            return post;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: create (HibernatePostRepository repository)");
            return null;
        }
    }

    @Override
    public Optional<Post> getById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var post = session.get(Post.class, id);
            transaction.commit();
            return Optional.ofNullable(post);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: getById (HibernatePostRepository repository)");
            return Optional.empty();
        }
    }

    @Override
    public List<Post> getAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var posts = session.createQuery("from Post", Post.class).list();
            transaction.commit();
            return posts;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: getAll (HibernatePostRepository repository)");
            return new ArrayList<>();
        }
    }

    @Override
    public boolean update(Post post) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.merge(post);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: update (HibernatePostRepository repository)");
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var post = session.get(Post.class, id);
            post.getLabels().forEach(
                    label -> label.setPosts(label.getPosts()
                            .stream()
                            .filter(val -> !Objects.equals(val.getId(), post.getId())).toList()));
            session.remove(post);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: deleteById (HibernatePostRepository repository)");
            return false;
        }
    }

    @Override
    public void deleteAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery("truncate table post cascade", Post.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: deleteAll (HibernatePostRepository repository)");
        }
    }

    @Override
    public boolean addPostToWriter(Long postId, Long writerId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var writer = session.get(Writer.class, writerId);
            var post = session.get(Post.class, postId);
            writer.getPosts().add(post);
            post.setWriter(writer);
            session.persist(writer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: addPostToWriter (HibernatePostRepository repository)");
            return false;
        }
    }

    @Override
    public List<Post> getAllByWriterId(Long writerId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var writer = session.get(Writer.class, writerId);
            transaction.commit();
            return writer.getPosts();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: getAllByWriterId (HibernatePostRepository repository)");
            return new ArrayList<>();
        }
    }

    @Override
    public void deletePostFromWriter(Long postId, Long writerId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var writer = session.get(Writer.class, writerId);
            var post = session.get(Post.class, postId);
            post.setWriter(null);
            writer.setPosts(writer.getPosts().stream().filter(val -> !val.getId().equals(postId)).toList());
            session.persist(writer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: deletePostFromWriter (HibernatePostRepository repository)");
        }
    }
}
