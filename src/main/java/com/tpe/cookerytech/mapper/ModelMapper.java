package com.tpe.cookerytech.mapper;


import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.dto.request.ModelRequest;
import com.tpe.cookerytech.dto.response.ModelCreateResponse;
import com.tpe.cookerytech.dto.response.ModelResponse;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ModelMapper {


    Model modelRequestToModel(ModelRequest modelRequest);

    ModelResponse modelToModelResponse(Model model);

    default List<ModelResponse> modelListToModelResponseList(List<Model> modelList){
        List<ModelResponse> modelResponseList = new ArrayList<>();
        for (Model model: modelList){
            ModelResponse modelResponse = modelToModelResponse(model);
            modelResponse.setCurrencyId(model.getCurrency().getId());
            modelResponse.setProductId(model.getProduct().getId());
            modelResponseList.add(modelResponse);
        }
        return modelResponseList;
    }

    ModelCreateResponse modelToModelCreateResponse(Model model);
}
