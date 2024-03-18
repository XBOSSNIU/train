package com.xboss.train.business.controller.admin;

import com.xboss.train.common.context.LoginMemberContext;
import com.xboss.train.common.resp.CommonResp;
import com.xboss.train.common.resp.PageResp;
import com.xboss.train.business.req.TrainSeatQueryReq;
import com.xboss.train.business.req.TrainSeatSaveReq;
import com.xboss.train.business.resp.TrainSeatQueryResp;
import com.xboss.train.business.service.TrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class TrainSeatAdminController {

    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainSeatSaveReq req) {
        trainSeatService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainSeatQueryResp>> queryList(@Valid TrainSeatQueryReq req) {
        PageResp<TrainSeatQueryResp> list = trainSeatService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        trainSeatService.delete(id);
        return new CommonResp<>();
    }

}
