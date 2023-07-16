package com.mobicoolsoft.electronic.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "cat_id")
    private String id;

    @Column(name = "cat_title")
    private String title;

    @Column(name = "cat_description")
    private String description;

    private String coverImage;


}
