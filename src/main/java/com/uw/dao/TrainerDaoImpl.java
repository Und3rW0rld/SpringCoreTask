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

@Repository
public class TrainerDaoImpl implements TrainerDao{

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Trainer trainer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(trainer);
            transaction.commit();
        } catch ( Exception e ){
            transaction.rollback();
        }finally{
            session.close();
        }
    }

    @Override
    public void update(Trainer trainer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(trainer);
            transaction.commit();
        } catch ( Exception e ){
            transaction.rollback();
        }finally {
            session.close();
        }
    }

    @Override
    public Trainer selectProfile(long id) {
        Session session = sessionFactory.openSession();
        Trainer trainer = null;
        try{
            trainer = session.get(Trainer.class, id);
        }catch ( Exception e ){
            e.printStackTrace();
        }finally{
            session.close();
        }
        return trainer;
    }

    @Override
    public List<Trainer> findAll() {
        Session session = sessionFactory.openSession();
        List<Trainer> trainerList = null;

        try {
            // Crear un objeto Criteria para la clase de entidad User
            Query query = session.createQuery("select t from Trainer t", Trainer.class);
            trainerList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return trainerList;
    }

    @Override
    public Trainer findTrainerByUser(User user) {
        Session session = sessionFactory.openSession();
        Trainer trainer = null;
        try{
            org.hibernate.query.Query<Trainer> query = session.createQuery("select t from Trainer t where t.user = :user").setParameter("user", user);
            trainer = query.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            session.close();
        }
        return trainer;
    }

    @Override
    public Trainer findTrainerByUsername(String username) {
        Session session = sessionFactory.openSession();
        Trainer trainer = null;
        try{
            org.hibernate.query.Query<Trainer> query = session.createQuery("select t from Trainer t where t.user.username = :username").setParameter("username", username);
            trainer = query.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            session.close();
        }
        return trainer;
    }
}
