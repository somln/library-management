package sub.librarymanagement.persistence.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.domain.user.service.UserRepository;
import sub.librarymanagement.persistence.user.entity.User;

@Repository
@Transactional
@RequiredArgsConstructor
public class CoreUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void save(User user) {
        jpaUserRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}
