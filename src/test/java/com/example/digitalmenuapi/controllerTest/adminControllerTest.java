package com.example.digitalmenuapi.controllerTest;

import com.example.digitalmenuapi.controller.AdminMenuController;
import com.example.digitalmenuapi.model.AdminMenuItems;
import com.example.digitalmenuapi.service.AdminMenuItemsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class adminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AdminMenuItemsService adminMenuItemsService;

    @InjectMocks
    private AdminMenuController adminMenuController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminMenuController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createNewSandwichesTest() throws Exception {
        AdminMenuItems adminMenuItems = new AdminMenuItems();
        adminMenuItems.setId("6454aa815f859e3504b2ec75");
        adminMenuItems.setName("Sweet Chilli Sandwich");
        adminMenuItems.setCalories(1300);
        adminMenuItems.setAllergies("wheat, poultry, chicken");
        adminMenuItems.setPrice(9.50);
        adminMenuItems.setAvailable(true);
        adminMenuItems.setVegan(false);
        adminMenuItems.setVegetarian(false);
        when(adminMenuItemsService.createNewSandwiches(any(AdminMenuItems.class))).thenReturn(adminMenuItems);
        String json = objectMapper.writeValueAsString(adminMenuItems);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin123", "staff");
        headers.setContentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(post("/admin/menu")
                        .accept(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sweet Chilli Sandwich"))
                .andExpect(jsonPath("$.calories").value(1300))
                .andExpect(jsonPath("$.allergies").value("wheat, poultry, chicken"))
                .andExpect(jsonPath("$.price").value(9.50))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.vegan").value(false))
                .andExpect(jsonPath("$.vegetarian").value(false));
    }
}

