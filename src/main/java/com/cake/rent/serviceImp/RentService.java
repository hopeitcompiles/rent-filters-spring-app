package com.cake.rent.serviceImp;

import com.cake.rent.model.Device;
import com.cake.rent.model.Rent;
import com.cake.rent.model.Status;
import com.cake.rent.repository.IRentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentService extends GenericService<Rent, Long>{
    @Autowired
    EntityManager em;
    @Autowired
    DeviceService deviceService;
    @Autowired
    private IRentRepository repository;
    @Override
    public JpaRepository<Rent, Long> getDao() {
        return repository;
    }
    @Override
    public Rent save(Rent rent){
        if (rent.getDevice().getStatus()!= Status.AVAILABLE){
            System.out.println("Device is not available");
            return  new Rent();
        }else {
            Rent rented = repository.save(rent);
            Device device = rent.getDevice();
            if(rent.isReturned()){
                device.setStatusAvailable();
            }else {
                device.setStatusRented();
            }
            deviceService.save(device);
            return rented;
        }
    }


    public Page<Rent> getFiltered(Pageable pageable, LocalDate startDate, LocalDate endDate, String returned, Long deviceid){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rent> cq = cb.createQuery(Rent.class);

        Root<Rent> rent = cq.from(Rent.class);

        List<Predicate> predicates = new ArrayList<>();

        if(startDate!=null && endDate!=null){
            if(startDate.isAfter(endDate)){
                LocalDate temp=startDate;
                startDate=endDate;
                endDate=temp;
            }
            predicates.add(cb.between(rent.get("startDate"),startDate,endDate));
        }

        if(returned!=null && !returned.isEmpty()){
            if(returned.compareTo("returned")==0){
                predicates.add(cb.isNotNull(rent.get("returnedDate")));
            }
            if(returned.compareTo("noreturned")==0){
                predicates.add(cb.isNull(rent.get("returnedDate")));
            }
        }
        if(deviceid!=null && deviceid>0){
            predicates.add(cb.equal(rent.get("device"),deviceid));
        }
        if(predicates.isEmpty()){
            return repository.findAll(pageable);
        }

        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Rent> query = em.createQuery(cq);
        int totalRows = query.getResultList().size();

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        Page<Rent> result = new PageImpl<>(query.getResultList(), pageable, totalRows);
        return result;
    }
}
