package com.ddbb.dingdong.domain.reservation;

import com.ddbb.dingdong.domain.reservation.entity.Reservation;
import com.ddbb.dingdong.domain.reservation.repository.ReservationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ReservationQueueManager {
    private final Map<Long, ConcurrentLinkedQueue<Reservation>> reservationQ = new HashMap<>();
    private final ReservationRepository reservationRepository;

    public ReservationQueueManager(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    //예매하기 버튼을 누르면 버스 대기열에
    public boolean addReservation(Long busId, Reservation reservation) {
        ConcurrentLinkedQueue<Reservation> queue = reservationQ.get(busId);
        // queue 가 없으면 해당 버스 대기열이 없다는 뜻
        if(queue == null) return false;
        // 중복 reservation 이 있는지 application 단에서 검증 (db에 집어넣을때도 검증 거침 unique constraints 로)
        if(queue.stream().anyMatch(res -> res.getUserId().equals(reservation.getUserId()))) return false;
        // 대기열에 Reservation 추가
        queue.add(reservation);
        return true;
    }

    // 노선이 생성되면 Event 를 발생 시켜 무조건 createBusQueue 를 생성해 주어야 한다.
    @EventListener
    public boolean createBusQueue(Long busId) {
        if(reservationQ.containsKey(busId)) return false;
        ConcurrentLinkedQueue<Reservation> newQ = new ConcurrentLinkedQueue<>();
        reservationQ.put(busId, newQ);

        return true;
    }

    // 운행이 시작되면 Event 를 발생 시켜 무조건 removeBusQueue 를 해줘야 한다.
    @EventListener
    public boolean removeBusQueue(Long busId) {
        if(!reservationQ.containsKey(busId)) return false;
        reservationQ.remove(busId);

        return true;
    }
}
