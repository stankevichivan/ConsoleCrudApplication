package com.sivan.crudapp.repository.hibernate;

import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.repository.WriterRepository;
import com.sivan.crudapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HibernateWriterRepository implements WriterRepository {
    @Override
    public Writer create(Writer writer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.persist(writer);
            transaction.commit();
            return writer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: create (HibernateWriterRepository repository)");
            return null;
        }
    }

    @Override
    public Optional<Writer> getById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var writer = session.get(Writer.class, id);
            transaction.commit();
            return Optional.ofNullable(writer);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: getById (HibernateWriterRepository repository)");
            return Optional.empty();
        }
    }

    @Override
    public List<Writer> getAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            var writers = session.createQuery("from Writer", Writer.class).list();
            transaction.commit();
            return writers;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: getAll (HibernateWriterRepository repository)");
            return new ArrayList<>();
        }
    }

    @Override
    public boolean update(Writer writer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.merge(writer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: update (HibernateWriterRepository repository)");
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            var writer = getById(id);
            if (writer.isPresent()) {
                transaction = session.beginTransaction();
                session.remove(writer.get());
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: deleteById (HibernateWriterRepository repository)");
            return false;
        }
        return false;
    }

    @Override
    public void deleteAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery("truncate table writer cascade", Writer.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in method: deleteAll (HibernateWriterRepository repository)");
        }
    }
}
