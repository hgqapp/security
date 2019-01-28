package com.hgq.security.repository;

import com.hgq.security.model.Authorities;
import com.hgq.security.support.jpa.querydsl.JPAQueryAware;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authorities, Long>, QuerydslPredicateExecutor<Authorities>, JPAQueryAware {

}
