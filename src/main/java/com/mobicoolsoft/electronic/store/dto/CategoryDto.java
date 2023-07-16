package com.mobicoolsoft.electronic.store.dto;

import com.mobicoolsoft.electronic.store.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private String id;

    @NotBlank(message = "category title must not be empty or null!")
    @Size(min = 5, max = 50, message = "category title must be in the range of 5 to 50 characters!")
    private String title;

    @NotBlank(message = "category title must not be empty or null!")
    @Size(min = 5, max = 500, message = "category description must be in the range of 5 to 50 characters!")
    private String description;

    @ImageNameValid
    private String coverImage;

}
