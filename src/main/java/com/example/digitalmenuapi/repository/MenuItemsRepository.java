package com.example.digitalmenuapi.repository;

import com.example.digitalmenuapi.model.MenuItems;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Menu items repository class.
 */
@Repository
public interface MenuItemsRepository extends MongoRepository<MenuItems, String> {

    List<MenuItems> findMenuItemsByVegan(boolean vegan);
    List<MenuItems> findMenuItemsByVegetarian(boolean vegetarian);
    List<MenuItems> findMenuItemsByCaloriesLessThan(int calories);
    List<MenuItems> findMenuItemsByPriceLessThan(double price);
    List<MenuItems> findMenuItemsByAllergiesNotContaining(String allergies);





    }



