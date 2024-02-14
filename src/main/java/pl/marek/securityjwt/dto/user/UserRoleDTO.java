package pl.marek.securityjwt.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
public class UserRoleDTO {

    @NotNull
    private ArrayList<Long> newRolesIds;

    private String version;
}