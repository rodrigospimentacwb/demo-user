package br.com.pepper.demouser.domains.auth.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_Authenticate_ValidSignInDto_Returns_Unauthorized() throws Exception {
        String signInDtoJson = "{\"email\": \"test@test.com\", \"password\": \"testPassword\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
