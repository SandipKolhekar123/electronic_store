package com.mobicoolsoft.electronic.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntityAudit{

    @Column(name = "product_title")
    private String title;

    @Column(name = "product_desc")
    private String description;

    @Column(name = "product_image")
    private String image;

    @Column(name = "product_price")
    private Integer price;

    @Column(name = "product_discount")
    private Integer discount;

    @Column(name = "product_quantity")
    private Integer quantity;

    private  Boolean live;

    private Boolean stock;

    @ManyToOne
    @JoinColumn(name = "fk_category_id")
    private Category category;

}
