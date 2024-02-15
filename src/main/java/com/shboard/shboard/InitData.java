package com.shboard.shboard;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shboard.shboard.board.domain.Board;
import com.shboard.shboard.member.domain.Member;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
                final Member member = com.shboard.shboard.member.domain.Member.builder()
                        .loginId(loginId)
                        .password("password1!")
                        .nickname("sh" + i)
                        .build();
                em.persist(member);

                Map<String, Object> sessionData = new HashMap<>();
                sessionData.put("lastAccessedTime", "1707983611409");
                sessionData.put("maxInactiveInterval", "3600");
                sessionData.put("sessionAttr:memberId", "\"seongha1\"");
                sessionData.put("creationTime", "1707983610661");

                redisTemplate.opsForHash().putAll(namespace + ":sessions:" + "test-session" + i, sessionData);

                final Board board = new Board(em.find(Member.class, i), "title" + i, "content" + i);
                em.persist(board);
            }
        }
    }

}
