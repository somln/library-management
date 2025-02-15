package sub.librarymanagement.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sub.api.LoginApi;
import sub.model.LoginRequest;

@RestController
@RequiredArgsConstructor
public class LoginController implements LoginApi {

    @Override
    public ResponseEntity<Void> login(LoginRequest loginRequest) {
        return LoginApi.super.login(loginRequest);
    }
}