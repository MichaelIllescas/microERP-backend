// src/main/java/com/imperialnet/product_service/infrastructure/config/jpa/AuditingConfig.java
package com.imperialnet.product_service.infrastructure.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing // habilita @CreatedDate y @LastModifiedDate
public class AuditingConfig {

    // Solo si más adelante usás createdBy/lastModifiedBy
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("system"); // luego podés leer del SecurityContext
    }
}
