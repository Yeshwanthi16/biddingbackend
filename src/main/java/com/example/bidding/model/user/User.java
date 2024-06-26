package com.example.bidding.model.user;


import com.example.bidding.model.AbstractEntity;
import com.example.bidding.service.Converter.RoleEnumConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
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
}
