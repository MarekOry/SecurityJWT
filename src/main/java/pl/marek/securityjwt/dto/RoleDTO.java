package pl.marek.securityjwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.marek.securityjwt.model.Role;

@NoArgsConstructor
@Getter
public class RoleDTO {
    private Long id;
    private Long version;
    private String name;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.version = role.getVersion();
        this.name = role.getName();
    }
}