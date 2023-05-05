package com.example.digitalmenuapi.controller;

import com.example.digitalmenuapi.model.AdminMenuItems;
import com.example.digitalmenuapi.model.MenuItems;
import com.example.digitalmenuapi.service.AdminMenuItemsService;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * admin controller class.
 */
@RestController
@RequestMapping("/admin")
public class AdminMenuController {

  @Autowired
  AdminMenuItemsService adminMenuItemsService;

  public AdminMenuController(AdminMenuItemsService adminMenuItemsService)  {
    this.adminMenuItemsService = adminMenuItemsService;
  }

  Logger logger = LoggerFactory.getLogger(MenuItemsController.class);

  /**
   * Post request method to add new sandwiches to the menu.
   */
  @PostMapping("/menu")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<AdminMenuItems> addMenu(@RequestBody AdminMenuItems adminMenuItems) {
    AdminMenuItems addedMenu = adminMenuItemsService.createNewSandwiches(adminMenuItems);
    logger.info("Sandwich created successfully");
    return ResponseEntity.created(URI.create("admin/menu/" + addedMenu.getId())).body(addedMenu);
  }

  /**
   * Delete request method to delete sandwiches from the menu
   * providing the correct id.
   */
  @DeleteMapping()
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteMenu(@RequestParam String id) {
    adminMenuItemsService.deleteMenuItem(id);
    logger.info("Sandwich deleted successfully");
    return ResponseEntity.ok().body("Deleted successfully");
  }

  /**
   * Get request method to get all temporary available sandwiches from the menu.
   */
  @GetMapping("/available")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<AdminMenuItems>> getAllTemporaryAvailableSandwiches() {
    logger.info("All temporary available sandwiches retrieved successfully");
    List<AdminMenuItems> menuList = adminMenuItemsService.getAllTemporaryAvailableSandwiches();
    return new ResponseEntity<>(menuList, HttpStatus.OK);
  }

  /**
   * Get request method to get all sandwiches from the menu.
   */
  @GetMapping("/menuList")
  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.OK)
  public List<AdminMenuItems> getAllMenuItems() {
    logger.info("Retrieved all items on the menu");
    return adminMenuItemsService.getAllSandwiches();
  }

}

