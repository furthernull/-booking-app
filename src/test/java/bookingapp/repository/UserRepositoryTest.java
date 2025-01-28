package bookingapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bookingapp.model.user.User;
import bookingapp.repository.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/user/delete-user-related-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/user/add-default-users.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/user/delete-user-related-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Verify existByEmail() method should user exist")

    void existByEmail_ValidEmail_ReturnTrue() {
        String email = "john.example@email.com";

        boolean actual = userRepository.existsByEmail(email);
        assertTrue(actual);
    }

    @Test
    @DisplayName("Verify exitsByEmail() method could not exist")
    void existByEmail_NotValidEmail_ReturnFalse() {
        String email = "noexist@test.com";

        boolean actual = userRepository.existsByEmail(email);
        assertFalse(actual);
    }

    @Test
    @DisplayName("Verify findByEmail() should return exist user")
    void findByEmail_ValidEmail_ReturnExistingUser() {
        String email = "jane.example@gmail.com";

        Optional<User> actual = userRepository.findByEmail(email);
        assertTrue(actual.isPresent());
        assertEquals(email, actual.get().getEmail());
    }

    @Test
    @DisplayName("Verify findByEmail() method should return empty optional")
    void findByEmail_NotValidEmail_ReturnEmptyUser() {
        String email = "noexist@test.com";

        Optional<User> actual = userRepository.findByEmail(email);
        assertFalse(actual.isPresent());
    }
}
