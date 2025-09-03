package com.imperialnet.sales_service.infrastructure.persistence.repository;

import com.imperialnet.sales_service.infrastructure.persistence.entity.SaleEntity;
import com.imperialnet.sales_service.infrastructure.persistence.entity.SaleStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataSaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByCustomerId(Long customerId);
    List<SaleEntity> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<SaleEntity> findByStatus(SaleStatusEntity status);
}
