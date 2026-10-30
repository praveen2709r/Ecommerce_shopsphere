package com.ecommerce.product_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String sku,
        Integer quantity,
        String category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}