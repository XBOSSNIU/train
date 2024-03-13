package com.xboss.train.member.controller;

import com.xboss.train.common.resp.CommonResp;
import com.xboss.train.member.req.MemberSendCodeReq;
import com.xboss.train.member.req.MemberLoginReq;
import com.xboss.train.member.req.MemberRegisterReq;
import com.xboss.train.member.resp.MemberLoginResp;
import com.xboss.train.member.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class memberController {
    @Resource
    private MemberService memberService;
    @GetMapping("/count")
    public CommonResp<Integer> count(){
        int count = memberService.count();
        CommonResp<Integer> commonResp=new CommonResp<>();
        commonResp.setContent(count);
        return commonResp;
    }

    @PostMapping("register")
    public CommonResp<Long> register(@Valid MemberRegisterReq memberRegisterReq){
        long register = memberService.register(memberRegisterReq);
        CommonResp<Long> commonResp=new CommonResp<Long>();
        commonResp.setContent(register);

        return commonResp;
    }

    @PostMapping("sendCode")
    public CommonResp sendCode(@Valid @RequestBody MemberSendCodeReq memberSendCodeReq){
        memberService.sendCode(memberSendCodeReq);
        return new CommonResp<>();
    }
    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid @RequestBody MemberLoginReq req) {
        MemberLoginResp resp = memberService.login(req);
        return new CommonResp<>(resp);
    }

}
