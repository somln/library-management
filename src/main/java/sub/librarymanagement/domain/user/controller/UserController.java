package sub.librarymanagement.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sub.api.JoinApi;
import sub.librarymanagement.domain.user.service.UserService;
import sub.model.JoinDto;
import sub.model.ResponseDtoUserIdDto;
import sub.model.UserIdDto;

@RestController
@RequiredArgsConstructor
public class UserController implements JoinApi {

    private final UserService userService;

    @Override
    public ResponseEntity<ResponseDtoUserIdDto> createUser(JoinDto joinDto) {
        UserIdDto userIdDto = userService.registerUser(joinDto);
        ResponseDtoUserIdDto response = new ResponseDtoUserIdDto().code(200).data(userIdDto);

        return ResponseEntity.ok(response);
    }

}
