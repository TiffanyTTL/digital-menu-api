package com.example.digitalmenuapi.controller;

import com.example.digitalmenuapi.model.MenuItems;
import com.example.digitalmenuapi.service.MenuItemsService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Menu items controller class.
 */
@RestController
@RequestMapping("/menu")
public class MenuItemsController {

  @Autowired
  MenuItemsService menuItemsService;

  public MenuItemsController(MenuItemsService menuItemsService) {
    this.menuItemsService = menuItemsService;
  }


  Logger logger = LoggerFactory.getLogger(MenuItemsController.class);

  /**
   * post mapping request, used to creat new sandwiches.
   */
  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> createSandwiches(@RequestBody MenuItems menuItems) {
    logger.info("Sandwich created successfully");
    MenuItems createdMenuItem = menuItemsService.createSandwiches(menuItems);
    return ResponseEntity.status(HttpStatus.CREATED).body("Sandwich created successfully");
  }

  @GetMapping("/list")
  @ResponseStatus(HttpStatus.OK)
  public List<MenuItems> getFilteredSandwiches(@RequestParam(required = false) boolean vegan,
                                          @RequestParam(required = false) boolean vegetarian,
                                          @RequestParam(required = false) Integer calories,
                                          @RequestParam(required = false) Double price,
                                          @RequestParam(required = false) String allergies) {
    return menuItemsService.getSandwichesByFilter(vegan, vegetarian, calories, price, allergies);

  }
}

