package com.mobicoolsoft.electronic.store.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Sandip Kolhekar
 * @apiNote create User DTO class to map with User entity
 * @since 2021
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @NotEmpty
    @Size(min = 4, max = 50, message = "Username must be atleast 4 characters !")
    private String name;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&])(?=\\S+$).{6,}$" , message = "Please enter valid password !")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String about;

    private String gender;

    private String image;
}
