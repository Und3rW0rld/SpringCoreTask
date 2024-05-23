package com.uw.dao;

import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.model.User;
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

@Repository
public class TraineeDaoImpl implements TraineeDao{

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory( SessionFactory sessionFactory ){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Trainee trainee) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.persist(trainee);
            transaction.commit();
        }catch( Exception e ){
            transaction.rollback();
        }finally{
            session.close();
        }
    }

    @Override
    public void update(Trainee trainee) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.merge(trainee);
            transaction.commit();
        }catch(Exception e){
            transaction.rollback();
        }finally{
            session.close();
        }
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            Trainee traineeToDelete = session.get(Trainee.class, id);
            session.remove(traineeToDelete);
            transaction.commit();
        }catch(Exception e){
            transaction.rollback();
        }finally{
            session.close();
        }
    }

    @Override
    public Trainee selectProfile(long id) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;
        Trainee trainee = null;
        try{
            trainee = session.get (Trainee.class, id);
        }catch ( Exception e){
            e.printStackTrace();
        }finally{
            session.close();
        }
        return trainee;
    }

    @Override
    public List<Trainee> findAll() {
        Session session = sessionFactory.openSession();
        List<Trainee> traineeList = null;

        try {
            // Crear un objeto Criteria para la clase de entidad User
            Query query = session.createQuery("select t from Trainee t", Trainee.class);
            traineeList = query.getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch( NoResultException e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return traineeList;
    }

    @Override
    public Trainee existTraineeByUserId(long id) {
        Session session = sessionFactory.openSession();
        Trainee trainee = null;
        try{
            org.hibernate.query.Query<Trainee> query = session.createQuery("select t from Trainee t where t.user.id = :id", Trainee.class).setParameter("id", id);
            trainee = query.getSingleResult();
        }catch ( NoResultException e ){
            // Ignore this
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            session.close();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return trainers;
    }

}
