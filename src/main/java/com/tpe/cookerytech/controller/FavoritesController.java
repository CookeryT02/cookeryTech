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


    @PostMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ModelResponse> updateAuthUserFavorites(@RequestBody FavoritesRequest favoritesRequest){
        ModelResponse modelResponse = favoritesService.updateAuthUserFavorites(favoritesRequest.getId());
        return ResponseEntity.ok(modelResponse);
    }

    @DeleteMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public void deleteAllFavorites(){
        favoritesService.deleteAllFavoritesAuthUser();
    }

    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<ModelResponse>> getAllAuthUserFavorites(){

        List<ModelResponse> modelResponseList = favoritesService.getAllAuthUserFavorites();

        return ResponseEntity.ok(modelResponseList);

    }

    @PutMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<CkResponse> moveFavoritesToCart(){

        CkResponse ckResponse = favoritesService.moveAllFavoritesToShoppingCart();

        return ResponseEntity.ok(ckResponse);

    }

}