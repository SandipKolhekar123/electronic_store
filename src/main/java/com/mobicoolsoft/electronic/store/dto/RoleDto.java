package com.mobicoolsoft.electronic.store.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RoleDto {

    private int roleId;

    private String roleName;
}
