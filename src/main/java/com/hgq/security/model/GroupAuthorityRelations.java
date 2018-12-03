package com.hgq.security.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "group_authority_relations", indexes = {
        @Index(name = "uk_group_id_authority_id", columnList = "group_id, authority_id", unique = true),
        @Index(name = "idx_authority_id", columnList = "authority_id")})
public class GroupAuthorityRelations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id", updatable = false, columnDefinition = "bigint unsigned")
    private Long relationId;
    @Column(name = "group_id", nullable = false, columnDefinition = "bigint unsigned")
    private Long groupId;
    @Column(name = "authority_id", nullable = false, columnDefinition = "bigint unsigned")
    private Long authorityId;
}
