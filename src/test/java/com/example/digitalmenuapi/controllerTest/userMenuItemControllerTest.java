package com.example.digitalmenuapi.controllerTest;

import com.example.digitalmenuapi.controller.UserMenuController;
import com.example.digitalmenuapi.model.MenuItems;
import com.example.digitalmenuapi.service.MenuItemsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class userMenuItemControllerTest {

    @Autowired
   private MockMvc mockMvc;

    @Mock
   private MenuItemsService menuItemsService;

    @InjectMocks
     private UserMenuController userMenuController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userMenuController).build();
    }



    @Test
    public void getAllSandwichesTest() throws Exception {
        List<MenuItems> menuItemsList = Arrays.asList(
        new MenuItems("Spicy Lobster Sandwich", 897, "wheat, mustard, fish", false, false, 10.75),
        new MenuItems("Veggie Delight Sandwich", 500, "soy beans, wheat", true, true, 6.99));
        when(menuItemsService.getAllMenuItems()).thenReturn(menuItemsList);
        String json = objectMapper.writeValueAsString(menuItemsList);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/menuList")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Spicy Lobster Sandwich"))
                .andExpect(jsonPath("$[0].calories").value(897))
                .andExpect(jsonPath("$[0].allergies").value("wheat, mustard, fish"))
                .andExpect(jsonPath("$[0].vegan").value(false))
                .andExpect(jsonPath("$[0].vegetarian").value(false))
                .andExpect(jsonPath("$[0].price").value(10.75))
                .andExpect(jsonPath("$[1].name").value("Veggie Delight Sandwich"))
                .andExpect(jsonPath("$[1].calories").value(500))
                .andExpect(jsonPath("$[1].allergies").value("soy beans, wheat"))
                .andExpect(jsonPath("$[1].vegan").value(true))
                .andExpect(jsonPath("$[1].vegetarian").value(true))
                .andExpect(jsonPath("$[1].price").value(6.99));

    }

    @Test
    public void testGetAllMenuItems_NoItemsFound() throws Exception {
        when(menuItemsService.getAllMenuItems()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/user/menuList"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}