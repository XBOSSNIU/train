package com.xboss.train.member.controller;

import com.xboss.train.common.resp.CommonResp;
import com.xboss.train.common.resp.PageResp;
import com.xboss.train.member.domain.Passenger;
import com.xboss.train.member.req.PassengerQueryReq;
import com.xboss.train.member.req.PassengerSaveReq;
import com.xboss.train.member.resp.PassengerQueryResp;
import com.xboss.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("selectByMemberId")
    public CommonResp<PageResp> selectByMemberId(@Valid PassengerQueryReq passengerQueryReq){
        PageResp<PassengerQueryResp> passengerQueryRespPageResp = passengerService.queryList(passengerQueryReq);
        return new CommonResp(passengerQueryRespPageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        passengerService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-mine")
    public CommonResp<List<PassengerQueryResp>> queryMine() {
        List<PassengerQueryResp> list = passengerService.queryMine();
        return new CommonResp<>(list);
    }

    @GetMapping("/init")
    public CommonResp<Object> init() {
        passengerService.init();
        return new CommonResp<>();
    }
}
