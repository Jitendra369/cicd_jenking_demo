package com.multiserver.multiserver1.controller;

import com.multiserver.multiserver1.dto.JsonUser;
import com.multiserver.multiserver1.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/core")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    public static final String USER_SERVER_URL = "http://localhost:8081/api/core/produce/user";
    public static final String ONLINE_USER_URL = "https://jsonplaceholder.typicode.com/posts/1";

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/user")
    public JsonUser getUserObject() {
        ResponseEntity<JsonUser> responseEntity = restTemplate.getForEntity(ONLINE_USER_URL, JsonUser.class);
        HttpHeaders headers = responseEntity.getHeaders();
        JsonUser jsonUser = responseEntity.getBody();
        return responseEntity.getBody();
//        return responseEntity;
    }

    @GetMapping("/local/user")
    public UserDto getUserObjectFromLocalServer() {
        try {
            ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity(USER_SERVER_URL, UserDto.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                log.error("Not able to get user from local server");
            }
        }catch (HttpClientErrorException httpClientErrorException){
            if (httpClientErrorException.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("404 not found");
            }
            if (httpClientErrorException.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                log.error("internal server error");
            }
        }
        catch (Exception e) {
            log.error("Exception while connecting to server  " + e.getMessage());
        }
        return null;
    }

    @GetMapping("/webflux")
    public void apiCallUsingWebFlux(){
    }

}
