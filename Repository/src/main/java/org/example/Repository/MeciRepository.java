package org.example.Repository;


import org.example.Interfaces.IMeciRepository;
import org.example.Meci;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.example.Meci;
import java.util.List;
import java.util.Optional;

@Repository
public class MeciRepository implements IMeciRepository {

    private final SessionFactory sessionFactory;

    public MeciRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Meci> save(Meci meci) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(meci);
            session.getTransaction().commit();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.of(meci);
        }
    }


    public Optional<Meci> findOne(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Meci.class, id));
        }
    }


    public List<Meci> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Meci", Meci.class).getResultList();
        }
    }


    public Optional<Meci> update(Meci meci) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Meci updated = (Meci) session.merge(meci);
            session.getTransaction().commit();
            return Optional.ofNullable(updated);
        }
    }


    public Optional<Meci> delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Meci meci = session.find(Meci.class, id);
            if (meci != null) {
                session.beginTransaction();
                session.remove(meci);
                session.getTransaction().commit();
                return Optional.of(meci);
            }
            return Optional.empty();
        }
    }


    public Meci findLatest(){
        try(Session session = sessionFactory.openSession()){
            List<Meci> meciuri = session.createQuery("from Meci order by id desc", Meci.class).setMaxResults(1).getResultList();
            if(!meciuri.isEmpty()) {
                return meciuri.get(0);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }







}
