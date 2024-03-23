package pl.marek.securityjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.marek.securityjwt.model.User;

import java.util.Optional;

@Repository
@Transactional(isolation = Isolation.READ_COMMITTED)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}