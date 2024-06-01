package br.com.pepper.demouser.integration.tests;

import br.com.pepper.demouser.commons.AbstractIntegrationTests;
import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserIntegrationTest extends AbstractIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_search_users_endpoint_with_authentication() throws Exception {
        String signInDtoJson = createSignInDtoJson();

        String authenticateToken = authenticate(signInDtoJson, mockMvc);

        mockMvc.perform(get("/users?page=0&size=10&sortField=name&sortDirection=asc&name="+USER_NAME.substring(0,3))
                        .header("Authorization", "Bearer " + authenticateToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(USER_NAME))
                .andExpect(jsonPath("$.content[0].email").value(USER_EMAIL))
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    public void test_search_users_endpoint_page_two_no_records() throws Exception {
        String signInDtoJson = createSignInDtoJson();
        String authenticateToken = authenticate(signInDtoJson, mockMvc);

        mockMvc.perform(get("/users?page=1&size=10&sortField=name&sortDirection=asc&name="+USER_NAME.substring(0,3))
                        .header("Authorization", "Bearer " + authenticateToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    public void test_search_users_endpoint_ordering_asc() throws Exception {
        String signInDtoJson = createSignInDtoJson();
        String authenticateToken = authenticate(signInDtoJson, mockMvc);

        UserDto user2 = addUser(createSignUpDtoJson("Mike", "mike.teste@gmail.com", "654321"), mockMvc);
        UserDto user3 = addUser(createSignUpDtoJson("Angela", "angela.teste@gmail.com", "896541"), mockMvc);

        mockMvc.perform(get("/users?page=0&size=10&sortField=name&sortDirection=asc")
                        .header("Authorization", "Bearer " + authenticateToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].name").value("Angela"))
                .andExpect(jsonPath("$.content[1].name").value(USER_NAME))
                .andExpect(jsonPath("$.content[2].name").value("Mike"));
    }

    @Test
    public void test_search_users_endpoint_ordering_desc() throws Exception {
        String signInDtoJson = createSignInDtoJson();
        String authenticateToken = authenticate(signInDtoJson, mockMvc);

        UserDto user2 = addUser(createSignUpDtoJson("Mike", "mike.teste@gmail.com", "654321"), mockMvc);
        UserDto user3 = addUser(createSignUpDtoJson("Angela", "angela.teste@gmail.com", "896541"), mockMvc);

        mockMvc.perform(get("/users?page=0&size=10&sortField=name&sortDirection=desc")
                        .header("Authorization", "Bearer " + authenticateToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].name").value("Mike"))
                .andExpect(jsonPath("$.content[1].name").value(USER_NAME))
                .andExpect(jsonPath("$.content[2].name").value("Angela"));
    }

    @Test
    public void test_pagination_returns_correct_page_of_users() throws Exception {
        String signInDtoJson = createSignInDtoJson();
        String authenticateToken = authenticate(signInDtoJson, mockMvc);

        UserDto user2 = addUser(createSignUpDtoJson("Mike", "mike.teste@gmail.com", "654321"), mockMvc);
        UserDto user3 = addUser(createSignUpDtoJson("Angela", "angela.teste@gmail.com", "896541"), mockMvc);

        mockMvc.perform(get("/users?page=1&size=1&sortField=name&sortDirection=desc")
                        .header("Authorization", "Bearer " + authenticateToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value(USER_NAME));
    }

    @Test
    public void test_search_users_endpoint_using_id() throws Exception {
        String signInDtoJson = createSignInDtoJson();

        String authenticateToken = authenticate(signInDtoJson, mockMvc);

        UserDto user2 = addUser(createSignUpDtoJson("Mike", "mike.teste@gmail.com", "654321"), mockMvc);
        UserDto user3 = addUser(createSignUpDtoJson("Angela", "angela.teste@gmail.com", "896541"), mockMvc);

        mockMvc.perform(get("/users/2")
                        .header("Authorization", "Bearer " + authenticateToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mike"))
                .andExpect(jsonPath("$.email").value("mike.teste@gmail.com"));
    }
}
