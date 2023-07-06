package com.mobicoolsoft.electronic.store.dto;

import jakarta.persistence.Column;
import lombok.*;

/**
 * @author Sandip Kolhekar
 * @apiNote create User DTO class to map with User entity class
 * @since 2021
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    private String name;

    private String email;

    private String password;

    private String about;

    private String gender;

    private String image;
}
