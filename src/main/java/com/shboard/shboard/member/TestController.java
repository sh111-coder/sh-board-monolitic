package com.shboard.shboard.member;

import com.shboard.shboard.member.application.dto.MemberLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    private final RestTemplate restTemplate;

    public TestController(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @PostMapping("test-session")
    public ResponseEntity<Void> testSession() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        final MemberLoginRequest request = new MemberLoginRequest("seongha1", "password1!");
        final HttpEntity<Object> httpEntity = new HttpEntity<>(request, httpHeaders);
        for (int i = 0; i < 100; i++) {
            restTemplate.exchange("http://localhost:8080/api/members/login", HttpMethod.POST, httpEntity, Void.class);
        }
        return ResponseEntity.ok().build();
    }
}
