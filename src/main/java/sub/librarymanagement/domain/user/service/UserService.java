package sub.librarymanagement.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.domain.user.dto.JoinDto;
import sub.librarymanagement.domain.user.dto.UserIdDto;
import sub.librarymanagement.persistence.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserIdDto registerUser(JoinDto joinDto) {

        if(userRepository.existsByEmail(joinDto.email())) {
            throw new ApplicationException(ErrorCode.EMAIL_DUPLICATION);
        }

        User user = User.from(joinDto.username(), joinDto.email(),
                bCryptPasswordEncoder.encode(joinDto.password()), joinDto.role());
        userRepository.save(user);
        return UserIdDto.from(user.getId());
    }
}
