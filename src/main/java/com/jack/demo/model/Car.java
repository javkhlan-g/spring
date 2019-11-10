package com.jack.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Car {
    @Id
    public String id;
    public String name;
    public String plateNumber;

    public Car(String name, String plateNumber) {
        this.name = name;
        this.plateNumber = plateNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Override
    public String toString() {
        return String.format(
                "Car [name=%s, number=%s]", name, plateNumber
        );
    }
}
