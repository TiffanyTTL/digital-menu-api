package com.example.digitalmenuapi.controller;

import com.example.digitalmenuapi.model.AdminMenuItem;
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
  @PostMapping("/createMenu")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<AdminMenuItem> addItemToMenu(@RequestBody AdminMenuItem adminMenuItem) {
    AdminMenuItem addedMenu = adminMenuItemsService.createNewSandwiches(adminMenuItem);
    logger.info("Sandwich created successfully");
    return ResponseEntity.created(URI.create("admin/createMenu/" + addedMenu.getId())).body(addedMenu);
  }

  /**
   * Delete request method to delete sandwiches from the menu
   * providing the correct id.
   */
  @DeleteMapping()
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteMenu(@RequestParam String id) {
      String deleteMenuItemStatus = adminMenuItemsService.deleteMenuItem(id);
      if (deleteMenuItemStatus.equals("Deleted successfully")) {
        logger.info("Sandwich with ID {} deleted successfully", id);
        return ResponseEntity.ok().body(deleteMenuItemStatus);
      } else {
        logger.warn("Sandwich with ID {} not found", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(deleteMenuItemStatus);
      }
    }

  /**
   * Get request method to get all temporary available sandwiches from the menu.
   */
  @GetMapping("/available")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<AdminMenuItem>> getAllTemporaryAvailableSandwiches() {
    logger.info("All temporary available sandwiches retrieved successfully");
    List<AdminMenuItem> menuList = adminMenuItemsService.getAllTemporaryAvailableSandwiches();
    return new ResponseEntity<>(menuList, HttpStatus.OK);
  }

  /**
   * Get request method to get all sandwiches from the menu.
   */
  @GetMapping("/menuList")
  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.OK)
  public List<AdminMenuItem> getAllMenuItems() {
    logger.info("Retrieved all items on the menu");
    return adminMenuItemsService.getAllSandwiches();
  }

}

