package com.hgq.security.repository;

import com.hgq.security.model.Users;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Repository
public interface UserRepository extends CrudRepository<Users, Long>, QuerydslPredicateExecutor<Users> {

}
