package com.ddbb.dingdong.domain.reservation.service;

import com.ddbb.dingdong.domain.reservation.ReservationEvent;
import com.ddbb.dingdong.domain.reservation.entity.Reservation;
import com.ddbb.dingdong.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher publisher;
    @Transactional
    public void reserve(Long pathId) {
        Long count = reservationRepository.countByPathId(pathId);
        if(count >= 25) return;
        Reservation reservation = new Reservation();
        reservation.setPathId(pathId);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void reserveWithPessimisticLock(Long pathId) {
        int count = reservationRepository.findReservationsWithLock(pathId).size();
        if(count >= 25) return;
        Reservation reservation = new Reservation();
        reservation.setPathId(pathId);
        reservationRepository.save(reservation);
        publisher.publishEvent(new ReservationEvent(1L, count));
    }
}
