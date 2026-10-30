package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.InvalidPaginationException;
import com.ecommerce.product_service.exception.ProductAlreadyExistsException;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    @Override
    public ProductResponse create(ProductRequest productRequest) {
        if(productRepository.existsBySku(productRequest.sku())){
            throw new ProductAlreadyExistsException("Product is already available with "+productRequest.sku());
        }
        Product product=productMapper.toEntity(productRequest);
        product.setActive(true);
        Product savedProduct=productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public Page<ProductResponse> findAll(int page, int size, String sortBy, String direction) {
        if (page < 0) {
            throw new InvalidPaginationException("Page number cannot be negative");
        }
        if (size < 1 || size > 100) {
            throw new InvalidPaginationException("Page size must be between 1 and 100");
        }
        Set<String> allowedFields=Set.of("id","name","price","quantity","category","createdAt","updatedAt");
        if (sortBy == null || !allowedFields.contains(sortBy)) {
            throw new InvalidPaginationException("Invalid sort field: " + sortBy);
        }
        if(!allowedFields.contains(sortBy)){
            throw new InvalidPaginationException(sortBy+" field is not allowed");
        }
        if (direction == null || (!direction.equalsIgnoreCase("asc")
                        && !direction.equalsIgnoreCase("desc"))) {
            throw new InvalidPaginationException("Invalid sort direction. Allowed values are 'asc' and 'desc'");
        }
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findByActiveTrue(pageable)
                .map(productMapper::toResponse);
    }
    @Override
    public ProductResponse findById(Long id) {
        Product product=getProduct(id);
        return productMapper.toResponse(product);
    }
    private Product getProduct(Long id){
        return productRepository.findByActiveTrueAndId(id).orElseThrow(
            ()->new ProductNotFoundException("Product not available with id "+id)
        );
    }
    @Override
    public ProductResponse updateById(Long id, ProductRequest productRequest) {

        Product product=getProduct(id);
        if (!product.getSku().equals(productRequest.sku())
                && productRepository.existsBySku(productRequest.sku())) {
            throw new ProductAlreadyExistsException(
                    "Product already exists with SKU " + productRequest.sku()
            );
        }
        productMapper.updateProductFromDto(productRequest,product);
        Product updatedProduct=productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteById(Long id) {
        Product product=getProduct(id);
        product.setActive(false);
        productRepository.save(product);
    }
}
