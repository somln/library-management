package sub.librarymanagement.domain.user.service;

import sub.librarymanagement.persistence.user.entity.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    User getByUsername(String username);
}
