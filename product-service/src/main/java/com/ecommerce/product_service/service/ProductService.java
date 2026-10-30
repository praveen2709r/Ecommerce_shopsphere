package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest productRequest);
    Page<ProductResponse> findAll(int page,int size,String sortBy,String direction);
    ProductResponse findById(Long id);
    ProductResponse updateById(Long id, ProductRequest productRequest);
    void deleteById(Long id);
}
