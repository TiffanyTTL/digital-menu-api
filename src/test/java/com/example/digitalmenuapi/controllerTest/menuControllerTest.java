package com.example.digitalmenuapi.controllerTest;

import com.example.digitalmenuapi.controller.MenuItemsController;
import com.example.digitalmenuapi.controller.UserMenuController;
import com.example.digitalmenuapi.model.MenuItems;
import com.example.digitalmenuapi.repository.MenuItemsRepository;
import com.example.digitalmenuapi.service.MenuItemsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class menuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MenuItemsService menuItemsService;

    @Mock
    private MenuItemsRepository menuItemsRepository;

    @InjectMocks
    private MenuItemsController menuItemsController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuItemsController).build();
    }

    @Test
    public void createNewSandwichesTest() throws Exception {
        MenuItems menuItems = new MenuItems();
        menuItems.setName("Tuna Mayo Sandwich");
        menuItems.setCalories(900);
        menuItems.setAllergies("wheat, poultry");
        menuItems.setVegan(false);
        menuItems.setVegetarian(false);
        menuItems.setPrice(13.60);
        when(menuItemsService.createSandwiches(any(MenuItems.class))).thenReturn(menuItems);
        String json = objectMapper.writeValueAsString(menuItems);
        mockMvc.perform(post("/menu/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Sandwich created successfully"))
                .andExpect(content().string(notNullValue()));
    }

    @Test
    void testGetSandwichesByCriteria() throws Exception {
        // mock the service response
        List<MenuItems> sandwiches = new ArrayList<>();
               sandwiches.add(new MenuItems("Veggie Delight", 700, "wheat,mustard", true, true, 9.00));
               sandwiches.add(new MenuItems("BLT wrap", 1200, "poultry,diary", false, false, 12.00));
               sandwiches.add(new MenuItems("Tomato and cheese", 500, "diary,mustard", false, true, 9.00));
        when(menuItemsService.getSandwichesByFilter(true, true, 1000, 12.00, "poultry,diary")).thenReturn(sandwiches);
        // perform the request
         mockMvc.perform(MockMvcRequestBuilders.get("/menu/list")
                        .param("vegan", "true")
                        .param("vegetarian", "true")
                        .param("calories", "1000")
                        .param("allergy", "poultry,diary")
                        .param("price", "12.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Veggie Delight"))
                .andExpect(jsonPath("$[2].name").value("Tomato and cheese"));


    }
}
