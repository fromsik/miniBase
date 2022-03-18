package com.kmsystem.document.service;

import com.kmsystem.document.domain.*;
import com.kmsystem.document.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public void createCategory(Category category)throws Exception{

        categoryMapper.createCategory(category);
    }

    public List<Category> readCategoryList()throws Exception{
        return categoryMapper.readCategoryList();
    }

    public Category readCategoryByCategoryType(String categoryType)throws Exception{
        return categoryMapper.readCategoryByCategoryType(categoryType);
    }

    public void updateCategory(Category category)throws Exception{
        categoryMapper.updateCategory(category);
    }
}
