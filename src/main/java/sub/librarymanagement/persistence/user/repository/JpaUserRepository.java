package sub.librarymanagement.persistence.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sub.librarymanagement.persistence.user.entity.User;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    User getByUsername(String username);
}
