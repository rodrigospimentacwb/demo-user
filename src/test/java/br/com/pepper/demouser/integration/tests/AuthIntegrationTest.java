package br.com.pepper.demouser.integration.tests;

import br.com.pepper.demouser.commons.AbstractIntegrationTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthIntegrationTest extends AbstractIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test_signin_without_user_should_return_unauthorized() throws Exception {
        String signInDtoJson = createSignInDtoJson();

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void test_signin_with_user_should_return_authorized() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSignUpDtoJson()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSignInDtoJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void test_access_protected_url_without_user_should_return_unauthorized() throws Exception {
        String signInDtoJson = createSignInDtoJson();

        mockMvc.perform(post("/users?page=0&size=10&sortField=name&sortDirection=asc&name=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void test_access_protected_url_with_logged_in_user_should_return_authorized() throws Exception {
        String signInDtoJson = createSignInDtoJson();

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSignUpDtoJson()))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSignInDtoJson()))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseContent).get("token").asText();

        mockMvc.perform(get("/users?page=0&size=10&sortField=name&sortDirection=asc&name=test")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(status().isOk());
    }
}
