package com.example.digitalmenuapi.service;

import com.example.digitalmenuapi.model.AdminMenuItem;
import com.example.digitalmenuapi.repository.AdminMenuRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Service class for admin menu items.
 */
@Service
public class AdminMenuItemsService {

  @Autowired
  private AdminMenuRepository adminMenuRepository;

  Logger logger = LoggerFactory.getLogger(MenuItemsService.class);

  public AdminMenuItemsService(AdminMenuRepository adminMenuRepository) {
    this.adminMenuRepository = adminMenuRepository;
  }

  /**
   * delete a menu item method.
   */
  public String deleteMenuItem(String id) {
    Optional<AdminMenuItem> optionalAdminMenuItem = adminMenuRepository.findById(id);
    if (optionalAdminMenuItem.isPresent()) {
      adminMenuRepository.deleteById(id);
      logger.info("Sandwich with ID {} deleted successfully", id);
      return "Deleted successfully";
    } else {
      logger.warn("Sandwich with ID {} not found", id);
      return "Sandwich with ID" + id + " not found";
    }
  }

  /**
   * create a sandwich method.
   */
  public AdminMenuItem createNewSandwiches(AdminMenuItem adminMenuItem) {
    try {
      logger.info("Creating new sandwich: {}", adminMenuItem);
      return adminMenuRepository.save(adminMenuItem);
    } catch (DataIntegrityViolationException e) {
      logger.error("Error creating sandwich: {}", e.getMessage());
      throw new IllegalArgumentException("Invalid sandwich data");
    }
  }


  /**
   * get all menu items from the repository method.
   */
  public List<AdminMenuItem> getAllSandwiches() {
    List<AdminMenuItem> menuList = adminMenuRepository.findAdminMenuItemsByAvailable(true);
    logger.info("Retrieved all temporary available sandwiches");
    return menuList;
  }
}
