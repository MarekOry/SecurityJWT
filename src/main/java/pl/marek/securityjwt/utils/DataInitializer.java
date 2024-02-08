package pl.marek.securityjwt.utils;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.marek.securityjwt.model.Role;
import pl.marek.securityjwt.repository.RoleRepository;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    boolean setupIsDone = false;

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        if (setupIsDone) return;

        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER");
        Role employeeRole = createRoleIfNotFound("ROLE_EMPLOYEE");
        setupIsDone = true;
    }

    @Transactional
    public Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role = roleRepository.save(role);
        return role;
    }

}