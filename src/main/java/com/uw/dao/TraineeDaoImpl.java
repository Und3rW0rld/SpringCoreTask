package com.uw.dao;

import com.uw.model.Trainee;
import com.uw.model.Trainer;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class TraineeDaoImpl implements TraineeDao{

    private SessionFactory sessionFactory;
    private static final Logger logger = Logger.getLogger(TraineeDaoImpl.class.getName());
    @Autowired
    public void setSessionFactory( SessionFactory sessionFactory ){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Trainee trainee) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.persist(trainee);
            transaction.commit();
            logger.fine("Trainee created successfully");
        }catch( HibernateException | NoResultException e ){
            if(transaction != null) {
                transaction.rollback();
            }
            logger.severe("Error while creating transaction");
        }
    }

    @Override
    public void update(Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(trainee);
            transaction.commit();
            logger.fine("Trainee updated successfully");
        } catch ( HibernateException | NoResultException e) {
            logger.severe("Failed to update trainee");
        }
    }

    @Override
    public void delete(long id) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            Trainee traineeToDelete = session.get(Trainee.class, id);
            session.remove(traineeToDelete);
            transaction.commit();
            logger.fine("Trainee deleted successfully");
        }catch( HibernateException | NoResultException e){
            if(transaction != null){
                transaction.rollback();
            }
            logger.severe("Failed to delete trainee");
        }
    }

    @Override
    public Trainee selectProfile(long id) {
        Trainee trainee = null;
        try( Session session = this.sessionFactory.openSession() ){
            trainee = session.get (Trainee.class, id);
        }catch ( HibernateException | NoResultException e){
            logger.severe("Could not find trainee instance");
            logger.severe(e.getMessage());
        }
        return trainee;
    }

    @Override
    public List<Trainee> findAll() {
        List<Trainee> traineeList = List.of();

        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("select t from Trainee t", Trainee.class);
            traineeList = query.getResultList();
        } catch (HibernateException | NoResultException e) {
            logger.severe("Failed to find trainees");
            logger.severe(e.getMessage());
        }
        return traineeList;
    }

    @Override
    public Trainee existTraineeByUserId(long id) {
        Trainee trainee = null;
        try (Session session = sessionFactory.openSession()) {
            org.hibernate.query.Query<Trainee> query = session.createQuery("select t from Trainee t where t.user.id = :id", Trainee.class).setParameter("id", id);
            trainee = query.getSingleResult();
        }catch ( HibernateException | NoResultException e ){
            logger.severe("Could not find trainee instance");
            logger.severe(e.getMessage());
        }
        return trainee;
    }

    @Override
    public Trainee findTraineeByUsername(String username) {
        Trainee trainee = null;
        try( Session session = sessionFactory.openSession()){
            org.hibernate.query.Query<Trainee> query = session.createQuery("select t from Trainee t where t.user.username = :username", Trainee.class).setParameter("username", username);
            trainee = query.getSingleResult();
        }catch(Exception e){
            logger.severe("Could not find trainee instance");
            logger.severe(e.getMessage());
        }
        return trainee;
    }

    @Override
    public boolean activeDeActiveProfile(Trainee trainee) {
        Transaction transaction;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            trainee.getUser().setActive(!trainee.getUser().isActive());
            session.merge(trainee);
            transaction.commit();
            return trainee.getUser().isActive();
        }catch (Exception e){
            logger.severe("Something went wrong: Active or deactivate process failed");
            logger.severe(e.getMessage());
        }
        return false;
    }

    @Override
    public void deleteTraineeByUsername(String username) {
        Transaction transaction;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            Trainee trainee = this.findTraineeByUsername(username);
            session.remove(trainee);
            transaction.commit();
        }catch ( Exception e ){
            logger.severe("Failed to delete trainee By Username");
            logger.severe(e.getMessage());
        }
    }

    @Override
    public List<Trainer> findTrainers(Trainee trainee) {
        List<Trainer> trainers = new ArrayList<>();
        try(Session session = sessionFactory.openSession()){
            String hql = "select t from Trainer t join t.trainees tr where tr.id = :traineeId";
            trainers = session.createQuery(hql, Trainer.class)
                    .setParameter("traineeId", trainee.getId())
                    .getResultList();
        }catch ( Exception e ){
            logger.severe("Failed to find trainers of the trainee");
            logger.severe(e.getMessage());
        }
        return trainers;
    }

}
