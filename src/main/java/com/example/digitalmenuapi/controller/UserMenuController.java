package com.example.digitalmenuapi.controller;

import com.example.digitalmenuapi.model.MenuItems;
import com.example.digitalmenuapi.service.MenuItemsService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Menu controller class.
 */
@RestController
@Log4j2
@RequestMapping("/user")
public class UserMenuController {

  @Autowired
  MenuItemsService menuItemsService;

  public UserMenuController(MenuItemsService menuItemsService) {
    this.menuItemsService = menuItemsService;
  }

  Logger logger = LoggerFactory.getLogger(UserMenuController.class);

  /**
   * Get request method to get all sandwiches from the menu.
   */
  @GetMapping("/menuList")
  @ResponseStatus(HttpStatus.OK)
  public List<MenuItems> getAllMenuItems() {
    logger.info("Retrieved all items on the menu");
    return menuItemsService.getAllMenuItems();
  }

}
