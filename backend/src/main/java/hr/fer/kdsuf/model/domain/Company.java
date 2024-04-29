package hr.fer.kdsuf.model.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdminUser> adminUsers;


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> devices;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Software> softwares;

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

    public void addAdminUser(AdminUser adminUser){
        if (adminUser == null) return;

        if (adminUsers == null){
            adminUsers = new ArrayList<>();
        }

        adminUsers.add(adminUser);
    }
}
