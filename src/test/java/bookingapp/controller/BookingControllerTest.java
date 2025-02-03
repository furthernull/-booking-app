package bookingapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bookingapp.dto.booking.BookingRequestDto;
import bookingapp.dto.booking.BookingResponseDto;
import bookingapp.dto.booking.BookingUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class BookingControllerTest {
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
                    new ClassPathResource("database/booking/add-default-two-bookings.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/booking/delete-booking-related-data.sql")
            );
        }
    }

    @Test
    @DisplayName("Verify create() method")
    @WithUserDetails(value = "john.example@email.com",
            userDetailsServiceBeanName = "customUserDetailsService")
    void create_ValidRequest_ReturnValidResponse() throws Exception {
        LocalDate checkIn = LocalDate.now().plusMonths(1);
        LocalDate checkOut = LocalDate.now().plusMonths(2);
        BookingRequestDto requestDto = new BookingRequestDto(1L, checkIn, checkOut);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/bookings")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookingResponseDto bookingResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookingResponseDto.class);
        assertNotNull(bookingResponseDto);
    }

    @Test
    @DisplayName("Verify getBookingsByIdAndStatus() method")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getBookingsByIdAndStatus_ValidStatusParam_ReturnValidResponse() throws Exception {
        MvcResult result = mockMvc.perform(get("/bookings")
                        .param("status", "PENDING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookingResponseDto[] responseDtos = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookingResponseDto[].class);
        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.length);
    }

    @Test
    @DisplayName("Verify getBookingsByUserId() method")
    @WithUserDetails(value = "john.example@email.com",
            userDetailsServiceBeanName = "customUserDetailsService")
    void getBookingsByUserId_ValidUserIdParam_ReturnValidResponse() throws Exception {
        MvcResult result = mockMvc.perform(get("/bookings/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookingResponseDto[] responseDtos = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookingResponseDto[].class);

        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.length);
        assertEquals(1, responseDtos[0].customerId());
    }

    @Test
    @DisplayName("Verify getBookingById() method")
    @WithUserDetails(value = "john.example@email.com",
            userDetailsServiceBeanName = "customUserDetailsService")
    void getBookingById_ValidIdParam_ReturnValidResponse() throws Exception {
        Long id = 2L;
        MvcResult result = mockMvc.perform(get("/bookings/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookingResponseDto bookingResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookingResponseDto.class);
        assertNotNull(bookingResponseDto);
        assertEquals(id, bookingResponseDto.id());
    }

    @Test
    @DisplayName("Verify updateBookingById() method")
    @WithUserDetails(value = "john.example@email.com",
            userDetailsServiceBeanName = "customUserDetailsService")
    void updateBookingById_ValidUpdateRequestDto_ReturnValidResponse() throws Exception {
        Long id = 1L;
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusWeeks(2);
        BookingUpdateRequestDto updateRequestDto = new BookingUpdateRequestDto(checkIn, checkOut);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);
        MvcResult result = mockMvc.perform(patch("/bookings/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookingResponseDto bookingResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookingResponseDto.class);
        assertNotNull(bookingResponseDto);
        assertEquals(id, bookingResponseDto.id());
        assertEquals(checkIn, bookingResponseDto.checkInDate());
        assertEquals(checkOut, bookingResponseDto.checkOutDate());
    }

    @Test
    @DisplayName("Verify cancelBookingById() method")
    @WithUserDetails(value = "jane.example@gmail.com",
            userDetailsServiceBeanName = "customUserDetailsService")
    @Sql(scripts = "classpath:database/booking/add-temporary-booking.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void cancelBookingById_ValidCancelRequest_ReturnValidStatus() throws Exception {
        Long id = 3L;

        mockMvc.perform(delete("/bookings/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
