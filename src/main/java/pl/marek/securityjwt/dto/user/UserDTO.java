package pl.marek.securityjwt.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.marek.securityjwt.model.User;

@NoArgsConstructor
@Getter
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private boolean active;
    private String firstName;
    private String lastName;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}