package com.cake.rent.repository;

import com.cake.rent.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClientRepository extends JpaRepository<Client, Long> {
    public Client findByNameAndLastName(String name, String lastName);
    Page<Client> findAll(Pageable pageable);
}
