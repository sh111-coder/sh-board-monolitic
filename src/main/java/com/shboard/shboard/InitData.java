package com.shboard.shboard;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shboard.shboard.board.domain.Board;
import com.shboard.shboard.member.application.dto.MemberLoginRequest;
import com.shboard.shboard.member.domain.Member;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Profile("session-test")
@Component
@RequiredArgsConstructor
public class InitData {

    private final InitDataService initDataService;

    @PostConstruct
    public void init() {
        initDataService.init();
    }

    @Component
    @RequiredArgsConstructor
    static class InitDataService {
        @PersistenceContext
        private EntityManager em;
        private final StringRedisTemplate redisTemplate;

        @Value("${spring.session.redis.namespace}")
        private String namespace;

        @Transactional
        public void init() {
            int totalPostCount = 100;
            for (int i = 1; i <= totalPostCount; i++) {
                final String loginId = "seongha" + i;
                final String password = "password1!";
                final Member member = com.shboard.shboard.member.domain.Member.builder()
                        .loginId(loginId)
                        .password(password)
                        .nickname("sh" + i)
                        .build();
                em.persist(member);

                final Board board = new Board(em.find(Member.class, i), "title" + i, "content" + i);
                em.persist(board);
            }
        }
    }

}
