package sub.librarymanagement.persistence.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User {

    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 255)
    private String username;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 6)
    private String password;

}