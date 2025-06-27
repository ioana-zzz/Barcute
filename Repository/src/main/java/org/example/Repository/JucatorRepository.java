package org.example.Repository;


import org.example.Interfaces.IJucatorRepository;
import org.example.Jucator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JucatorRepository implements IJucatorRepository {

    private final SessionFactory sessionFactory;

    public JucatorRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Jucator> save(Jucator jucator) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(jucator);
            session.getTransaction().commit();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.of(jucator);
        }
    }


    public Optional<Jucator> findOne(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Jucator.class, id));
        }
    }


    public List<Jucator> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Jucator", Jucator.class).getResultList();
        }
    }


    public Optional<Jucator> update(Jucator jucator) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Jucator updated = (Jucator) session.merge(jucator);
            session.getTransaction().commit();
            return Optional.ofNullable(updated);
        }
    }


    public Optional<Jucator> delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Jucator jucator = session.find(Jucator.class, id);
            if (jucator != null) {
                session.beginTransaction();
                session.remove(jucator);
                session.getTransaction().commit();
                return Optional.of(jucator);
            }
            return Optional.empty();
        }
    }


    public Optional<Jucator> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Jucator where nume = :name", Jucator.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        }
    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }







}
