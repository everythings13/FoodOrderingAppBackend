package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.ItemsEntity;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ItemService itemService; // Handles all the Service Related to Item.

    @Autowired
    RestaurantService restaurantService; // Handles all the Service Related to Restaurant.

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/{restaurant_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getTopFiveItemsByPopularity (@PathVariable(value = "restaurant_id")final String restaurantUuid) throws RestaurantNotFoundException {

        //Calls restaurantByUUID method of restaurantService to get the restaurant entity.
        Restaurant restaurantEntity = restaurantService.getRestaurantByRestaurantUuid(restaurantUuid);

        //Calls getItemsByPopularity method of itemService to get the ItemEntity.
        List<ItemsEntity> itemEntities = itemService.getItemsByPopularity(restaurantEntity);

        //Creating the ItemListResponse details as required.
        ItemListResponse itemListResponse = new ItemListResponse();
        itemEntities.forEach(itemEntity -> {
            ItemList itemList = new ItemList()
                    .id(UUID.fromString(itemEntity.getUuid()))
                    .itemName(itemEntity.getItemName())
                    .price(itemEntity.getPrice())
                    .itemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType().getValue()));
            itemListResponse.add(itemList);
        });

        return new ResponseEntity<ItemListResponse>(itemListResponse,HttpStatus.OK);
    }


}