package com.cake.rent.serviceImp;

import com.cake.rent.model.Client;
import com.cake.rent.repository.IClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService extends GenericService<Client, Long>{
    @Autowired
    private IClientRepository repository;
    @Override
    public JpaRepository<Client, Long> getDao() {
        return repository;
    }

    public Client findByNameAndLastName(String name, String lastname) {
        return repository.findByNameAndLastName(name,lastname);
    }
}
