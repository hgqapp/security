package com.hgq.security.repository;

import com.hgq.security.model.QUserGroupRelations;
import com.hgq.security.model.QUsers;
import com.hgq.security.model.Users;
import com.hgq.security.support.jpa.querydsl.JPAQueryAware;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Repository
public interface UserRepository extends CrudRepository<Users, Long>, QuerydslPredicateExecutor<Users>, JPAQueryAware {

    default List<Users> selectByGroupId(Long groupId){
        QUsers users = QUsers.users;
        QUserGroupRelations relations = QUserGroupRelations.userGroupRelations;
        return query().select(users).from(users)
                .innerJoin(relations)
                .on(users.userId.eq(relations.userId).and(relations.groupId.eq(groupId))).fetch();
    }
}
