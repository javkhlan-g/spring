package com.jack.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Driver {
    @Id
    private String id;
    private String name;
    private String licenseNumber;
    private List<Car> cars;

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public Driver(String name, String licenseNumber, List<Car> cars) {
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.cars = cars;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String toString() {
        return this.getName();
    }
}
