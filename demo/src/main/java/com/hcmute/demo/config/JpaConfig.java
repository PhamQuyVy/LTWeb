package com.hcmute.demo.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaConfig {
    
    private static final String PERSISTENCE_UNIT_NAME = "demoPU";

    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    private JpaConfig() {
    }

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void shutdown() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
