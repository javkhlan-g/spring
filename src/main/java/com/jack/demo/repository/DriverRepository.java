package com.jack.demo.repository;

import com.jack.demo.model.Car;
import com.jack.demo.model.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DriverRepository extends MongoRepository<Driver, String> {
    Driver findByLicenseNumber(String s);
    List<Car> findAllBy();
    List<Driver> findByContactAddress(String s);
}
