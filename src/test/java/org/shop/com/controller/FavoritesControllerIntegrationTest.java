package org.shop.com.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.shop.com.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoritesControllerIntegrationTest {

    @MockBean
    private JwtService jwtService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    public void testAddProductToFavorites() throws Exception {
        String favoritesCreateDtoJson = "{\"productId\":1}";

        mockMvc.perform(post("/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(favoritesCreateDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(1));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    public void testGetAllFavorites() throws Exception {
        mockMvc.perform(get("/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].unexpectedField").doesNotExist())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    public void testRemoveProductFromFavorites() throws Exception {
        mockMvc.perform(delete("/v1/favorites/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
