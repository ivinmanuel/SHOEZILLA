package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Image;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ImageRepository;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;


    private final ImageUpload imageUpload;

    @Autowired
    private ImageRepository imageRepository;



    @Override
    public List<ProductDto> findAll() {
        List<Product> products=productRepository.findAll();
        List<ProductDto> productDtoList=transferData(products);
        return productDtoList;
    }


//    @Override
//    public Product save(List<MultipartFile> imageProducts, ProductDto productDto) {
//        Product product = new Product();
//        try {
//            product.setName(productDto.getName());
//            product.setDescription(productDto.getDescription());
//            product.setLong_description(productDto.getLong_description());
//            product.setCurrentQuantity(productDto.getCurrentQuantity());
//            product.setCostPrice(productDto.getCostPrice());
//            product.setSalePrice(productDto.getSalePrice());
//            product.setCategory(productDto.getCategory());
//            product.set_activated(true);
//            Product savedProduct = productRepository.save(product);
//            if (imageProducts == null) {
//                product.setImage(null);
//            } else {
//                List<Image> imagesList = new ArrayList<>();
//                for (MultipartFile imageProduct : imageProducts) {
//                    Image image = new Image();
//                    String imageName = imageUpload.storeFile(imageProduct);
//                    image.setName(imageName);
//                    image.setProduct(product);
//                    imageRepository.save(image);
//                    imagesList.add(image);
//                }
//                product.setImage(imagesList);
//            }
//            return productRepository.save(product);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    @Override
    public Product update(List<MultipartFile> imageProducts, ProductDto productDto) {
        try {
            long id= productDto.getId();
            Product productUpdate = productRepository.getById(productDto.getId());
            if (imageProducts != null && !imageProducts.isEmpty() && imageProducts.size()!=1) {
                List<Image> imagesList = new ArrayList<>();
                List<Image> image = imageRepository.findImageBy(id);
                int i=0;
                for (MultipartFile imageProduct : imageProducts) {
                    String imageName = imageUpload.storeFile(imageProduct);
                    image.get(i).setName(imageName);
                    image.get(i).setProduct(productUpdate);
                    imageRepository.save(image.get(i));
                    imagesList.add(image.get(i));
                    i++;
                }
                productUpdate.setImage(imagesList);
            }
            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setName(productDto.getName());
            productUpdate.setDescription(productDto.getDescription());
            productUpdate.setLong_description(productDto.getLong_description());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setSalePrice(productDto.getSalePrice());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            return productRepository.save(productUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public ProductDto getById(Long id) {
        ProductDto productDto = new ProductDto();
        Product product = productRepository.getById(id);
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setLong_description(product.getLong_description());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setImage(product.getImage());
        productDto.setDeleted(product.is_deleted());
        productDto.setActivated(productDto.isActivated());
        return productDto;
    }




    @Override
    public void enableById(Long id) {
        Product product = productRepository.getById(id);
        product.set_activated(true);
        product.set_deleted(false);
        productRepository.save(product);
    }




    @Override
    public void deleteById(Long id) {
        Product product = productRepository.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        productRepository.save(product);
    }


    @Override
    public Page<Product> pageProducts(int pageNo) {
        Pageable pageable=PageRequest.of(pageNo,8);
        List<ProductDto> products= transferData(productRepository.findAll());
        Page<Product> productPage=toPage(products,pageable);
        return productPage;
    }






    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<ProductDto> productDtoList = transferData(productRepository.searchProductsList(keyword));
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.getById(id);
    }


    private Page toPage(List<ProductDto> list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }



    private List<ProductDto> transferData(List<Product> products) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setDescription(product.getDescription());
            productDto.setLong_description(product.getLong_description());
            productDto.setImage(product.getImage());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @Override
    public List<ProductDto> findAllProducts() {
        List<Product> products=productRepository.findAllByActivatedTrue();
        List<ProductDto>productDtoList = transferData(products);
        return productDtoList;
    }


    @Override
    public List<Product> findAllByCategory(long id) {
        return productRepository.findAllByCategoryId(id);
    }


    @Override
    public List<ProductDto> filterHighProducts() {
        return transferData(productRepository.filterHighProducts());
    }

    @Override
    public List<ProductDto> filterLowerProducts() {
        return transferData(productRepository.filterLowerProducts());
    }





    @Override
    public List<ProductDto> listViewProducts() {
        return transferData(productRepository.listViewProduct());
    }



    @Override
    public Product save(List<MultipartFile> imageProducts, ProductDto productDto) {
        Product product = new Product();
        try {
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setLong_description(productDto.getLong_description());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.setSalePrice(productDto.getSalePrice());
            product.setCategory(productDto.getCategory());
            product.set_activated(true);

            // Save the product first to get its ID
            Product savedProduct = productRepository.save(product);

            if (imageProducts != null && !imageProducts.isEmpty()) {
                List<Image> imagesList = new ArrayList<>();
                for (MultipartFile imageProduct : imageProducts) {
                    String imageName = imageUpload.storeFile(imageProduct);

                    // Check if an image with the same name exists for the product
                    if (!isImageExistsForProduct(savedProduct.getId(), imageName)) {
                        Image image = new Image();
                        image.setName(imageName);
                        image.setProduct(savedProduct);
                        imageRepository.save(image);
                        imagesList.add(image);
                    }
                }
                savedProduct.setImage(imagesList);
            }

            return productRepository.save(savedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isImageExistsForProduct(Long productId, String imageName) {
        // Check if an image with the same name exists for the given product ID
        return imageRepository.existsByNameAndProductId(imageName, productId);
    }

    @Override
    public long countTotalProducts() {
        return productRepository.count();
    }


    @Override
    public List<Object[]> getProductStats() {
        return productRepository.getProductStatsForConfirmedOrders();
    }

    @Override
    public List<Object[]> getProductsStatsBetweenDates(Date startDate, Date endDate) {
        return productRepository.getProductsStatsForConfirmedOrdersBetweenDates(startDate,endDate);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

}
