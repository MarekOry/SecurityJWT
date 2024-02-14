package pl.marek.securityjwt.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class UserEditDTO {
    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    @Size(min = 3, max = 20)
    private String lastName;

    @NotNull
    @Size(min = 9, max = 9)
    @Pattern(regexp = "[0-9]+")
    private String phoneNumber;

    private long version;
}