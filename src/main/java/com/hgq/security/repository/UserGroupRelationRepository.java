package com.hgq.security.repository;

import com.hgq.security.model.UserGroupRelations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author houguangqiang
 * @date 2019-01-28
 * @since 1.0
 */
@Repository
public interface UserGroupRelationRepository extends CrudRepository<UserGroupRelations, Long> {
}
