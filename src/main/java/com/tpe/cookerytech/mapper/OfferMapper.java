package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.domain.Offer;
import com.tpe.cookerytech.dto.response.OfferResponse;
import com.tpe.cookerytech.dto.response.OfferResponseWithUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfferMapper {


    OfferResponse offerToOfferResponse(Offer offer);

    OfferResponseWithUser offerToOfferResponsewithUser(Offer offer);
}
