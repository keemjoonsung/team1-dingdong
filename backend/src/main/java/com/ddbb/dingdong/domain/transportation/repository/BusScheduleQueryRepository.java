package com.ddbb.dingdong.domain.transportation.repository;

import com.ddbb.dingdong.domain.transportation.entity.BusSchedule;
import com.ddbb.dingdong.domain.transportation.repository.projection.ScheduleTimeProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusScheduleQueryRepository extends JpaRepository<BusSchedule, Long> {
    @Query("SELECT DISTINCT bs.arrivalTime as time FROM BusSchedule bs WHERE bs.schoolId = :schoolId and bs.status = 'READY'")
    List<ScheduleTimeProjection> findAvailableGoSchoolBusTime(@Param("schoolId") Long schoolId);

    @Query("SELECT DISTINCT bs.departureTime as time FROM BusSchedule bs WHERE bs.schoolId = :schoolId and bs.status = 'READY'")
    List<ScheduleTimeProjection> findAvailableGoHomeBusTime(@Param("schoolId") Long schoolId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b.count FROM BusSchedule b WHERE b.id = :id")
    Optional<Integer> findBusScheduleByIdForUpdate(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BusSchedule b WHERE b.id = :id")
    Optional<BusSchedule> findByIdForUpdate(@Param("id") Long id);
}
