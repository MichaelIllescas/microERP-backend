package com.imperialnet.customer_service.infrastructure.persistence.entity;

import com.imperialnet.customer_service.infrastructure.config.jpa.AuditingConfig;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa a un Cliente en la base de datos.
 */
@Entity
@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_customers_email", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity  extends AuditingConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, length = 20)
    private String status; // ACTIVE, INACTIVE, etc.
}
