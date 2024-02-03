package pl.marek.securityjwt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = {"roles", "subordinates"})
@EqualsAndHashCode(exclude = {"roles", "subordinates"})
@Table(name = "users")
@SecondaryTable(name = "user_data")
public
class User {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    @Version
    @NotNull
    private Long version;

    @Column
    @NotNull
    private String email;

    @Column
    @NotNull
    @Size(min = 8)
    private String password;

    @Column
    @NotNull
    private boolean active;

    @Column(table = "user_data")
    @NotNull
    private String firstName;

    @Column(table = "user_data")
    @NotNull
    private String lastName;

    @Column(table = "user_data")
    @NotNull
    @Size(min = 9, max = 9)
    @Pattern(regexp = "[0-9]+")
    private String phoneNumber;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    @NotNull
    private Collection<Role> roles;

    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private User manager;

    @OneToMany(mappedBy = "manager")
    private Collection<User> subordinates;


    @Column
    @Length(min = 30, max = 30)
    private String resetPasswordToken;

    @Column
    @Future
    private LocalDateTime resetPasswordTokenExpirationDate;

    @OneToMany(mappedBy = "user")
    private Collection<RefreshToken> refreshTokens;
}