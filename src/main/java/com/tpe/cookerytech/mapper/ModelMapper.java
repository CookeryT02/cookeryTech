package com.tpe.cookerytech.mapper;


import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.dto.request.ModelRequest;
import com.tpe.cookerytech.dto.response.ModelResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelMapper {


    Model modelRequestToModel(ModelRequest modelRequest);

    ModelResponse modelToModelResponse(Model model);
}
