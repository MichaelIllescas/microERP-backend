package com.imperialnet.sales_service.application.usecase;

import com.imperialnet.sales_service.application.dto.*;
import com.imperialnet.sales_service.application.port.in.CreateSaleUseCase;
import com.imperialnet.sales_service.application.port.out.CustomerServicePort;
import com.imperialnet.sales_service.application.port.out.ProductServicePort;
import com.imperialnet.sales_service.application.port.out.SaleRepositoryPort;
import com.imperialnet.sales_service.application.port.out.StockServicePort;
import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.domain.model.SaleItem;
import com.imperialnet.sales_service.domain.model.SaleStatus;
import com.imperialnet.sales_service.infrastructure.adapter.out.SaleEventPublisher;
import com.imperialnet.sales_service.infrastructure.mapper.SaleDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterSaleService implements CreateSaleUseCase {

    private final SaleRepositoryPort saleRepositoryPort;
    private final ProductServicePort productServicePort;
    private final StockServicePort stockServicePort;
    private final SaleDtoMapper saleDtoMapper;
    private final CustomerServicePort customerServicePort;
    private final SaleEventPublisher saleEventPublisher; // 👈 agregado

    @Override
    public SaleResponse registerSale(CreateSaleRequest request) {
        log.info("▶️ Iniciando registro de venta para el cliente {}", request.getCustomerId());

        // 1. Mapear DTO -> dominio
        Sale sale = saleDtoMapper.toDomain(request);
        log.debug("📦 Venta mapeada desde DTO: {}", sale);

        // 2. Validar cliente
        log.debug("🔎 Validando existencia del cliente {}", request.getCustomerId());
        CustomerResponse customer = customerServicePort.getCustomerById(request.getCustomerId());

        // 3. Enriquecer items con precios y validar stock
        List<SaleItem> enrichedItems = sale.getItems().stream()
                .map(item -> {
                    log.debug("🔍 Consultando producto {} en ProductService", item.getProductId());
                    ProductResponse product = productServicePort.getProductById(item.getProductId());

                    log.debug("🔍 Validando stock disponible para producto {}", item.getProductId());
                    if (!stockServicePort.hasSufficientStock(item.getProductId(), item.getQuantity())) {
                        log.warn("❌ Stock insuficiente para producto {} (cantidad solicitada: {})",
                                item.getProductId(), item.getQuantity());
                        throw new IllegalStateException(
                                "Stock insuficiente para el producto " + product.getName()
                        );
                    }

                    return SaleItem.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .price(product.getPrice()) // precio oficial desde ProductService
                            .productName(product.getName()) // 👈 agregado para el evento
                            .build();
                })
                .collect(Collectors.toList());

        // 4. Calcular total
        double totalAmount = enrichedItems.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        log.info("💰 Total calculado de la venta: {}", totalAmount);

        // 5. Completar venta
        sale.setItems(enrichedItems);
        sale.setTotalAmount(totalAmount);
        sale.setStatus(SaleStatus.CONFIRMED);
        sale.setCreatedAt(LocalDateTime.now());

        // 6. Guardar en BD
        log.info("💾 Guardando venta en la base de datos...");
        Sale saved = saleRepositoryPort.save(sale);
        log.info("✅ Venta guardada con ID {}", saved.getId());

        // 7. Actualizar stock en cada producto
        enrichedItems.forEach(item -> {
            log.info("📉 Restando {} unidades al producto {}", item.getQuantity(), item.getProductId());
            stockServicePort.decreaseStock(item.getProductId(), item.getQuantity());
        });

        // 8. Mapear a respuesta DTO
        SaleResponse response = saleDtoMapper.toResponse(saved);
        log.info("📤 Venta registrada correctamente: {}", response);

        // 9. Publicar evento en RabbitMQ
        SaleCreatedEvent event = SaleCreatedEvent.builder()
                .saleId(saved.getId())
                .totalAmount(saved.getTotalAmount())
                .createdAt(saved.getCreatedAt())
                .customerId(customer.getId())
                .customerName(customer.getFirstName() + " " + customer.getLastName())
                .customerEmail(customer.getEmail())
                .items(enrichedItems.stream().map(i ->
                        SaleItemEvent.builder()
                                .productId(i.getProductId())
                                .productName(i.getProductName())
                                .quantity(i.getQuantity())
                                .price(i.getPrice())
                                .build()
                ).toList())
                .build();

        log.info("📨 Publicando evento de venta creada: {}", event);
        saleEventPublisher.publishSaleCreated(event);

        return response;
    }
}
