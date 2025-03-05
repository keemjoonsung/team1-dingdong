package com.ddbb.dingdong.application.usecase.bus;

import com.ddbb.dingdong.application.common.Params;
import com.ddbb.dingdong.application.common.UseCase;
import com.ddbb.dingdong.domain.transportation.repository.PathQueryRepository;
import com.ddbb.dingdong.domain.transportation.repository.projection.PathPointProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPathPointLine implements UseCase<GetPathPointLine.Param, GetPathPointLine.Response> {
    private final PathQueryRepository pathQueryRepository;

    @Override
    @Cacheable(value = "pathPoints", key = "#param.busScheduleId")
    public Response execute(Param param) {
        long start = System.currentTimeMillis();
        List<PathPointProjection> projections = pathQueryRepository.findPathPointsByPathId(param.busScheduleId);
        long end = System.currentTimeMillis();
        log.info("✅ [DB 조회 실행 시간] : {} ms", (end - start));
        List<Response.Point> points = projections.stream()
                .map(proj -> new Response.Point(proj.getLongitude(), proj.getLatitude()))
                .toList();
        long end2 = System.currentTimeMillis();
        log.info("✅ [Point 변환 실행 시간] : {} ms", (end2 - end));

        return new Response(points);
    }

    @Getter
    @AllArgsConstructor
    public static class Param implements Params {
        private Long busScheduleId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response implements Serializable {
        private List<Point> points;

        @Getter
        @AllArgsConstructor
        public static class Point implements Serializable {
            double longitude;
            double latitude;
        }
    }
}
