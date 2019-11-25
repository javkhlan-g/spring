package com.jack.demo.controller;

import com.jack.demo.model.Car;
import com.jack.demo.model.Driver;
import com.jack.demo.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RequestMapping("/api/v1/driver")
@RestController
public class DriverController {
    @Autowired
    private DriverService driverService;

    public static final String FRONT_KEY = ".*urd.*";
    public static final String REAR_KEY = ".*hoino.*";


    @PostMapping("/create")
    public String create(@RequestBody Driver d) {
        Driver driver = driverService.create(d.getName(), d.getLicenseNumber(), d.getCar(), d.getContact());
        return driver.toString();
    }

    @GetMapping("/list")
    public List<Driver> getAll() {
        return driverService.getAll();
    }

    @GetMapping("/cars")
    public List<Car> getCars() {
        return driverService.getCars();
    }

    @PostMapping("/reset")
    public void reset() {
        driverService.deleteAllDriver();
    }

    @GetMapping("/contact/{address}")
    public List<Driver> findByAddress(@PathVariable("address") String name) {
        return driverService.findByAddress(name);
    }

    @GetMapping("/listen/{plateNumber}/{msg}")
    public String listen(@PathVariable("plateNumber") String plateNumber, @PathVariable("msg") String msg) {

        Driver d = driverService.findByCarPlate(plateNumber);
        if (d == null) {
            return "Sorry,the requested driver is not found";
        }

        boolean matches = Pattern.matches(FRONT_KEY, msg);

        Integer currentIndex = Integer.parseInt(d.getCar().getIndex());

        if (matches) {
            currentIndex--;
            d = driverService.findCarByIndex(currentIndex.toString());
            if (d == null) {
                return "Sorry, Car not found in front of you";
            }
            sendFcm(d);
        } else {
            matches = Pattern.matches(REAR_KEY, msg);
            if (matches) {
                currentIndex++;
                d = driverService.findCarByIndex(currentIndex.toString());
                if (d == null) {
                    return "Sorry, Car not found in front of you";
                }
                sendFcm(d);
            } else {
                return "Sorry, We didn't completely understand your expression. Please say it again more clearly!";
            }
        }

        return d.toString();
    }

    private void sendFcm(Driver d) {

    }
}
