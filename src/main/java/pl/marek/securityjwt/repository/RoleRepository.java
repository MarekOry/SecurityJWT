package pl.marek.securityjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.marek.securityjwt.model.Role;

@Repository
@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.READ_COMMITTED)
public interface RoleRepository extends JpaRepository<Role, Long> {
}