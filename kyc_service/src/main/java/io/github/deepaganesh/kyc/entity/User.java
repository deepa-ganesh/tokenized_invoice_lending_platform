package io.github.deepaganesh.kyc.entity;

import io.github.deepaganesh.kyc.common.KycStatus;
import io.github.deepaganesh.kyc.common.UserType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;
}
