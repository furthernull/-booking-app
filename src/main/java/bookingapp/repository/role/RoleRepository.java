package bookingapp.repository.role;

import bookingapp.model.user.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(Role.RoleName role);
}
