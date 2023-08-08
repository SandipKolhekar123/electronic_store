package com.mobicoolsoft.electronic.store.dto;

import com.mobicoolsoft.electronic.store.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String id;

    @NotBlank(message = "product title should not be empty or null!")
    @Size(min = 5, max = 100, message = "product title should be within 5 to 100 letters!")
    private String title;

    @NotBlank(message = "product description should not be empty or null!")
    @Size(min = 5, max = 500, message = "product title should be within 5 to 500 letters!")
    private String description;

    @ImageNameValid
    private String image;

    private Double price;

    private Integer discount;

    private Integer quantity;

    private  Boolean live;

    private Boolean stock;

    private CategoryDto category;

    private String createdBy;

    private String updatedBy;

    private Date createdAt;

    private Date updatedAt;
}
