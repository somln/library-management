package sub.librarymanagement.persistence.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sub.librarymanagement.persistence.user.entity.User;

public interface JpaUserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
