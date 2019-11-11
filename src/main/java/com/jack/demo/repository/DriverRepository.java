package com.jack.demo.repository;

import com.jack.demo.model.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DriverRepository extends MongoRepository<Driver, String> {
    Driver findByLicenseNumber(String s);
}
