package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.domain.OfferItem;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferItemMapper {

    OfferItemResponse offerItemToOfferItemResponse(OfferItem offerItem);

    List<ReportOfferResponse> offerItemListToReportOfferResponseList(List<OfferItem> x);


}
