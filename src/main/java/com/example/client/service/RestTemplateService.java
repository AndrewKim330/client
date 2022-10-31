package com.example.client.service;

import com.example.client.dto.Req;
import com.example.client.dto.UserRequest;
import com.example.client.dto.UserResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class RestTemplateService {

    public UserResponse hello(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/hello")
                .encode()
                .build()
                .toUri();
        System.out.println(uri.toString());

        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(uri, String.class);
//        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        ResponseEntity<UserResponse> result = restTemplate.getForEntity(uri, UserResponse.class);


        System.out.println(result.getStatusCode());
        System.out.println(result.getBody());

        return result.getBody();
    }

    public UserResponse post(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(35, "ruben")
                .toUri();
        System.out.println(uri);

        UserRequest req = new UserRequest();
        req.setName("ruben");
        req.setAge(35);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(uri, req, UserResponse.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());

        return response.getBody();

    }

    public UserResponse exchange() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(35, "ruben")
                .toUri();
        System.out.println(uri);

        UserRequest req = new UserRequest();
        req.setName("ruben");
        req.setAge(35);

        RequestEntity<UserRequest> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization", "abcd")
                .header("custom-header", "ffff")
                .body(req);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.exchange(requestEntity, UserResponse.class);
        return response.getBody();
    }

    public Req<UserResponse> genericExchange () {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(35, "ruben")
                .toUri();
        System.out.println(uri);

        UserRequest userRequest = new UserRequest();
        userRequest.setName("ruben");
        userRequest.setAge(35);

        Req req = new Req();
        req.setHeader(
                new Req.Header()
        );
        req.setHttpBody(userRequest);

        RequestEntity<Req<UserRequest>> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization", "abcd")
                .header("custom-header", "ffff")
                .body(req);

        new ParameterizedTypeReference<Req<UserResponse>>(){};
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Req<UserResponse>> response = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Req<UserResponse>>(){});
        return response.getBody();
    }
}
