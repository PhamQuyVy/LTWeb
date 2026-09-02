package com.hcmute.demo.dao.impl;

import java.sql.Timestamp;

import com.hcmute.demo.config.JpaConfig;
import com.hcmute.demo.dao.AccountDao;
import com.hcmute.demo.entity.Account;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class AccountDaoImpl implements AccountDao {

    @Override
    public void insert(Account account) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(account);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public Account getByEmail(String email) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            TypedQuery<Account> query =
                    enma.createNamedQuery("Account.findByEmail", Account.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            enma.close();
        }
    }

    @Override
    public Account getByUsername(String username) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            TypedQuery<Account> query =
                    enma.createNamedQuery("Account.findByUsername", Account.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            enma.close();
        }
    }

    @Override
    public Account get(int id) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            return enma.find(Account.class, id);
        } finally {
            enma.close();
        }
    }

    @Override
    public void updateOtp(int id, String otpCode, Timestamp otpExpiry) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Account account = enma.find(Account.class, id);
            if (account != null) {
                account.setOtpCode(otpCode);
                account.setOtpExpiry(otpExpiry);
                enma.merge(account);
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void activate(int id) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Account account = enma.find(Account.class, id);
            if (account != null) {
                account.setStatus("ACTIVE");
                account.setOtpCode(null);
                account.setOtpExpiry(null);
                enma.merge(account);
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }
        @Override
    public void updatePassword(int id, String newPassword) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Account account = enma.find(Account.class, id);
            if (account != null) {
                account.setPassword(newPassword);
                enma.merge(account);
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }
}