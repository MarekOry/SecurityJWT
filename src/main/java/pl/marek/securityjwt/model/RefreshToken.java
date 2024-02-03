package pl.marek.securityjwt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity(name="refresh_token")
@Data
@NoArgsConstructor
@ToString(exclude="user")
@EqualsAndHashCode(exclude="user")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @NotNull
    private Long version;

    @NotNull
    private String token;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @NotNull
    private User user;

    @NotNull
    @Future
    private LocalDateTime expiryDate;

    public RefreshToken(String token, User user, LocalDateTime expiryDate){
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }
}