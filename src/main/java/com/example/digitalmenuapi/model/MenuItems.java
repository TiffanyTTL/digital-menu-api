package com.example.digitalmenuapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Menu Items Model class.
 */
@Document(collection = "MenuItems")
public class MenuItems {

  @Id
  private String id;
  private String name;
  private int calories;
  private String allergies;
  private boolean isVegan;
  private boolean isVegetarian;
  private double price;

  /**
   * Menu items constructor.
   */
  public MenuItems(String name, int calories, String allergies,
                   boolean isVegan, boolean isVegetarian, double price) {
    this.name = name;
    this.calories = calories;
    this.allergies = allergies;
    this.isVegan = isVegan;
    this.isVegetarian = isVegetarian;
    this.price = price;
  }

  public MenuItems() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCalories() {
    return  calories;
  }

  public void setCalories(int calories) {
    this.calories = calories;
  }

  public String getAllergies() {
    return allergies;
  }

  public void setAllergies(String allergies) {
    this.allergies = allergies;
  }

  public boolean isVegan() {
    return isVegan;
  }

  public void setVegan(boolean vegan) {
    isVegan = vegan;
  }

  public boolean isVegetarian() {
    return isVegetarian;
  }

  public void setVegetarian(boolean vegetarian) {
    isVegetarian = vegetarian;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}

