package com.hgq.security.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_group_relations", indexes = {
        @Index(name = "uk_user_id_group_id", columnList = "user_id, group_id", unique = true),
        @Index(name = "idx_group_id", columnList = "group_id")})
public class UserGroupRelations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id", updatable = false, columnDefinition = "bigint unsigned")
    private Long relationId;
    @Column(name = "user_id", nullable = false, columnDefinition = "bigint unsigned")
    private Long userId;
    @Column(name = "group_id", nullable = false, columnDefinition = "bigint unsigned")
    private Long groupId;
}
