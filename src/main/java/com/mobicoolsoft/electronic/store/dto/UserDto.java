package com.mobicoolsoft.electronic.store.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobicoolsoft.electronic.store.validate.ImageNameValid;
import jakarta.validation.constraints.*;
import lombok.*;

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

    @NotBlank
    @Size(min = 4, max = 20, message = "Username must be at least 4 characters !")
    private String name;

    /**
     * @implNote regex for email validation
     */
    @NotEmpty(message = "Email is required !")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Please enter valid Email Id !")
    private String email;

    /**
     * @implNote regex for password validation
     * 	^ - start-of-string
     * 	(?=.*[0-9]) - a digit must occur at least once
     * 	(?=.*[a-z]) - a lower case letter must occur at least once
     *  (?=.*[A-Z]) - an upper case letter must occur at least once
     *  (?=.*[@#$%^&]) - a special character must occur at least once
     *	(?=\S+$) - no whitespace allowed in the entire string
     * 	.{6,} - anything, at least six places
     *	$ - end-of-string
     */
    @NotBlank(message = "password  is required !")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&])(?=\\S+$).{6,}$" , message = "Please enter valid password !")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Size(min = 4, max = 6, message = "Invalid gender!")
    private String gender;

    private String about;

    @ImageNameValid
    private String image;

}
