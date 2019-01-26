package com.hgq.security.support.jpa.querydsl;

import com.hgq.security.support.jpa.EntityManagerAwareImpl;

import javax.persistence.EntityManager;

public class JPAQueryAwareImpl extends EntityManagerAwareImpl implements JPAQueryAware {

    public JPAQueryAwareImpl(EntityManager manager) {
        super(manager);
    }

}
