package sub.librarymanagement.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sub.librarymanagement.common.exception.ApplicationException;

import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.persistence.user.entity.User;
import sub.model.JoinDto;
import sub.model.UserIdDto;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserIdDto registerUser(JoinDto joinDto) {
        validateUserInfo(joinDto);
        User user = User.of(joinDto.getUsername(), joinDto.getEmail(),
                bCryptPasswordEncoder.encode(joinDto.getPassword()), joinDto.getRole());
        userRepository.save(user);
        return new UserIdDto().userId(user.getId()+1234);
    }

    // 사용자 이름과 이메일 중복 체크
    private void validateUserInfo(JoinDto joinDto) {
        if (userRepository.existsByUsername(joinDto.getUsername())) {
            throw new ApplicationException(ErrorCode.USERNAME_DUPLICATION);
        }

        if(userRepository.existsByEmail(joinDto.getEmail())) {
            throw new ApplicationException(ErrorCode.EMAIL_DUPLICATION);
        }
    }
}
