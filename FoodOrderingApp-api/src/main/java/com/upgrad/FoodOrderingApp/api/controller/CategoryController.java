package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemsEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class CategoryController {

  @Autowired private CategoryBusinessService categoryBusinessService;

  @RequestMapping(method = RequestMethod.GET, path = "/category")
  public ResponseEntity<List<CategoryListResponse>> getCategories() {
    List<CategoryEntity> categories = categoryBusinessService.getAllCategories();
    List<CategoryListResponse> categoryListResponses = new ArrayList<>();
    categories.stream()
        .forEach(
            categoryEntity -> {
              CategoryListResponse response = new CategoryListResponse();
              response.setCategoryName(categoryEntity.getCategoryName());
              response.setId(UUID.fromString(categoryEntity.getUuid()));
              categoryListResponses.add(response);
            });
    return new ResponseEntity<List<CategoryListResponse>>(categoryListResponses, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}")
  public ResponseEntity<CategoryDetailsResponse> getCategoryById(
      @PathVariable("category_id") String categoryId) throws CategoryNotFoundException {
    if (categoryId == null ) {
      throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
    }
    CategoryEntity categoryDetails =
        categoryBusinessService.getCategoryById(categoryId);
    if (categoryDetails == null) {
      throw new CategoryNotFoundException("CNF-002", "No category by this id");
    }
    List<ItemsEntity> itemDetails = categoryBusinessService.getItems(categoryDetails.getId());
    CategoryDetailsResponse response = new CategoryDetailsResponse();
    if (itemDetails != null) {
      itemDetails.stream()
          .forEach(
              itemsEntity -> {
                ItemList item = new ItemList();
                item.setId(itemsEntity.getUuid());
                item.setItemName(itemsEntity.getItemName());
                item.setPrice(itemsEntity.getPrice());
                item.setItemType(ItemList.ItemTypeEnum.fromValue(itemsEntity.getType()));
                response.addItemListItem(item);
              });
    }
    response.setId(UUID.fromString(categoryDetails.getUuid()));
    response.setCategoryName(categoryDetails.getCategoryName());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
