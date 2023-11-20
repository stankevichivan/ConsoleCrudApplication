package com.sivan.crudapp.repository.hibernate;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.repository.LabelRepository;
import com.sivan.crudapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class HibernateLabelRepository implements LabelRepository {
    @Override
    public Label create(Label label) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.persist(label);
            transaction.commit();
            return label;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: create (HibernateLabelRepository repository)");
            return null;
        }
    }

    @Override
    public Optional<Label> getById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var label = session.get(Label.class, id);
            transaction.commit();
            return Optional.ofNullable(label);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: getById (HibernateLabelRepository repository)");
            return Optional.empty();
        }
    }

    @Override
    public List<Label> getAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var labels = session.createQuery("from Label", Label.class).list();
            transaction.commit();
            return labels;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: getAll (HibernateLabelRepository repository)");
            return new ArrayList<>();
        }
    }

    @Override
    public boolean update(Label label) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.merge(label);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: update (HibernateLabelRepository repository)");
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            var label = getById(id);
            if (label.isPresent()) {
                transaction = session.beginTransaction();
                session.remove(label.get());
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: deleteById (HibernateLabelRepository repository)");
            return false;
        }
        return false;
    }

    @Override
    public void deleteAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery("truncate table label cascade", Label.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: deleteAll (HibernateLabelRepository repository)");
        }
    }

    @Override
    public boolean addLabelToPost(Long postId, Long labelId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var label = session.get(Label.class, labelId);
            var post = session.get(Post.class, postId);
            post.getLabels().add(label);
            label.getPosts().add(post);
            session.persist(post);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: addLabelToPost (HibernateLabelRepository repository)");
            return false;
        }
    }

    @Override
    public List<Label> getAllByPostId(Long postId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var labels = session.createQuery("from Label", Label.class).list();
            var result = labels.stream().filter(label ->
                            label.getPosts()
                                    .stream()
                                    .map(Post::getId)
                                    .toList()
                                    .contains(postId))
                    .collect(Collectors.toList());
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: getAllByPostId (HibernateLabelRepository repository)");
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteLabelFromPost(Long labelId, Long postId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var label = session.get(Label.class, labelId);
            var post = session.get(Post.class, postId);
            label.setPosts(label.getPosts().stream().filter(val -> !Objects.equals(val.getId(), postId)).toList());
            post.setLabels(post.getLabels().stream().filter(val -> !Objects.equals(val.getId(), labelId)).toList());
            session.persist(post);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: deleteLabelFromPost (HibernateLabelRepository repository)");
        }
    }
}
