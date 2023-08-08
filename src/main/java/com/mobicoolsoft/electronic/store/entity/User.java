package com.mobicoolsoft.electronic.store.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sandip Kolhekar
 * @apiNote create User entity class to map with users table
 * @since 2021
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "users")
public class User extends BaseEntityAudit{

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_gender")
    private String gender;

    @Column(name = "about_user")
    private String about;

    @Column(name = "user_image")
    private String image;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="user_roles",
    joinColumns=@JoinColumn (name ="user", referencedColumnName = "id" ),
    inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "roleId"))
    private Set<Role> roles = new HashSet<>();



/**
         * @return the email as a primary key
         */
     /*   @Id
        @Column(name = "email", unique = true, nullable = false)
        public String getEmail() {
            return email;
        }*/

}
