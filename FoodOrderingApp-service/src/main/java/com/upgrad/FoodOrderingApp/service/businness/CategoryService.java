package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  private final CategoryDao categoryDao;

  @Autowired
  public CategoryService(CategoryDao categoryDao) {
    this.categoryDao = categoryDao;
  }

  /** @return get category by category uuid */
  public Category getCategoryNameByCategoryUuid(String categoryId) {
    return categoryDao.getCategoryByCategoryUuid(categoryId);
  }
}
