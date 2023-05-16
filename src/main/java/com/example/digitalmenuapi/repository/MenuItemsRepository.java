package com.example.digitalmenuapi.repository;

import com.example.digitalmenuapi.model.MenuItems;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Menu items repository class.
 */
@Repository
public interface MenuItemsRepository extends MongoRepository<MenuItems, String> {
}
