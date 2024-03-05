package com.xboss.train.member.controller;

import com.xboss.train.common.resp.CommonResp;
import com.xboss.train.member.req.MemberRegisterReq;
import com.xboss.train.member.service.MemberService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CommonResp<Long> register(MemberRegisterReq memberRegisterReq){
        long register = memberService.register(memberRegisterReq);
        CommonResp<Long> commonResp=new CommonResp<Long>();
        commonResp.setContent(register);

        return commonResp;
    }
}
