package com.imperialnet.stock_service.infraestructure.persistence.entity;

import com.imperialnet.stock_service.infraestructure.config.jpa.AuditingConfig;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "stock_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockItemEntity extends AuditingConfig {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, length = 16)
    private String status;
}
