package com.hgq.security.repository;

import com.hgq.security.model.Authorities;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends CrudRepository<Authorities, Long> {

    /**
     * 根据用户id获取用户权限
     *
     * @param userId
     * @returnA
     */
//    @Query("select a from Authorities a, GroupAuthorityRelations gar, user_group_relations ugr " +
//            "where ugr.user_id=?1 and ugr.group_id=gar.group_id and gar.authority_id=a.authority_id")
    @Query("select a from Authorities a, GroupAuthorityRelations gar, UserGroupRelations ugr " +
            "where ugr.userId = ?1 and a.authorityId = gar.authorityId and gar.groupId = ugr.groupId")
    List<Authorities> findAuthorityByUserId(Long userId);
}
