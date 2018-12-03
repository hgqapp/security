package com.hgq.security.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "`groups`", indexes = {@Index(name = "uk_group_name", columnList = "group_name", unique = true)})
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", updatable = false, columnDefinition = "bigint unsigned")
    private Long groupId;
    @Column(name = "group_name", length = 50, nullable = false)
    private String groupName;
}
