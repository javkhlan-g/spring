package com.jack.demo.repository;

import com.jack.demo.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {
        public Car findByPlateNumber(String number);
        List<Car> getAllBy();
 }
