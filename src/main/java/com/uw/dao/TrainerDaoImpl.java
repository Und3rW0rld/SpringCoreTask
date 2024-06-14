package com.uw.dao;

import com.uw.model.Trainer;
import com.uw.model.User;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Logger;

@Repository
public class TrainerDaoImpl implements TrainerDao{

    private SessionFactory sessionFactory;
    private static final Logger logger = Logger.getLogger(TrainerDaoImpl.class.getName());

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Trainer trainer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(trainer);
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
            }
            logger.severe("Failed to create trainer transaction");
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void update(Trainer trainer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(trainer);
            transaction.commit();
        } catch (Exception e) {
            if( transaction != null ){
                transaction.rollback();
            }
            logger.severe("Error trying to update trainer");
            logger.severe(e.getMessage());
        }
    }

    @Override
    public Trainer selectProfile(long id) {
        Trainer trainer = null;
        try(Session session = sessionFactory.openSession()){
            trainer = session.get(Trainer.class, id);
        }catch ( Exception e ){
            logger.severe("Trainer not found");
            logger.severe(e.getMessage());
        }
        return trainer;
    }

    @Override
    public List<Trainer> findAll() {
        List<Trainer> trainerList = List.of();
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("select t from Trainer t", Trainer.class);
            trainerList = query.getResultList();
        } catch (Exception e) {
            logger.severe("Can not find trainers list");
            logger.severe(e.getMessage());
        }
        return trainerList;
    }

    @Override
    public Trainer findTrainerByUser(User user) {
        Trainer trainer = null;
        try (Session session = sessionFactory.openSession()) {
            org.hibernate.query.Query<Trainer> query = session.createQuery("select t from Trainer t where t.user = :user").setParameter("user", user);
            trainer = query.getSingleResult();
        }catch (Exception e){
            logger.severe("Can not find trainer by user");
            logger.severe(e.getMessage());
        }
        return trainer;
    }

    @Override
    public Trainer findTrainerByUsername(String username) {
        Trainer trainer = null;
        try(Session session = sessionFactory.openSession()){
            org.hibernate.query.Query<Trainer> query = session.createQuery("select t from Trainer t where t.user.username = :username").setParameter("username", username);
            trainer = query.getSingleResult();
        }catch (Exception e){
            logger.severe("Can not find trainer by username");
            logger.severe(e.getMessage());
        }
        return trainer;
    }
}
