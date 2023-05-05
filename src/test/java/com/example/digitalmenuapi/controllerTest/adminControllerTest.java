package com.example.digitalmenuapi.controllerTest;

import com.example.digitalmenuapi.controller.AdminMenuController;
import com.example.digitalmenuapi.model.AdminMenuItems;
import com.example.digitalmenuapi.service.AdminMenuItemsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteMenuItemFromDatabase() throws Exception {
            AdminMenuItems adminMenuItems2 = new AdminMenuItems();
            adminMenuItems2.setId("6454d1411fc1e574b1439688");
            adminMenuItems2.setName("Tofu Steak Sandwich");
            adminMenuItems2.setCalories(1300);
            adminMenuItems2.setAllergies("wheat, poultry, chicken");
            adminMenuItems2.setPrice(9.50);
            adminMenuItems2.setAvailable(true);
            adminMenuItems2.setVegan(false);
            adminMenuItems2.setVegetarian(false);
            when(adminMenuItemsService.deleteMenuItem("6454d1411fc1e574b1439688")).thenReturn("Deleted successfully");
            String json = objectMapper.writeValueAsString(adminMenuItems2);
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth("admin123", "staff");
            headers.setContentType(MediaType.APPLICATION_JSON);
            mockMvc.perform(delete("/admin")
                            .param("id", "6454d1411fc1e574b1439688")
                            .accept(MediaType.APPLICATION_JSON)
                            .headers(headers)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllTemporaryUnavailableSandwichesInAListTest() throws Exception {
        List<AdminMenuItems> adminMenuItems = Arrays.asList(
                new AdminMenuItems("Bacon Egg And Cheese Sandwich", 970,
                        "wheat, egg, cheese, mustard", false, false, 12.50, false),
                new AdminMenuItems("Tofu crab Sandwich", 500, "soy beans, wheat", true,
                        true, 18.50, false));
        given(adminMenuItemsService.getAllTemporaryUnavailableSandwiches()).willReturn(adminMenuItems);
        String json = objectMapper.writeValueAsString(adminMenuItems);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin123", "staff");
        headers.setContentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/unavailable")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bacon Egg And Cheese Sandwich"))
                .andExpect(jsonPath("$[0].calories").value(970))
                .andExpect(jsonPath("$[0].allergies").value("wheat, egg, cheese, mustard"))
                .andExpect(jsonPath("$[0].vegan").value(false))
                .andExpect(jsonPath("$[0].vegetarian").value(false))
                .andExpect(jsonPath("$[0].price").value(12.50))
                .andExpect(jsonPath("$[0].available").value(false))
                .andExpect(jsonPath("$[1].name").value("Tofu crab Sandwich"))
                .andExpect(jsonPath("$[1].calories").value(500))
                .andExpect(jsonPath("$[1].allergies").value("soy beans, wheat"))
                .andExpect(jsonPath("$[1].vegan").value(true))
                .andExpect(jsonPath("$[1].vegetarian").value(true))
                .andExpect(jsonPath("$[1].price").value(18.50))
                .andExpect(jsonPath("$[1].available").value(false));

    }
    }


