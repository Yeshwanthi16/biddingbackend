package com.example.bidding.entity.user;


import com.example.bidding.entity.AbstractEntity;
import com.example.bidding.service.Converter.RoleEnumConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "_user")
public class User extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "user-sequence-generator")
    private Long userId;

    @Email(regexp = "[^@]+@[^@]+\\.[^@.]+", message = "Email is not valid")
    @Column(unique = true)
    private String email;

    @JsonIgnore
    @NotBlank
    private String password;

    @NotBlank
    @Column(unique = true)
    private String username;

    @Convert(converter = RoleEnumConverter.class)
    private RoleEnum roleName;

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
