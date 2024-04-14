package hr.fer.kdsuf.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Device {
    @Id
    private String id;

    private String model;

    private String manufacturer;

    //@OneToOne(cascade = CascadeType.DETACH)
    private String currentSoftware;

    //@OneToOne(cascade = CascadeType.DETACH)
    private String fallbackSoftware;
}