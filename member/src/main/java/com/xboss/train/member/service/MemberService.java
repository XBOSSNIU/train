package com.xboss.train.member.service;

import cn.hutool.core.collection.CollUtil;
import com.xboss.train.common.exception.BusinessException;
import com.xboss.train.common.exception.BusinessExceptionEnum;
import com.xboss.train.member.domain.Member;
import com.xboss.train.member.domain.MemberExample;
import com.xboss.train.member.mapper.MemberMapper;
import com.xboss.train.member.req.MemberRegisterReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Resource
    private MemberMapper memberMapper;

    public int count(){
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public long register(MemberRegisterReq memberRegisterReq){
        String mobile=memberRegisterReq.getMobile();
        MemberExample memberExample=new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        if(CollUtil.isNotEmpty(list)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }
        Member member=new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(mobile);

        memberMapper.insert(member);
        return member.getId();
    }
}
