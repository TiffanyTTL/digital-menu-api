package com.example.digitalmenuapi.repository;

import com.example.digitalmenuapi.model.AdminMenuItem;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * Admin menu repository class.
 */
public interface AdminMenuRepository extends MongoRepository<AdminMenuItem, String> {

  List<AdminMenuItem> findAdminMenuItemsByAvailable(boolean available);
}
