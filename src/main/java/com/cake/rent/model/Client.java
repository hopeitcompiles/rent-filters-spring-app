package com.cake.rent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "clients")
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Rent> rents;

    @Transient
    @Getter(AccessLevel.NONE)
    private Boolean hasBeenFined;
    @Transient
    @Getter(AccessLevel.NONE)
    private Integer totalFinedRents;
    @Transient
    @Getter(AccessLevel.NONE)
    private double totalFined;

    public double getTotalFined(){
        if (getHasBeenFined()){
            return totalFined;
        }
        return 0F;
    }

    public Boolean getHasBeenFined(){
        if (hasBeenFined==null){
            hasBeenFined=(rentsWithFine()>0);
        }
        return hasBeenFined;
    }
    public Integer rentsWithFine() {
        if (totalFinedRents == null){
            totalFined= 0F;
            totalFinedRents = 0;
            if (rents.size() > 0) {
                for (Rent rent : rents) {
                    if (rent.isLate()) {
                        totalFinedRents++;
                        totalFined+=rent.lateFee();
                    }
                }
            }
        }
        return totalFinedRents;
    }
}
