package com.hgq.security.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_role_relations", indexes = {
        @Index(name = "uk_user_id_role_id", columnList = "user_id, role_id", unique = true),
        @Index(name = "idx_role_id", columnList = "role_id")})
public class UserRoleRelations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id", updatable = false, columnDefinition = "bigint unsigned")
    private Long relationId;
    @Column(name = "user_id", nullable = false, columnDefinition = "bigint unsigned")
    private Long userId;
    @Column(name = "role_id", nullable = false, columnDefinition = "bigint unsigned")
    private Long roleId;

}
