package com.ecommerce.library.service;

import com.ecommerce.library.dto.OfferDto;
import com.ecommerce.library.model.Offer;

import java.util.List;
import java.util.Optional;

public interface OfferService {


    Offer save(OfferDto offerDto);

    Offer update(OfferDto offerDto);

    List<OfferDto> findAllByActivatedTrue();

    List<OfferDto> findAll();

    Optional<OfferDto> findById(Long id);

    void deleteById(Long id);

    void enableById(Long id);

    long countTotalOffers();
    Offer createOffer(OfferDto offerDto);
    OfferDto updateOffer(Long offerId, OfferDto offerDto);
    OfferDto getOfferById(Long offerId);
    List<OfferDto> getAllOffers();
    void deleteOffer(Long offerId);


}
