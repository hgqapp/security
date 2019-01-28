package com.hgq.security.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles", indexes = {
        @Index(name = "uk_role", columnList = "role", unique = true),
        @Index(name = "uk_role_name", columnList = "role_name", unique = true)
})
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", updatable = false, columnDefinition = "bigint unsigned")
    private Long roleId;
    @Column(name = "role_name", length = 50, nullable = false)
    private String roleName;
    @Column(name = "role", length = 50, nullable = false)
    private String role;
    @Transient
    @OneToMany(targetEntity = UserRoleRelations.class, mappedBy = "role")
    private List<Users> users;
}
