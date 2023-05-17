package com.example.digitalmenuapi.service;

import com.example.digitalmenuapi.model.MenuItems;
import com.example.digitalmenuapi.repository.MenuItemsRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


/**
 * Service class for menu items.
 */
@Service
public class MenuItemsService {

  @Autowired
  private MenuItemsRepository menuItemsRepository;

  Logger logger = LoggerFactory.getLogger(MenuItemsService.class);

  public MenuItemsService(MenuItemsRepository menuItemsRepository) {
    this.menuItemsRepository = menuItemsRepository;
  }

  /**
   * get all menu items from the repository method.
   */
  public List<MenuItems> getAllMenuItems() {
    return menuItemsRepository.findAll();
  }

  /**
   * create new sandwich using try and catch method.
   * if correctly entered saves to db
   */
  public MenuItems createSandwiches(MenuItems menuItems) {
    try {
      logger.info("Creating new sandwich: {}", menuItems);
      return menuItemsRepository.save(menuItems);
    } catch (DataIntegrityViolationException e) {
      logger.error("Error creating sandwich: {}", e.getMessage());
      throw new IllegalArgumentException("Invalid sandwich data");
    }
  }

  /**
   * get sandwich by filters.
   *
   */

  public List<MenuItems> getSandwichesByFilter(boolean vegan, boolean vegetarian,
                                               Integer calories, Double price,
                                               String allergies) {
    List<MenuItems> sandwiches = new ArrayList<>();
    if (vegan) {
      sandwiches.addAll(menuItemsRepository.findMenuItemsByVegan(true));
    }
    if (vegetarian) {
      sandwiches.addAll(menuItemsRepository.findMenuItemsByVegetarian(true));
    }
    if (calories < 1000) {
      sandwiches.addAll(menuItemsRepository.findMenuItemsByCaloriesLessThan(calories));
    }
    if (price > 0) {
      sandwiches.addAll(menuItemsRepository.findMenuItemsByPriceLessThan(price));
    }
    if (allergies != null && !allergies.isBlank()) {
      sandwiches.addAll(menuItemsRepository.findMenuItemsByAllergiesNotContaining(allergies));
    }
    return sandwiches;
  }

}



