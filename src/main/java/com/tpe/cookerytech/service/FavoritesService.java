package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Favorites;
import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.dto.response.ModelResponse;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.ModelMapper;
import com.tpe.cookerytech.repository.FavoritesRepository;
import com.tpe.cookerytech.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;

    private final UserService userService;

    private final ModelRepository modelRepository;

    private final ModelMapper modelMapper;

    public FavoritesService(FavoritesRepository favoritesRepository, UserService userService, ModelRepository modelRepository, ModelMapper modelMapper) {
        this.favoritesRepository = favoritesRepository;
        this.userService = userService;
        this.modelRepository = modelRepository;
        this.modelMapper = modelMapper;
    }

    public ModelResponse updateAuthUserFavorites(Long modelId) {
        User user = userService.getCurrentUser();

        Model model = modelRepository.findById(modelId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION,modelId)));

        List<Favorites> favoritesListAuthUser = favoritesRepository.findByUserId(user.getId());

        List<Long> modelIdList = new ArrayList<>();
        for (Favorites favorites: favoritesListAuthUser){
            modelIdList.add(favorites.getModel().getId());
        }
        if(modelIdList.contains(modelId)){
            for (Favorites favorites: favoritesListAuthUser){
                if(favorites.getModel().getId()==modelId){
                    favoritesRepository.delete(favorites);
                }
            }
        }else {
                    Favorites favoritesNew = new Favorites();
                    favoritesNew.setModel(model);
                    favoritesNew.setCreateAt(LocalDateTime.now());
                    favoritesNew.setUser(user);
                    favoritesRepository.save(favoritesNew);
        }

        ModelResponse modelResponse = modelMapper.modelToModelResponse(model);
        modelResponse.setCurrencyId(model.getCurrency().getId());
        modelResponse.setProductId(model.getProduct().getId());
        return modelResponse;
    }

    public void deleteAllFavoritesAuthUser() {
        User user = userService.getCurrentUser();

        List<Favorites> favoritesListAuthUser = favoritesRepository.findByUserId(user.getId());

        for (Favorites favorites: favoritesListAuthUser){
            favoritesRepository.delete(favorites);
        }
    }
}
