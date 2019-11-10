package com.jack.demo.service;

import com.jack.demo.model.Car;
import com.jack.demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public Car create(String name, String plateNumber) {
        return carRepository.save(new Car(name, plateNumber));
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public Car getFrontCar(String name) {
        return carRepository.findByPlateNumber(name);
    }

    public Car getRearCar(String name) {
        return carRepository.findByPlateNumber(name);
    }

    public Car update(String name, String plateNumber) {
        Car c = carRepository.findByPlateNumber(plateNumber);
        c.setName(name);
        return carRepository.save(c);
    }

    public void deleteAllCar() {
        carRepository.deleteAll();
    }

    public void deleteCar(String plateNumber) {
        Car c = carRepository.findByPlateNumber(plateNumber);
        carRepository.delete(c);
    }
}
