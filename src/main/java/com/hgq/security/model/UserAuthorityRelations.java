package com.hgq.security.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_authority_relations", indexes = {
        @Index(name = "uk_user_id_authority_id", columnList = "user_id, authority_id", unique = true),
        @Index(name = "idx_authority_id", columnList = "authority_id")})
public class UserAuthorityRelations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id", updatable = false, columnDefinition = "bigint unsigned")
    private Long relationId;
    @Column(name = "user_id", nullable = false, columnDefinition = "bigint unsigned")
    private Long userId;
    @Column(name = "authority_id", nullable = false, columnDefinition = "bigint unsigned")
    private Long authorityId;
}
