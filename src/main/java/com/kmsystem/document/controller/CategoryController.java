package com.kmsystem.document.controller;

import com.kmsystem.document.domain.Category;
import com.kmsystem.document.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation("카테고리_등록")
    @PostMapping()
    public ResponseEntity<Void> createCategory(@RequestBody Category category) throws Exception {
        categoryService.createCategory(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("카테고리_목록")
    @GetMapping()
    public ResponseEntity<List<Category>> readCategoryList() throws Exception {
        List<Category> categoryList = categoryService.readCategoryList();
        return new ResponseEntity<>(categoryList,HttpStatus.OK);
    }

    @ApiOperation("카테고리_수정")
    @PostMapping("/update")
    public ResponseEntity<Void> updateCategory(@RequestBody Category category) throws Exception {
        categoryService.updateCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
