package com.hgq.security.repository;

import com.hgq.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

}
