package com.cake.rent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "devices")
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status;
    @JsonIgnore
    @OneToMany(mappedBy = "device")
    private List<Rent> rents;

    public void setStatusAvailable() {
        this.status=Status.AVAILABLE;
    }

    public void setStatusRented() {
        this.status=Status.RENTED;
    }
    public boolean isRented(){
        return status==Status.RENTED;
    }
    public void setStatusDisable() {
        this.status=Status.DISABLED;
    }
}
