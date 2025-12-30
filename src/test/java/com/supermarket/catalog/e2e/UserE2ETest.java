package com.supermarket.catalog.e2e;

import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.repository.UserRepository;
import com.supermarket.catalog.testinfra.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserE2ETest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser_userIsPersistedCorrectly() throws Exception {

        CreateUserRequest request = CreateUserRequest.builder()
                .username("e2e_user")
                .password("password")
                .email("e2e@test.com")
                .storeName("E2E Store")
                .build();

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UUID userId = objectMapper.readValue(response, UUID.class);

        User user = userRepository.findById(userId)
                .orElseThrow();

        assertThat(user.getUsername()).isEqualTo("e2e_user");
        assertThat(user.getEmail()).isEqualTo("e2e@test.com");
        assertThat(user.getStore().getName()).isEqualTo("E2E Store");
    }
}