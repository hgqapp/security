package com.hgq.security.support.jpa;

import javax.persistence.EntityManager;

public class EntityManagerAwareImpl implements EntityManagerAware {

    private final EntityManager manager;

    public EntityManagerAwareImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public EntityManager getEntityManager() {
        return manager;
    }
}
