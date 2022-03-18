package com.kmsystem.document.mapper;


import com.kmsystem.document.domain.Category;

import java.util.List;
import java.util.Locale;

public interface CategoryMapper {

    public void createCategory(Category category)throws Exception;

    public List<Category> readCategoryList()throws Exception;

    public Category readCategoryByCategoryType(String categoryType)throws Exception;

    public void updateCategory(Category category)throws Exception;

}
