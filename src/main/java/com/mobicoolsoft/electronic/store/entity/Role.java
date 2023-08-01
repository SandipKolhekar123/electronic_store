package com.mobicoolsoft.electronic.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "roles")
public class Role {

    @Id
    private int roleId;

    @Column(name = "role_name")
    private String roleName;


}
