package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.CreateSoftwarePackageRequest;
import hr.fer.kdsuf.service.Impl.SoftwarePackageServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/software-packages")
@CrossOrigin
public class SoftwarePackageController {

    @Autowired
    private SoftwarePackageServiceImpl service;


    @PostMapping
    public ResponseEntity<SoftwarePackageDto> createSoftwarePackage(@RequestBody CreateSoftwarePackageRequest request){
        return ResponseEntity.ok(service.createSoftwarePackage(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoftwarePackageDto> retrieveSoftwarePackage(@PathVariable("id") String id){
        return  ResponseEntity.ok(service.retrieveSoftwarePackage(id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteSoftwarePackage(@PathVariable("id") String id){
        service.deleteSoftwarePackage(id);
        return ResponseEntity.ok("Successfully deleted!");
    }
}
