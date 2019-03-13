package com.hgq.security.support.jpa;

import com.hgq.security.support.jpa.querydsl.JPAQueryAware;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author houguangqiang
 * @date 2019-03-13
 * @since 1.0
 */
@NoRepositoryBean
public interface BaseRepository<T, ID>  extends CrudRepository<T, ID>, QuerydslPredicateExecutor<T>, JPAQueryAware {
}
