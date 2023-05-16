package com.example.digitalmenuapi.controllerTest;

import com.example.digitalmenuapi.controller.AdminMenuController;
import com.example.digitalmenuapi.model.AdminMenuItem;
import com.example.digitalmenuapi.repository.AdminMenuRepository;
import com.example.digitalmenuapi.service.AdminMenuItemsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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

    @Autowired
    private AdminMenuRepository adminMenuRepository;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminMenuController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createNewSandwichesTest() throws Exception {
        AdminMenuItem adminMenuItem = new AdminMenuItem();
        adminMenuItem.setId("6454aa815f859e3504b2ec75");
        adminMenuItem.setName("Sweet Chilli Sandwich");
        adminMenuItem.setCalories(1300);
        adminMenuItem.setAllergies("wheat, poultry, chicken");
        adminMenuItem.setPrice(9.50);
        adminMenuItem.setAvailable(true);
        adminMenuItem.setVegan(false);
        adminMenuItem.setVegetarian(false);
       when(adminMenuItemsService.createNewSandwiches(any(AdminMenuItem.class))).thenReturn(adminMenuItem);
        String json = objectMapper.writeValueAsString(adminMenuItem);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "staff123");
        headers.setContentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(post("/admin/createMenu")
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
    @DisplayName("Test create new sandwich with invalid basic auth")
    public void createNewSandwichesWithInvalidBasicAuth() throws Exception {
        AdminMenuItem adminMenuItem = new AdminMenuItem();
        adminMenuItem.setId("6454aa815f859e3504b2ec75");
        adminMenuItem.setName("Tuna Mayo Sandwich");
        adminMenuItem.setCalories(900);
        adminMenuItem.setAllergies("wheat, poultry");
        adminMenuItem.setVegan(false);
        adminMenuItem.setVegetarian(false);
        adminMenuItem.setAvailable(true);
        adminMenuItem.setPrice(13.60);
        when(adminMenuItemsService.createNewSandwiches(any(AdminMenuItem.class))).thenReturn(adminMenuItem);
        String json = objectMapper.writeValueAsString(adminMenuItem);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("invalidUsername", "876876567598765456");
        headers.setContentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(post("/admin/createMenu")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .headers(headers)
                        .with(httpBasic("invalidUsername", "876876567598765456"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Tuna Mayo Sandwich"))
                .andExpect(jsonPath("$.calories").value(900))
                .andExpect(jsonPath("$.allergies").value("wheat, poultry"))
                .andExpect(jsonPath("$.price").value(13.60))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.vegan").value(false))
                .andExpect(jsonPath("$.vegetarian").value(false));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteMenuItemFromDatabase() throws Exception {
        AdminMenuItem adminMenuItem2 = new AdminMenuItem();
        adminMenuItem2.setId("6454d1411fc1e574b1439688");
        adminMenuItem2.setName("Tofu Steak Sandwich");
        adminMenuItem2.setCalories(1300);
        adminMenuItem2.setAllergies("wheat, poultry, chicken");
        adminMenuItem2.setPrice(9.50);
        adminMenuItem2.setAvailable(true);
        adminMenuItem2.setVegan(false);
        adminMenuItem2.setVegetarian(false);
        when(adminMenuItemsService.deleteMenuItem("6454d1411fc1e574b1439688")).thenReturn("Deleted successfully");
        String json = objectMapper.writeValueAsString(adminMenuItem2);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "staff123");
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
    public void getAllTemporaryAvailableSandwichesInAListTest() throws Exception {
        List<AdminMenuItem> adminMenuItems = Arrays.asList(
                new AdminMenuItem("Bacon Egg And Cheese Sandwich", 970,
                        "wheat, egg, cheese, mustard", false, false, 12.50, true),
                new AdminMenuItem("Tofu crab Sandwich", 500, "soy beans, wheat", true,
                        true, 18.50, true));
        given(adminMenuItemsService.getAllTemporaryAvailableSandwiches()).willReturn(adminMenuItems);
        String json = objectMapper.writeValueAsString(adminMenuItems);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "staff123");
        headers.setContentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/available")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bacon Egg And Cheese Sandwich"))
                .andExpect(jsonPath("$[0].calories").value(970))
                .andExpect(jsonPath("$[0].allergies").value("wheat, egg, cheese, mustard"))
                .andExpect(jsonPath("$[0].vegan").value(false))
                .andExpect(jsonPath("$[0].vegetarian").value(false))
                .andExpect(jsonPath("$[0].price").value(12.50))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[1].name").value("Tofu crab Sandwich"))
                .andExpect(jsonPath("$[1].calories").value(500))
                .andExpect(jsonPath("$[1].allergies").value("soy beans, wheat"))
                .andExpect(jsonPath("$[1].vegan").value(true))
                .andExpect(jsonPath("$[1].vegetarian").value(true))
                .andExpect(jsonPath("$[1].price").value(18.50))
                .andExpect(jsonPath("$[1].available").value(true));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllSandwichesTest() throws Exception {
        List<AdminMenuItem> adminMenuItems = Arrays.asList(
                new AdminMenuItem("Spicy Lobster Sandwich", 897, "wheat, mustard, fish", false, false, 10.75, true),
                new AdminMenuItem("Veggie Delight Sandwich", 500, "soy beans, wheat", true, true, 6.99, false));
        when(adminMenuItemsService.getAllSandwiches()).thenReturn(adminMenuItems);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "staff123");
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = objectMapper.writeValueAsString(adminMenuItems);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/menuList")
                        .content(json)
                        .headers(headers)
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
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[1].name").value("Veggie Delight Sandwich"))
                .andExpect(jsonPath("$[1].calories").value(500))
                .andExpect(jsonPath("$[1].allergies").value("soy beans, wheat"))
                .andExpect(jsonPath("$[1].vegan").value(true))
                .andExpect(jsonPath("$[1].vegetarian").value(true))
                .andExpect(jsonPath("$[1].price").value(6.99))
                .andExpect(jsonPath("$[1].available").value(false));

    }

}



