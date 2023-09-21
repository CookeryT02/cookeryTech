package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.domain.OfferItem;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OfferItemMapper {

    OfferItemResponse offerItemToOfferItemResponse(OfferItem offerItem);

}
