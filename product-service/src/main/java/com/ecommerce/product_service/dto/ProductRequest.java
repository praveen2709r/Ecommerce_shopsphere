package com.ecommerce.product_service.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "Product name is required")
        @Size(max = 150)
        String name,

        @NotBlank(message = "Description is required")
        @Size(max = 500)
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin("0.01")
        @Digits(integer = 10, fraction = 2)
        BigDecimal price,

        @NotBlank(message = "SKU is required")
        @Size(max = 50)
        String sku,

        @NotNull(message = "Quantity is required")
        @PositiveOrZero
        Integer quantity,

        @NotBlank(message = "Category is required")
        @Size(max = 100)
        String category

) {}