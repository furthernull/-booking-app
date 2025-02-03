package bookingapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bookingapp.model.accommodation.Accommodation;
import bookingapp.repository.accommodation.AccommodationRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/accommodation/delete-accommodation-related-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/accommodation/add-default-two-accommodation.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/accommodation/delete-accommodation-related-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AccommodationRepositoryTest {
    @Autowired
    private AccommodationRepository accommodationRepository;

    @Test
    @DisplayName("Verify findAll() method")
    void findAll_ReturnsTwoAccommodations() {
        Integer expectedSize = 2;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Accommodation> accommodations = accommodationRepository.findAll(pageable);

        Integer actualSize = accommodations.getContent().size();
        assertEquals(expectedSize, actualSize);

    }

    @Test
    @DisplayName("Verify findById() method valid id")
    void findById_ValidId_ReturnValidAccommodation() {
        Long expectedId = 2L;
        Optional<Accommodation> accommodation = accommodationRepository.findById(expectedId);

        assertTrue(accommodation.isPresent());
        assertEquals(expectedId, accommodation.get().getId());
    }

    @Test
    @DisplayName("Verify findById() method not valid id")
    void findById_NotValidId_ReturnEmptyOptional() {
        Optional<Accommodation> accommodation = accommodationRepository.findById(Long.MAX_VALUE);
        assertTrue(accommodation.isEmpty());
    }
}
