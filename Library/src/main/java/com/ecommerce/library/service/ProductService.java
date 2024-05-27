package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();

    Product save(List<MultipartFile>imageProduct, ProductDto productDto);

    Product update(List<MultipartFile> imageProduct, ProductDto productDto);

    void enableById(Long id);

    void deleteById(Long id);

     ProductDto getById(Long id);

    Page<Product> pageProducts(int pageNo);

    Page<ProductDto> searchProducts(int pageNo, String keyword);

    Product getProductById(long id);

    List<ProductDto> findAllProducts();


     List<ProductDto> filterHighProducts();

     List<ProductDto> filterLowerProducts();

     List<ProductDto> listViewProducts();


    List<Product> findAllByCategory(long id);


    long countTotalProducts();

    List<Object[]> getProductStats();

    List<Object[]> getProductsStatsBetweenDates(Date startDate,Date endDate);

    Product findById(Long id);


    List<Object[]> findTopSellingProducts(Pageable pageable);






}
