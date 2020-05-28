package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryBusinessService categoryBusinessService;

    @RequestMapping(method = RequestMethod.GET ,path = "/category")
    public ResponseEntity<List<CategoryListResponse>> getCategories(){
     List<CategoryEntity> categories= categoryBusinessService.getAllCategories();
     List<CategoryListResponse> categoryListResponses = new ArrayList<>();
     categories.stream().forEach(
             categoryEntity ->{
                 CategoryListResponse response = new CategoryListResponse();
                 response.setCategoryName(categoryEntity.getCategoryName());
                 response.setId(categoryEntity.getId());
             }
     );
    return new ResponseEntity<List<CategoryListResponse>>(categoryListResponses, HttpStatus.OK);
    }
}
