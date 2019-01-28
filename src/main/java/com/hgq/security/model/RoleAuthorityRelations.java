package com.hgq.security.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "role_authority_relations", indexes = {
        @Index(name = "uk_role_id_authority_id", columnList = "role_id, authority_id", unique = true),
        @Index(name = "idx_authority_id", columnList = "authority_id")})
public class RoleAuthorityRelations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id", updatable = false, columnDefinition = "bigint unsigned")
    private Long relationId;
    @Column(name = "role_id", nullable = false, columnDefinition = "bigint unsigned")
    private Long roleId;
    @Column(name = "authority_id", nullable = false, columnDefinition = "bigint unsigned")
    private Long authorityId;
}
