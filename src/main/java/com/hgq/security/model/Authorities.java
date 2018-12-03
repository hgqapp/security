package com.hgq.security.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "authorities", indexes = {@Index(name = "uk_authority", columnList = "authority", unique = true)})
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id", updatable = false, columnDefinition = "bigint unsigned")
    private Long authorityId;
    @Column(name = "authority", length = 50, nullable = false)
    private String authority;
    @Column(name = "description", length = 200)
    private String description;
    /** 1=接口，2=菜单，3=按钮，4=目录 */
    @Column(name = "type", nullable = false)
    private Byte type;
}
