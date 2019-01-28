package com.hgq.security.repository;

import com.hgq.security.model.UserRoleRelations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author houguangqiang
 * @date 2019-01-28
 * @since 1.0
 */
@Repository
public interface UserRoleRelationsRepository extends CrudRepository<UserRoleRelations, Long> {
}
