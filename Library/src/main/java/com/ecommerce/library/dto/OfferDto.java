package com.ecommerce.library.dto;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {
    private Long id;
    private String name;
    private String description;
    private double discount;


    private Category category;
    private Product product;
    private boolean activated;
    private boolean deleted;


}
