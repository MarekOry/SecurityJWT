package pl.marek.securityjwt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collection;

@Entity(name = "role")
@Data
@NoArgsConstructor
@ToString(exclude = {"users"})
@EqualsAndHashCode(exclude = {"users"})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @NotNull
    private Long version;

    @NotNull
    String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role(String name) {
        this.name = name;
    }
}