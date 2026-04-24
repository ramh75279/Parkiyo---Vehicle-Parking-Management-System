package com.parkiyo.parkiyo.model;

import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpiry;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean emailNotificationsEnabled = true;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean smsNotificationsEnabled = true;

    private String profileImagePath;

    private LocalDateTime lastLoginAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Wallet wallet;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getInitials() {
        String firstInitial = firstName != null && !firstName.isBlank()
                ? firstName.substring(0, 1)
                : "";
        String lastInitial = lastName != null && !lastName.isBlank()
                ? lastName.substring(0, 1)
                : "";
        return (firstInitial + lastInitial).toUpperCase(Locale.ROOT);
    }
}
