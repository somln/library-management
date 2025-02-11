package sub.librarymanagement.persistence.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.domain.user.service.UserRepository;

@Repository
@Transactional
@RequiredArgsConstructor
public class CoreUserRepository implements UserRepository {

}
