package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
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

  @Autowired private CategoryService categoryService;

  @RequestMapping(method = RequestMethod.GET, path = "/category")
  public ResponseEntity<CategoriesListResponse> getCategories() {
    List<CategoryEntity> categories = categoryService.getAllCategoriesOrderedByName();
    CategoriesListResponse categoryListResponses = getCategoryListResponses(categories);
    return new ResponseEntity<>(categoryListResponses, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}")
  public ResponseEntity<CategoryDetailsResponse> getCategoryById(
      @PathVariable("category_id") String categoryId) throws CategoryNotFoundException {
    if (categoryId == null) {
      throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
    }
    CategoryEntity categoryDetails = categoryService.getCategoryById(categoryId);
      List<ItemsEntity> itemDetails = new ArrayList<>();
      if(categoryDetails.getId()!=null) {
        itemDetails = categoryService.getItems(categoryDetails.getId());
        categoryDetails.setItems(itemDetails);
    }

    CategoryDetailsResponse response = getCategoryDetailsResponse(categoryDetails);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private CategoriesListResponse getCategoryListResponses(List<CategoryEntity> categories) {
      CategoriesListResponse categoriesListResponses = new CategoriesListResponse();
    if (categories != null && categories.size()>0) {

        List<CategoryListResponse> listResponses = new ArrayList<>();
        categories.stream()
                .forEach(
                        categoryEntity -> {
                            CategoryListResponse response = new CategoryListResponse();
                            response.setCategoryName(categoryEntity.getCategoryName());
                            response.setId(UUID.fromString(categoryEntity.getUuid()));
                            listResponses.add(response);
                        });

        categoriesListResponses.setCategories(listResponses);
    }else{
        categoriesListResponses.setCategories(null);
    }
    return categoriesListResponses;
  }

  private CategoryDetailsResponse getCategoryDetailsResponse(
      CategoryEntity categoryDetails) {
    CategoryDetailsResponse response = new CategoryDetailsResponse();
    if (categoryDetails.getItems() != null) {
        categoryDetails.getItems().stream()
          .forEach(
              itemsEntity -> {
                ItemList item = new ItemList();
                item.setId(UUID.fromString(itemsEntity.getUuid()));
                item.setItemName(itemsEntity.getItemName());
                item.setPrice(itemsEntity.getPrice());
                item.setItemType(ItemList.ItemTypeEnum.valueOf(itemsEntity.getType().toString()));
                response.addItemListItem(item);
              });
    }
    response.setId(UUID.fromString(categoryDetails.getUuid()));
    response.setCategoryName(categoryDetails.getCategoryName());
    return response;
  }
}
