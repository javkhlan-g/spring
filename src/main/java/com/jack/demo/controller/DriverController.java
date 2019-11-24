package com.jack.demo.controller;

import com.jack.demo.model.Car;
import com.jack.demo.model.Driver;
import com.jack.demo.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/driver")
@RestController
public class DriverController {
    @Autowired
    private DriverService driverService;

    public static final String FRONT_KEY = "urd";
    public static final String REAR_KEY = "hoino";


    @PostMapping("/create")
    public String create(@RequestBody Driver d) {
        Driver driver = driverService.create(d.getName(), d.getLicenseNumber(), d.getCars(), d.getContact());
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

    @GetMapping("/listen/{msg}")
    public List<Driver> listen(@PathVariable("msg") String msg) {

        return driverService.findByAddress(msg);
    }
}
