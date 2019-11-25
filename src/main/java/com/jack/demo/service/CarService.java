package com.jack.demo.service;

import com.jack.demo.model.Car;
import com.jack.demo.repository.CarRepository;
import com.jack.demo.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;
    private DriverRepository driverRepository;

    public Car create(String index, String name, String plateNumber, String video) {
        return carRepository.save(new Car(index, name, plateNumber, video));
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
        c.setModel(name);
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
