package com.parkiyo.parkiyo.model;

import com.parkiyo.parkiyo.enums.VehicleCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleCategory category;

    private String make;
    private String model;
    private String color;

    @Column(name = "manufacture_year")
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;   // Can be null for quick/visitor registration

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ParkingRecord> parkingRecords = new ArrayList<>();

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ==================== HELPER METHODS ====================

    public String getFullName() {
        if (make == null && model == null) return licensePlate;
        return (make != null ? make : "") + " " +
                (model != null ? model : "") + " (" + licensePlate + ")";
    }

    public boolean isRegisteredToUser() {
        return user != null;
    }
}