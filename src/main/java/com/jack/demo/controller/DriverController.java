package com.jack.demo.controller;

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

    @PostMapping("/create")
    public String create(@RequestBody Driver d) {
        Driver driver = driverService.create(d.getName(), d.getLicenseNumber(), d.getCars());
        return driver.toString();
    }

    @GetMapping("/list")
    public List<Driver> getCars() {
        return driverService.getAll();
    }

    @PostMapping("/reset")
    public void reset() {
        driverService.deleteAllDriver();
    }
}
