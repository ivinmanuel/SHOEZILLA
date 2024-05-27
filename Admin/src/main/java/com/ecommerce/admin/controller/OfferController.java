package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.OfferDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.OfferService;
import com.ecommerce.library.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class OfferController {

    private final OfferService offerService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public OfferController(OfferService offerService, ProductService productService, CategoryService categoryService) {
        this.offerService = offerService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/offers")
    public String getOffers(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<OfferDto> offerDtoList = offerService.getAllOffers();
        model.addAttribute("offers", offerDtoList);
        model.addAttribute("size", offerDtoList.size());
        return "offers";
    }

    @GetMapping("/offers/add-offer")
    public String getAddOffer(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        List<ProductDto> productList = productService.findAllProducts();
        List<Category> categoryList = categoryService.findAllByActivatedTrue();

        model.addAttribute("products", productList);
        model.addAttribute("categories", categoryList);
        model.addAttribute("offer", new OfferDto());

        return "add-offer";
    }

    @PostMapping("/offers/save")
    public String addOffer(@ModelAttribute("offer") OfferDto offer, @RequestParam(value = "productId", required = false) Long productId, @RequestParam(value = "categoryId", required = false) Long categoryId, RedirectAttributes redirectAttributes) {
        try {
            if (productId != null) {
                Product product = productService.findById(productId);
                offer.setProduct(product);
            }
            if (categoryId != null) {
                Category category = categoryService.findCategoryById(categoryId);
                offer.setCategory(category);
            }
            offerService.createOffer(offer);
            redirectAttributes.addFlashAttribute("success", "Added new Offer successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new Offer!");
        }
        return "redirect:/offers";
    }

    @GetMapping("/offers/update-offer/{id}")
    public String updateOfferForm(@PathVariable("id") long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        List<ProductDto> productList = productService.findAllProducts();
        List<Category> categoryList = categoryService.findAllByActivatedTrue();

        model.addAttribute("products", productList);
        model.addAttribute("categories", categoryList);

        OfferDto offerDto = offerService.getOfferById(id);
        model.addAttribute("offer", offerDto);

        return "update-offer";
    }

    @PostMapping("/offers/update-offer/{id}")
    public String updateOffer(@ModelAttribute("offer") OfferDto offerDto, @RequestParam(value = "productId", required = false) Long productId, @RequestParam(value = "categoryId", required = false) Long categoryId, RedirectAttributes redirectAttributes) {
        try {
            if (productId != null) {
                Product product = productService.findById(productId);
                offerDto.setProduct(product);
            }
            if (categoryId != null) {
                Category category = categoryService.findCategoryById(categoryId);
                offerDto.setCategory(category);
            }
            offerService.updateOffer(offerDto.getId(), offerDto);
            redirectAttributes.addFlashAttribute("success", "Updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/offers";
    }

    @GetMapping("/disable-offer/{id}")
    public String disableOffer(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        try {
            OfferDto offerDto = offerService.getOfferById(id);
            offerDto.setActivated(false);
            offerService.updateOffer(id, offerDto);
            redirectAttributes.addFlashAttribute("success", "Offer Disabled");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to disable Offer!");
        }
        return "redirect:/offers";
    }

    @GetMapping("/enable-offer/{id}")
    public String enableOffer(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        try {
            OfferDto offerDto = offerService.getOfferById(id);
            offerDto.setActivated(true);
            offerService.updateOffer(id, offerDto);
            redirectAttributes.addFlashAttribute("success", "Offer Enabled");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to enable Offer!");
        }
        return "redirect:/offers";
    }

    @GetMapping("/delete-offer/{id}")
    public String deleteOffer(@PathVariable("id") Long offerId, RedirectAttributes redirectAttributes) {
        try {
            offerService.deleteOffer(offerId);
            redirectAttributes.addFlashAttribute("success", "Offer deleted");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to delete Offer!");
        }
        return "redirect:/offers";
    }
}
