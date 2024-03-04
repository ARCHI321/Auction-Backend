package com.herovired.Auction.Management.System.controllers;


import com.herovired.Auction.Management.System.util.Categories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

    @GetMapping
    public Categories[] getCategories() {
        return Categories.values();
    }
}
