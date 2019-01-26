package com.hgq.security.support.jpa.querydsl;

import com.hgq.security.support.jpa.EntityManagerAware;
import com.querydsl.jpa.impl.JPAQuery;

public interface JPAQueryAware extends EntityManagerAware {

    default <T> JPAQuery<T> query() {
        return new JPAQuery<>(getEntityManager());
    }
}
