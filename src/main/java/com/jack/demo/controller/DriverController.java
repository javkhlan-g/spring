package com.jack.demo.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
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
            //return "Sorry,the requested driver is not found";
            throw new RuntimeException("Sorry,the requested driver is not found");
        }

        boolean matches = Pattern.matches(FRONT_KEY, msg);

        Integer currentIndex = Integer.parseInt(d.getCar().getIndex());

        if (matches) {
            currentIndex--;
            d = driverService.findCarByIndex(currentIndex.toString());
            if (d == null) {
                //return "Sorry, Car not found in front of you";
                throw new RuntimeException("Sorry, Car not found in front of you");
            }
            sendFcm(d);
        } else {
            matches = Pattern.matches(REAR_KEY, msg);
            if (matches) {
                currentIndex++;
                d = driverService.findCarByIndex(currentIndex.toString());
                if (d == null) {
                    //return "Sorry, Car not found in front of you";
                    throw new RuntimeException("Sorry, Car not found in behind of you");
                }
                sendFcm(d);
            } else {
                throw new RuntimeException("Sorry, We didn't completely understand your expression. Please say it again more clearly!");
                //return "Sorry, We didn't completely understand your expression. Please say it again more clearly!";
            }
        }

        return "We have sent the notification";
    }

    private void sendFcm(Driver d) {
        String registrationToken = "YOUR_REGISTRATION_TOKEN";

// See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(registrationToken)
                .build();

// Send a message to the device corresponding to the provided
// registration token.
        String response = FirebaseMessaging.getInstance().send(message);
// Response is a message ID string.
        System.out.println("Successfully sent message: " + response);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.newBuilder().build().getApplicationDefault())
                .setDatabaseUrl("https://kr-car.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
    }


}
