package bookingapp.controller;

import static bookingapp.test.TestUtils.USER_REGISTRATION_REQUEST_DTO;
import static bookingapp.test.TestUtils.USER_RESPONSE_DTO;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bookingapp.dto.user.UserLoginRequestDto;
import bookingapp.dto.user.UserLoginResponseDto;
import bookingapp.dto.user.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/user/add-default-users.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/user/delete-user-related-data.sql")
            );
        }
    }

    @Test
    @DisplayName("Verify loginUser() method")
    void loginUser_ValidRequest_ShouldReturnToken() throws Exception {
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto(
                "jane.example@gmail.com",
                "Qwerty&0"
        );

        String jsonRequest = objectMapper.writeValueAsString(userLoginRequestDto);
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserLoginResponseDto userLoginResponseDto = objectMapper
                .readValue(result.getResponse().getContentAsString(), UserLoginResponseDto.class);
        assertNotNull(userLoginResponseDto);
        assertNotNull(userLoginResponseDto.token());

    }

    @Test
    @DisplayName("Verify registerUser() method")
    void registerUser_ValidRequest_ShouldReturnUserResponseDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(USER_REGISTRATION_REQUEST_DTO);
        MvcResult result = mockMvc.perform(post("/auth/registration")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto userResponseDto = objectMapper
                .readValue(result.getResponse().getContentAsString(), UserResponseDto.class);
        assertNotNull(userResponseDto);
        EqualsBuilder.reflectionEquals(USER_RESPONSE_DTO, userResponseDto, "id");
    }
}
