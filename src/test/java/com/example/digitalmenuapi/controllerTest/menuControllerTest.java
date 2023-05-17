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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
            List<MenuItems> sandwiches = Arrays.asList(
                    new MenuItems( "Veggie Delight",700,"wheat,mustard",true,true,9.00 ),
                    new MenuItems( "BLT wrap",900,"poultry,diary",false,false,12.00 ),
                    new MenuItems( "Tomato and cheese",500,"diary,mustard",false,true,9.00 ));


            // perform the request
            String url = "/sandwiches?allergies=Wheat&vegan=false&vegetarian=true&maxCalories=1000&maxPrice=10";
            mockMvc.perform(post("/menu/list")
                            .accept(MediaType.APPLICATION_JSON)
                            .content(url)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.[0].name").value("Veggie Delight"))
                    .andExpect(jsonPath("$.[0].allergies").value("wheat,mustard"))
                    .andExpect(jsonPath("$.[0].vegan").value(true))
                    .andExpect(jsonPath("$.[0].vegetarian").value(true))
                    .andExpect(jsonPath("$.[0].calories").value(700))
                    .andExpect(jsonPath("$.[0].price").value(8.99))
                    .andExpect(jsonPath("$.[1].id").value("2"))
                    .andExpect(jsonPath("$.[1].name").value("Tuna Melt"))
                    .andExpect(jsonPath("$.[1].allergies").doesNotExist())
                    .andExpect(jsonPath("$.[1].vegan").value(false))
                    .andExpect(jsonPath("$.[1].vegetarian").value(false))
                    .andExpect(jsonPath("$.[1].calories").value(850))
                    .andExpect(jsonPath("$.[1].price").value(9.99))
                    .andExpect(jsonPath("$.[2].id").value("3"))
                    .andExpect(jsonPath("$.[2].name").value("Turkey Club"))
                    .andExpect(jsonPath("$.[2].allergies").doesNotExist())
                    .andExpect(jsonPath("$.[2].vegan").value(false))
                    .andExpect(jsonPath("$.[2].vegetarian").value(false))
                    .andExpect(jsonPath("$.[2].calories").value(1000))
                    .andExpect(jsonPath("$.[2].price").value(10.99));

            // verify the service call
            ArgumentCaptor<SandwichCriteria> argumentCaptor = ArgumentCaptor.forClass(SandwichCriteria.class);
            verify(sandwichService).getSandwichesByCriteria(argumentCaptor.capture());
            SandwichCriteria capturedCriteria = argumentCaptor.getValue();
            assertThat(capturedCriteria.getAllergies()).isEqualTo("Wheat");
            assertThat(capturedCriteria.isVegan()).isFalse();
            assertThat(capturedCriteria.isVegetarian()).isTrue();
            assertThat(capturedCriteria.getMaxCalories()).isEqualTo(1000);
            assertThat(capturedCriteria.getMaxPrice()).isEqualByComparingTo


        }
