package com.jack.demo.controller;

import com.jack.demo.model.Car;
import com.jack.demo.model.Driver;
import com.jack.demo.service.AndroidPushNotificationsService;
import com.jack.demo.service.DriverService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@RequestMapping("/api/v1/driver")
@RestController
public class DriverController {
    @Autowired
    private DriverService driverService;
    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;


    public static final String FRONT_KEY = ".*front.*";
    public static final String REAR_KEY = ".*back.*";
    public static final String CONTENT_TYPE = ".*video.*";


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
        String notificationBody = "";
        String contentType = "text";
        String content = "";
        if (matches) {
            currentIndex--;
            d = driverService.findCarByIndex(currentIndex.toString());
            if (d == null) {
                //return "Sorry, Car not found in front of you";
                throw new RuntimeException("Sorry, Car not found in front of you");
            }

            boolean hasVideo = Pattern.matches(CONTENT_TYPE, msg);

            if (hasVideo) {
                notificationBody = "You have a video from the BACK car #" + plateNumber;
                contentType = "video";
                content = d.getCar().getVideo();
            } else {
                notificationBody = "You have a text from the BACK car #" + plateNumber;
                content = naturalLanguageProcess(msg);
            }

            msg = sendFcm(content, notificationBody, contentType, d.getContact().getDevice());

        } else {
            matches = Pattern.matches(REAR_KEY, msg);
            if (matches) {
                currentIndex++;
                d = driverService.findCarByIndex(currentIndex.toString());
                if (d == null) {
                    //return "Sorry, Car not found in front of you";
                    throw new RuntimeException("Sorry, Car not found in behind of you");
                }

                boolean hasVideo = Pattern.matches(CONTENT_TYPE, msg);

                if (hasVideo) {
                    notificationBody = "You have a video from the FRONT car #" + plateNumber;
                    contentType = "video";
                    content = d.getCar().getVideo();
                } else {
                    notificationBody = "You have a text from the FRONT car #" + plateNumber;
                    content = naturalLanguageProcess(msg);
                }

                msg = sendFcm(content, notificationBody, contentType, d.getContact().getDevice());
            } else {
                throw new RuntimeException("Sorry, We didn't completely understand your expression. Please say it again more clearly!");
                //return "Sorry, We didn't completely understand your expression. Please say it again more clearly!";
            }
        }

        return "We have sent the notification";
    }


    private String naturalLanguageProcess(String msg) {
        if (Pattern.matches(".*fast.*", msg)) return "GO FASTER!!";
        if (Pattern.matches(".*slow.*", msg)) return "GO SLOWER!!";
        throw new RuntimeException("Sorry, We didn't completely understand your expression. Please say it again more clearly!");
        //return "";
    }

    private String sendFcm(String content, String msg, String contentType, String token) {


        JSONObject body = new JSONObject();
        body.put("to", token);
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", "CarBot");
        notification.put("body", msg);

        JSONObject data = new JSONObject();
        data.put("content", content);
        data.put("contentType", contentType);
        body.put("notification", notification);
        body.put("data", data);

        System.out.println(body.toString());

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return firebaseResponse;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "sent";
    }


}
