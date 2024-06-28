package com.example.bidding.entity;

import com.example.bidding.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RefreshToken extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    private Long refreshTokenId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    private Instant expiryDate;

}
