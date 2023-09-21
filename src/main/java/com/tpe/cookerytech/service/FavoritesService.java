package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.dto.response.CkResponse;
import com.tpe.cookerytech.dto.response.ModelResponse;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.ModelMapper;
import com.tpe.cookerytech.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;

    public FavoritesService(FavoritesRepository favoritesRepository, UserService userService, ShoppingCartRepository shoppingCartRepository, ShoppingCartItemRepository shoppingCartItemRepository, ModelRepository modelRepository, ModelMapper modelMapper) {
        this.favoritesRepository = favoritesRepository;
        this.userService = userService;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.modelRepository = modelRepository;
        this.modelMapper = modelMapper;
    }


    //K01
    public List<ModelResponse> getAllAuthUserFavorites() {

        User user = userService.getCurrentUser();

        if(favoritesRepository.findByUserId(user.getId()) == null){

            throw new ResourceNotFoundException(ErrorMessage.USER_FAVORITES_NOT_FOUND_EXCEPTION);
        }
        List<Favorites> favoritesList = favoritesRepository.findByUserId(user.getId());

        List<Model> modelList = favoritesList.stream()
                .map(t->t.getModel())
                .collect(Collectors.toList());

        List<ModelResponse> modelResponseList = modelMapper.modelListToModelResponseList(modelList);

        return modelResponseList;
    }




    //K02
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



    //K03
    public void deleteAllFavoritesAuthUser() {
        User user = userService.getCurrentUser();

        List<Favorites> favoritesListAuthUser = favoritesRepository.findByUserId(user.getId());

        for (Favorites favorites: favoritesListAuthUser){
            favoritesRepository.delete(favorites);
        }
    }



    //K04
    public CkResponse moveAllFavoritesToShoppingCart() {

        User user = userService.getCurrentUser();

        List<Favorites> favoritesListAuthUser = favoritesRepository.findByUserId(user.getId());

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(user);
                    newCart.setCreateAt(LocalDateTime.now());
                    return shoppingCartRepository.save(newCart);
                });
        List<Long> modelIdList = new ArrayList<>();

        for (Favorites favorites: favoritesListAuthUser){

            modelIdList.add(favorites.getModel().getId());
        }

        List<ShoppingCartItem> shoppingCartItemList = shoppingCartItemRepository.findByShoppingCartId(shoppingCart.getId());

        for (Favorites favoriteItem : favoritesListAuthUser) {

            boolean isItemInCart = false;

            for (ShoppingCartItem cartItem : shoppingCartItemList) {

                if (cartItem.getModel().getId() == favoriteItem.getModel().getId()) {

                    isItemInCart = true;

                    cartItem.setShoppingCart(shoppingCart);
                    cartItem.setModel(favoriteItem.getModel());
                    cartItem.setProduct(favoriteItem.getModel().getProduct());
                    cartItem.setAmount(cartItem.getAmount()+1);
                    cartItem.setCreateAt(LocalDateTime.now());

                    shoppingCartItemRepository.save(cartItem);

                    break;
                }
            }
            if (!isItemInCart) {
                ShoppingCartItem cartItem = new ShoppingCartItem();
                cartItem.setShoppingCart(shoppingCart);
                cartItem.setModel(favoriteItem.getModel());
                cartItem.setProduct(favoriteItem.getModel().getProduct());
                cartItem.setAmount(1);
                cartItem.setCreateAt(LocalDateTime.now());

                shoppingCartItemRepository.save(cartItem);
            }
        }
        return new CkResponse("Favorites move to shopping Cart successfully",true);
    }
}
