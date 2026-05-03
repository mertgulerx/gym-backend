package com.ytu.gymbackend.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hashedPassword;

    private String backupSecret;

    @CreationTimestamp
    private LocalDateTime accountCreationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany
    private List<UserSession> userSessions = new ArrayList<>();
}
