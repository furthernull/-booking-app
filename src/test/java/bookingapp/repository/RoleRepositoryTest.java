package bookingapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bookingapp.model.user.Role;
import bookingapp.repository.role.RoleRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Verify findByRole() method could return valid")
    void findByRole() {
        Role.RoleName expected = Role.RoleName.CUSTOMER;

        Optional<Role> byRole = roleRepository.findByRole(expected);
        assertTrue(byRole.isPresent());
        assertEquals(expected, byRole.get().getRole());
    }
}
