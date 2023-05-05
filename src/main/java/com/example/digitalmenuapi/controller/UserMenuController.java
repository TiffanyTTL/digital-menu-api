package com.example.digitalmenuapi.controller;

import com.example.digitalmenuapi.model.MenuItems;
import com.example.digitalmenuapi.service.MenuItemsService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/menuList")
    @ResponseStatus(HttpStatus.OK)
    public List<MenuItems> getAllMenuItems() {
        logger.info("Retrieved all items on the menu");
        return menuItemsService.getAllMenuItems();
    }

}
