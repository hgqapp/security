package com.hgq.security.support.jpa;

import javax.persistence.EntityManager;

public interface EntityManagerAware {

    EntityManager getEntityManager();

}
