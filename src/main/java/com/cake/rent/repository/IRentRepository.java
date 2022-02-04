package com.cake.rent.repository;

import com.cake.rent.model.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRentRepository extends JpaRepository<Rent, Long> {
}
