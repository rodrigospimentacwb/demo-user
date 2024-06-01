package br.com.pepper.demouser.commons;

import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.annotation.DirtiesContext.ClassMode;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTests extends AbstractDataGenerator {

    public String authenticate(String signInDtoJson, MockMvc mockMvc) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSignUpDtoJson()))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSignInDtoJson()))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseContent).get("token").asText();
    }

    public UserDto addUser(String signInDtoJson, MockMvc mockMvc) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        return objectMapper.readValue(responseContent, UserDto.class);
    }
}
