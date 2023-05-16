package com.example.digitalmenuapi.repository;

import com.example.digitalmenuapi.model.AdminMenuItem;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * Admin menu repository class.
 */
public interface AdminMenuRepository extends MongoRepository<AdminMenuItem, String> {

  List<AdminMenuItem> findAdminMenuItemsByAvailable(boolean available);

  Optional<AdminMenuItem> findAdminMenuItemsByName(String name);
}
