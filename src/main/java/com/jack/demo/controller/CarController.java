package com.jack.demo.controller;

import com.jack.demo.model.Car;
import com.jack.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/car")
@RestController
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/list")
    public List<Car> getCars() {
        return carService.getAll();
    }

    @PostMapping("/reset")
    public void reset() {
        carService.deleteAllCar();
    }


}
