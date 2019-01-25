package com.hgq.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users", indexes = {
        @Index(name = "uk_username", columnList = "username", unique = true),
        @Index(name = "idx_email", columnList = "email", unique = true),
        @Index(name = "idx_phone", columnList = "phone", unique = true),
        @Index(name = "idx_create_time", columnList = "create_time"),
        @Index(name = "idx_update_time", columnList = "update_time")})
@DynamicInsert
@DynamicUpdate
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, columnDefinition = "bigint unsigned")
    private Long userId;
    @Column(name = "username", length = 50, nullable = false)
    private String username;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 100)
    private String email;
    @Column(length = 50)
    private String phone;
    @Column(nullable = false, columnDefinition = "boolean default 1")
    private Boolean enabled;
    @Column(name = "create_time", updatable = false, nullable = false, columnDefinition = "bigint unsigned")
    private Long createTime;
    @Column(name = "update_time", nullable = false, columnDefinition = "bigint unsigned")
    private Long updateTime;

}
