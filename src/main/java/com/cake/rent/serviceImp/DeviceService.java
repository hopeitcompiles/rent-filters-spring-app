package com.cake.rent.serviceImp;

import com.cake.rent.model.Device;
import com.cake.rent.repository.IDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class DeviceService extends GenericService<Device, Long>{

    @Autowired
    private IDeviceRepository repository;
    @Override
    public JpaRepository<Device, Long> getDao() {
        return repository;
    }

    public Device findByName(String name) {
        return repository.findByName(name);
    }

}
