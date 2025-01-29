package com.ddbb.dingdong;

import com.ddbb.dingdong.domain.reservation.repository.ReservationRepository;
import com.ddbb.dingdong.domain.reservation.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
public class ReserveTest {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationService reservationService;

    @BeforeEach
    void DB삭제() {
            reservationRepository.deleteAll();
    }
    @Test
    @DisplayName("멀티 스레드 환경에서 100명 예약 테스트")
    void 예약_테스트_100명() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0 ; i < threadCount ; i++) {
            executorService.submit(() -> {
                try {
                    reservationService.reserve(1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Long count = reservationRepository.countByPathId(1L);
        Assertions.assertThat(count).isEqualTo(25);
    }

    @Test
    @DisplayName("멀티 스레드 환경에서 300명 예약 테스트 비관적 락 적용")
    void 예약_테스트_비관적락_1000명() throws InterruptedException {
        int threadCount = 700;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0 ; i < threadCount ; i++) {
            executorService.submit(() -> {
                try {
                    reservationService.reserveWithPessimisticLock(1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Long count = reservationRepository.countByPathId(1L);
        Assertions.assertThat(count).isEqualTo(25);
    }
}
