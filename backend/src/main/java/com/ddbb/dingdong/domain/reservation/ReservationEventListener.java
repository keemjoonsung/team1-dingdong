package com.ddbb.dingdong.domain.reservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class ReservationEventListener {
    @TransactionalEventListener
    public void sendReservationNotification(ReservationEvent reservationEvent) {
        log.info("{}가 예약을 완료했습니다. 현재 총 예약자 수 : {}.  현재 쓰레드 : {}", reservationEvent.userId() , reservationEvent.count() , Thread.currentThread().getName());
    }
}
