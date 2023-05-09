package com.example.digitalmenuapi.controllerTest;

import com.example.digitalmenuapi.controller.MenuItemsController;
import com.example.digitalmenuapi.model.MenuItems;
import com.example.digitalmenuapi.service.MenuItemsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
}

    



