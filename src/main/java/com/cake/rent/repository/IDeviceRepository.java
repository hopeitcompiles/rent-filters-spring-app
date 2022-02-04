package com.cake.rent.repository;

import com.cake.rent.model.Device;
import com.cake.rent.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IDeviceRepository extends JpaRepository<Device, Long> {
    Device findByName(String name);
}
