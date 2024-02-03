package pl.marek.securityjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marek.securityjwt.dto.RoleDTO;
import pl.marek.securityjwt.model.Role;
import pl.marek.securityjwt.service.RoleService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAll() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleDTO> roleDTOs = new ArrayList<>();
        roles.forEach(role -> roleDTOs.add(new RoleDTO(role)));
        return ResponseEntity.ok(roleDTOs);
    }
}