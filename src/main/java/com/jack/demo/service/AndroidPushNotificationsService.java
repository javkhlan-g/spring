package com.jack.demo.service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AndroidPushNotificationsService {

    @Value("${app.firebase.server-key}")
    private String FIREBASE_SERVER_KEY; // = "AAAAJ7yP294:APA91bHJONddGZub7YH3yE_Jn6wc8KXb3sxc4H0Yz3RtiRIlXtkyM67kqOLcMZ6HwX5flIusglUbT7HSdO8BdIStu0yoCqBs7uLV94cwCbeNAI6VIHjDvSQghB9g3gJ0eONhVpjc-e45";

    @Value("${app.firebase.api-url}")
    private String FIREBASE_API_URL;

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        /**
         https://fcm.googleapis.com/fcm/send
         Content-Type:application/json
         Authorization:key=FIREBASE_SERVER_KEY*/

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}