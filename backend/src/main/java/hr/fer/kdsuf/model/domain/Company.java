package hr.fer.kdsuf.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    private String companyId;

    private String name;

    @ManyToMany(mappedBy = "companies", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<AdminUser> adminUsers;


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> devices;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Software> softwares;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SoftwarePackage> softwarePackages;


    public void addDevice(Device device) {
        if (device == null) return;

        if (devices == null) {
            devices = new ArrayList<>();
        }

        devices.add(device);

    }

    public void addSoftware(Software software) {
        if (software == null) return;

        if (softwares == null) {
            softwares = new ArrayList<>();
        }

        softwares.add(software);

    }

    public void addSoftwarePackage(SoftwarePackage softwarePackage) {
        if (softwarePackage == null) return;

        if (softwarePackages == null) {
            softwarePackages = new ArrayList<>();
        }

        softwarePackages.add(softwarePackage);

    }

    public void addAdminUser(AdminUser adminUser){
        if (adminUser == null) return;

        if (adminUsers == null){
            adminUsers = new ArrayList<>();
        }

        adminUsers.add(adminUser);
    }
}
