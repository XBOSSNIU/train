package com.xboss.train.member.controller;

import com.xboss.train.common.resp.CommonResp;
import com.xboss.train.member.domain.Passenger;
import com.xboss.train.member.req.PassengerSaveReq;
import com.xboss.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody PassengerSaveReq passengerSaveReq){
        Passenger passenger = passengerService.save(passengerSaveReq);
        CommonResp commonResp=new CommonResp();
        commonResp.setContent(passenger);
        return commonResp;
    }
}
