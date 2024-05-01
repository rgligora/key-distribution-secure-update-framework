package hr.fer.kdsuf.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser {
    @Id
    private String userId;

    private String username;

    private String email;

    @ManyToMany()
    @JoinTable(
            name = "admin_user_companies",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "companyId")
    )
    private List<Company> companies;
}
