package com.shboard.shboard;

import com.shboard.shboard.board.domain.Board;
import com.shboard.shboard.global.session.domain.MemberSession;
import com.shboard.shboard.member.domain.Member;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
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
    static class InitDataService {
        @PersistenceContext
        private EntityManager em;

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

                final MemberSession memberSession = new MemberSession("session" + i, loginId);
                em.persist(memberSession);

                final Board board = new Board(em.find(Member.class, i), "title" + i, "content" + i);
                em.persist(board);
            }
        }
    }

}
