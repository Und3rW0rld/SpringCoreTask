package com.uw.dao;

import com.uw.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory( SessionFactory sessionFactory ){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean validateUsername(String username) {
        Session session = sessionFactory.openSession();
        User user = null;
        try{
            Query<User> query = session.createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username);
            user = query.getSingleResult();
        }catch( Exception e ){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return user != null;
    }

    @Override
    public boolean validatePassword(String username, String password) {
        Session session = sessionFactory.openSession();
        User user = null;
        try{
            Query<User> query = session.createQuery("select u from User u where u.username = :username and u.password = :password", User.class).setParameter("username", username).setParameter("password", password);
            user = query.getSingleResult();
        }catch( Exception e ){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return user != null;
    }

    @Override
    public User findUserByUsername(String username) {
        Session session = sessionFactory.openSession();
        User user = null;
        try{
            Query<User> query = session.createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username);
            user = query.getSingleResult();
        }catch( Exception e ){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return user;
    }

    @Override
    public boolean updateUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            return true;
        } catch( Exception e ){
            e.printStackTrace();
            transaction.rollback();
        }finally {
            session.close();
        }
        return false;
    }
}
