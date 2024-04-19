package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.dto.AdminUserDto;
import hr.fer.kdsuf.model.request.CreateAdminUserRequest;
import hr.fer.kdsuf.repository.AdminUserRepository;
import hr.fer.kdsuf.service.Impl.AdminUserServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin-users")
@CrossOrigin

public class AdminUserController {
    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private AdminUserServiceImpl service;

    @PostMapping
    public ResponseEntity<AdminUserDto> createAdminUser(@RequestBody CreateAdminUserRequest request){
        return ResponseEntity.ok(service.createAdminUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDto> retrieveAdminUser(@PathVariable("id") String id){
        return ResponseEntity.ok(service.retrieveAdminUser(id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteAdminUser(@PathVariable("id") String id){
        service.deleteAdminUser(id);
        return ResponseEntity.ok("Successfully deleted!");
    }
}
