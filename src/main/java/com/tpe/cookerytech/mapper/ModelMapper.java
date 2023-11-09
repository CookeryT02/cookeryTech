package com.tpe.cookerytech.mapper;


import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.dto.request.ModelRequest;
import com.tpe.cookerytech.dto.response.ModelCreateResponse;
import com.tpe.cookerytech.dto.response.ModelGenereteResponse;
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
    ModelGenereteResponse modelToModelGenereteResponse(Model model);
    default List<ModelGenereteResponse> modelListToModelGenereteResponseList(List<Model> modelLists){
        List<ModelGenereteResponse> modelGenereteResponseList = new ArrayList<>();
        for (Model model:modelLists){
            ModelGenereteResponse modelGenereteResponse = modelToModelGenereteResponse(model);
            modelGenereteResponse.setCurrencyCode(model.getCurrency().getCode());
            modelGenereteResponse.setProductId(model.getProduct().getId());
            modelGenereteResponseList.add(modelGenereteResponse);
        }
        return modelGenereteResponseList;
    }


}
