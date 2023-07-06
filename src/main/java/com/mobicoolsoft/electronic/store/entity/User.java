package com.mobicoolsoft.electronic.store.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", nullable = false, unique = true)
    private String password;

    @Column(name = "about_user")
    private String about;

    @Column(name = "user_gender")
    private String gender;

    @Column(name = "user_image")
    private String image;











        /**
         * @return the email as a primary key
         */
     /*   @Id
        @Column(name = "email", unique = true, nullable = false)
        public String getEmail() {
            return email;
        }*/

}
