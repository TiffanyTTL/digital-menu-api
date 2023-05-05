package com.example.digitalmenuapi.service;

import com.example.digitalmenuapi.model.AdminMenuItems;
import com.example.digitalmenuapi.repository.AdminMenuRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    try {
      adminMenuRepository.deleteById(id);
      logger.info("Sandwich with ID {} deleted successfully", id);
      return "Sandwich with ID " + id + " deleted successfully";
    } catch (EmptyResultDataAccessException e) {
      logger.warn("Sandwich with ID {} not found", id);
      return "Sandwich with ID " + id + " not found";
    }
  }

  /**
   * create a sandwich method.
   */
  public AdminMenuItems createNewSandwiches(AdminMenuItems adminMenuItems) {
    try {
      logger.info("Creating new sandwich: {}", adminMenuItems);
      return adminMenuRepository.save(adminMenuItems);
    } catch (DataIntegrityViolationException e) {
      logger.error("Error creating sandwich: {}", e.getMessage());
      throw new IllegalArgumentException("Invalid sandwich data");
    }
  }

  /**
   * list all temporary unavailable sandwiches method.
   */
  public List<AdminMenuItems> getAllTemporaryUnavailableSandwiches() {
    List<AdminMenuItems> menuList = adminMenuRepository.findAdminMenuItemsByAvailable(false);
    logger.info("Retrieved all temporary unavailable sandwiches");
    return menuList;
  }

}
