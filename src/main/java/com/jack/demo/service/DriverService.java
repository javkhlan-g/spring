package com.jack.demo.service;

import com.jack.demo.model.Car;
import com.jack.demo.model.Driver;
import com.jack.demo.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    public Driver create(String name, String licenseNumber, List<Car> cars) {
        return driverRepository.save(new Driver(name, licenseNumber, cars));
    }

    public List<Driver> getAll() {
        return driverRepository.findAll();
    }

    public Driver update(String name, String licenseNumber) {
        Driver c = driverRepository.findByLicenseNumber(licenseNumber);
        c.setName(name);
        return driverRepository.save(c);
    }

    public void deleteAllDriver() {
        driverRepository.deleteAll();
    }

    public void deleteDriver(String licenseNumber) {
        /*Driver c = driverRepository.findByLicenseNumber(licenseNumber);
        driverRepository.delete(c);*/
    }
}
