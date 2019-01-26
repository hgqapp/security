package com.hgq.security.repository;

import com.hgq.security.model.Authorities;
import com.hgq.security.model.QAuthorities;
import com.hgq.security.model.QGroupAuthorityRelations;
import com.hgq.security.model.QUserGroupRelations;
import com.hgq.security.support.jpa.querydsl.JPAQueryAware;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends CrudRepository<Authorities, Long>, QuerydslPredicateExecutor<Authorities>, JPAQueryAware {

    /**
     * 根据用户id获取用户权限
     *
     * @param userId
     * @returnA
     */
//    @Query("select a from Authorities a, GroupAuthorityRelations gar, UserGroupRelations ugr " +
//            "where ugr.userId = ?1 and a.authorityId = gar.authorityId and gar.groupId = ugr.groupId")
    default List<Authorities> selectByUserId(Long userId) {
        QAuthorities authorities = QAuthorities.authorities;
        QGroupAuthorityRelations groupAuthorityRelations = QGroupAuthorityRelations.groupAuthorityRelations;
        QUserGroupRelations userGroupRelations = QUserGroupRelations.userGroupRelations;
        return query().select(authorities)
                .from(authorities)
                .join(groupAuthorityRelations).on(authorities.authorityId.eq(groupAuthorityRelations.authorityId))
                .join(userGroupRelations).on(userGroupRelations.groupId.eq(groupAuthorityRelations.groupId)
                        .and(userGroupRelations.userId.eq(userId)))
                .fetch();
    }
}
