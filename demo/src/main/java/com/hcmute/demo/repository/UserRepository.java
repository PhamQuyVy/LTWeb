package com.hcmute.demo.repository;

import com.hcmute.demo.entity.User;
import com.hcmute.demo.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class UserRepository {

    public User findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public User findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
    public void updateProfile(int id, String fullName, String phone, String avatar) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            User user = em.find(User.class, id);
            if (user == null) {
                throw new IllegalArgumentException("Khong tim thay tai khoan.");
            }

            user.setFullName(fullName);
            user.setPhone(phone);
            if (avatar != null) {
                user.setAvatar(avatar);
            }

            em.getTransaction().commit();

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}