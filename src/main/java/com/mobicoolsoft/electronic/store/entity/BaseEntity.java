package com.mobicoolsoft.electronic.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Setter
@Getter
/**
 * @implSpec Hibernate Inheritance Mapping
 * @implSpec MappedSuperclass : is used to ensure that the BaseEntity class
 * will not have a separate representation as the table of the extending class.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    /**
     * @implSpec public abstract class BaseEntity : to prevent developers from instantiating an
     * instance of this class. Only extending classes can instantiate an instance
     */
    @Id
    @Column(updatable = false)
    private String id;
}
