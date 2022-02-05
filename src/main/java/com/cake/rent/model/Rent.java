package com.cake.rent.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Entity(name = "rents")
@Data
public class Rent{
    @Transient
    private final float lateFee=5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    @Setter(AccessLevel.NONE)
    private LocalDate returnedDate;
    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "id_device")
    private Device device;

    @Transient
    private Float penalty;

    public void setReturnedDate(LocalDate returnedDate){
        if(returnedDate.isBefore(startDate)){
            returnedDate=startDate.plus(1,ChronoUnit.DAYS);
        }
        this.returnedDate=returnedDate;
    }

    public void setEndDateByDurationDays(int durationDays){
        if(this.startDate==null){
            this.setStartDate(LocalDate.now());
        }
        this.endDate=this.startDate.plus(Period.ofDays(durationDays));
    }
    public Long getDurationDays(){
        return startDate.until(endDate,ChronoUnit.DAYS);
    }

    public boolean isReturned(){
        return returnedDate!=null;
    }
    public boolean isLate(){
        if (returnedDate!=null){
            return returnedDate.isAfter(endDate);
        }else{
            return LocalDate.now().isAfter(endDate);
        }
    }
    public double lateFee(){
        if(penalty==null){
            if(isLate()){
                if(isReturned()){
                    penalty= lateFee*endDate.until(returnedDate, ChronoUnit.DAYS);
                }else {
                    return lateFee*endDate.until(LocalDate.now(),ChronoUnit.DAYS);
                }
            }else {
                return 0;
            }
        }
        return penalty;
    }

    public Long daysRemaining(){
        if(isReturned()){
            return returnedDate.until(endDate,ChronoUnit.DAYS);
        }else if(isLate()){
            return -endDate.until(LocalDate.now(),ChronoUnit.DAYS);
        }else{
            return LocalDate.now().until(endDate,ChronoUnit.DAYS);
        }
    }

}
