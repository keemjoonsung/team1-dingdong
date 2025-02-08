package com.ddbb.dingdong.presentation.endpoint.bus;

import com.ddbb.dingdong.application.exception.APIException;
import com.ddbb.dingdong.application.usecase.bus.GetBusSchedulesUseCase;
import com.ddbb.dingdong.domain.common.exception.DomainException;
import com.ddbb.dingdong.domain.reservation.entity.vo.Direction;
import com.ddbb.dingdong.infrastructure.auth.AuthUser;
import com.ddbb.dingdong.infrastructure.auth.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bus")
@RequiredArgsConstructor
public class BusController {
    private final GetBusSchedulesUseCase getBusSchedulesUseCase;

    @GetMapping("/schedule/time")
    public ResponseEntity<GetBusSchedulesUseCase.Response> getBusSchedules(
            @RequestParam("direction") Direction direction,
            @LoginUser AuthUser authUser
    ) {
        System.out.printf("%s %d", direction, authUser.id());
        try {
            GetBusSchedulesUseCase.Param param = new GetBusSchedulesUseCase.Param(authUser.id(), direction);
            return ResponseEntity.ok(getBusSchedulesUseCase.execute(param));
        } catch (DomainException e) {
            throw new APIException(e, HttpStatus.NOT_FOUND);
        }
    }
}
