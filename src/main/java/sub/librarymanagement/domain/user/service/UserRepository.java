package sub.librarymanagement.domain.user.service;

import sub.librarymanagement.persistence.user.entity.User;

public interface UserRepository {
    void save(User user);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
