package com.tpe.cookerytech.controller;


import com.tpe.cookerytech.dto.request.FavoritesRequest;
import com.tpe.cookerytech.dto.response.CkResponse;
import com.tpe.cookerytech.dto.response.ModelResponse;
import com.tpe.cookerytech.service.FavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/favorites")
public class FavoritesController {
    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }



    //K01 -> It will get authenticated user`s favorites    Page:91
    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<ModelResponse>> getAllAuthUserFavorites(){

        List<ModelResponse> modelResponseList = favoritesService.getAllAuthUserFavorites();

        return ResponseEntity.ok(modelResponseList);
    }



    //K02 -> It will update authenticated user`s favorites
    @PostMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ModelResponse> updateAuthUserFavorites(@RequestBody FavoritesRequest favoritesRequest){
        ModelResponse modelResponse = favoritesService.updateAuthUserFavorites(favoritesRequest.getId());
        return ResponseEntity.ok(modelResponse);
    }




    //K03 -> It will remove all favorites of authenticated user
    @DeleteMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public void deleteAllFavorites(){
        favoritesService.deleteAllFavoritesAuthUser();
    }




    //K04 -> It will move all favorites of authenticated user to cart
    @PutMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<CkResponse> moveFavoritesToCart(){

        CkResponse ckResponse = favoritesService.moveAllFavoritesToShoppingCart();

        return ResponseEntity.ok(ckResponse);
    }
}
