package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.OfferDto;
import com.ecommerce.library.model.Offer;
import com.ecommerce.library.repository.OfferRepository;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.OfferService;
import com.ecommerce.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Override
    public Offer save(OfferDto offerDto) {
        Offer offer = new Offer();
        mapDtoToEntity(offerDto, offer);
        return offerRepository.save(offer);
    }

    @Override
    public Offer update(OfferDto offerDto) {
        Offer offer = offerRepository.findById(offerDto.getId())
                .orElseThrow(() -> new RuntimeException("Offer not found"));
        mapDtoToEntity(offerDto, offer);
        return offerRepository.save(offer);
    }

    private void mapDtoToEntity(OfferDto offerDto, Offer offer) {
        offer.setName(offerDto.getName());
        offer.setDescription(offerDto.getDescription());
        offer.setDiscount(offerDto.getDiscount());
        offer.setCategory(offerDto.getCategory());
        offer.setProduct(offerDto.getProduct());
        offer.setActivated(offerDto.isActivated());
        offer.setDeleted(offerDto.isDeleted());
    }

    private OfferDto convertToDto(Offer offer) {
        return new OfferDto(
                offer.getId(),
                offer.getName(),
                offer.getDescription(),
                offer.getDiscount(),
                offer.getCategory(),
                offer.getProduct(),
                offer.isActivated(),
                offer.isDeleted()
        );
    }

    @Override
    public Offer createOffer(OfferDto offerDto) {
        Offer offer = new Offer();
        mapDtoToEntity(offerDto, offer);
        offerRepository.save(offer);
        return offer;
    }

    @Override
    public OfferDto updateOffer(Long offerId, OfferDto offerDto) {
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new RuntimeException("Offer not found"));
        mapDtoToEntity(offerDto, offer);
        offerRepository.save(offer);
        return convertToDto(offer);
    }

    @Override
    public OfferDto getOfferById(Long offerId) {
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new RuntimeException("Offer not found"));
        return convertToDto(offer);
    }

    @Override
    public List<OfferDto> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOffer(Long offerId) {
        offerRepository.deleteById(offerId);
    }

    @Override
    public List<OfferDto> findAllByActivatedTrue() {
        return offerRepository.findAll().stream()
                .filter(Offer::isActivated)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OfferDto> findAll() {
        return offerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OfferDto> findById(Long id) {
        return offerRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public void deleteById(Long id) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new RuntimeException("Offer not found"));
        offer.setActivated(false);
        offer.setDeleted(true);
        offerRepository.save(offer);
    }

    @Override
    public void enableById(Long id) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new RuntimeException("Offer not found"));
        offer.setActivated(true);
        offer.setDeleted(false);
        offerRepository.save(offer);
    }

    @Override
    public long countTotalOffers() {
        return offerRepository.count();
    }
}
