package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.dto.CompanyDto;
import hr.fer.kdsuf.model.request.CreateCompanyRequest;
import hr.fer.kdsuf.repository.CompanyRepository;
import hr.fer.kdsuf.service.Impl.CompanyServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@CrossOrigin
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyServiceImpl service;

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CreateCompanyRequest request){
        return ResponseEntity.ok(service.createCompany(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> retrieveCompany(@PathVariable("id") String id){
        return ResponseEntity.ok(service.retrieveCompany(id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteCompany(@PathVariable("id") String id){
        service.deleteCompany(id);
        return ResponseEntity.ok("Successfully deleted!");
    }
}
