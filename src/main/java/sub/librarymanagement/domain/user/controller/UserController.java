package sub.librarymanagement.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sub.librarymanagement.common.util.ResponseDto;
import sub.librarymanagement.domain.user.dto.JoinDto;
import sub.librarymanagement.domain.user.dto.UserIdDto;
import sub.librarymanagement.domain.user.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto<UserIdDto>> registerUser(@Valid @RequestBody JoinDto joinDto) {
        UserIdDto userIdDto = userService.registerUser(joinDto);
        return ResponseEntity.ok(ResponseDto.okWithData(userIdDto));
    }
}
