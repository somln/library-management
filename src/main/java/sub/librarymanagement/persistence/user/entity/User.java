package sub.librarymanagement.persistence.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sub.librarymanagement.persistence.user.enums.Role;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String username;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 6)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static User from(String username, String email, String password, String role) {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(Role.from(role))
                .build();
    }

}