package com.xboss.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xboss.train.common.exception.BusinessException;
import com.xboss.train.common.exception.BusinessExceptionEnum;
import com.xboss.train.member.resp.MemberLoginResp;
import com.xboss.train.common.util.JwtUtil;
import com.xboss.train.common.util.SnowUtil;
import com.xboss.train.member.domain.Member;
import com.xboss.train.member.domain.MemberExample;
import com.xboss.train.member.mapper.MemberMapper;
import com.xboss.train.member.req.MemberLoginReq;
import com.xboss.train.member.req.MemberRegisterReq;
import com.xboss.train.member.req.MemberSendCodeReq;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Resource
    private MemberMapper memberMapper;
    private final static Logger LOG = LoggerFactory.getLogger(MemberService.class);

    public int count(){
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public long register(MemberRegisterReq memberRegisterReq){
        String mobile=memberRegisterReq.getMobile();
        Member member = SelectByMobile(mobile);

        if(ObjectUtil.isNotNull(member)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }
        Member newMember=new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);

        memberMapper.insert(newMember);
        return member.getId();
    }

    public void sendCode(MemberSendCodeReq memberSendCodeReq){
        String mobile=memberSendCodeReq.getMobile();
        Member member = SelectByMobile(mobile);

        //如果手机号未注册
        if(ObjectUtil.isNull(member)){
            Member newMember=new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(newMember);

            LOG.info("手机号未注册，插入一条记录");
        }
        else {
            LOG.info("手机号已存在，不插入记录");
        }

        //生成短信验证码
        String code= "8888";
        LOG.info("生成短信验证码：{}", code);

        // 保存短信记录表：手机号，短信验证码，有效期，是否已使用，业务类型，发送时间，使用时间
        LOG.info("保存短信记录表");

        // 对接短信通道，发送短信
        LOG.info("对接短信通道");

    }

    private Member SelectByMobile(String mobile) {
        MemberExample memberExample=new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        if(list.isEmpty()){
            return null;
        }
        else {
            return list.get(0);
        }
    }

    public MemberLoginResp login(MemberLoginReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();
        Member memberDB = SelectByMobile(mobile);

        // 如果手机号不存在，则插入一条记录
        if (ObjectUtil.isNull(memberDB)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        // 校验短信验证码
        if (!"8888".equals(code)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }

        MemberLoginResp memberLoginResp = BeanUtil.copyProperties(memberDB, MemberLoginResp.class);
        String token = JwtUtil.createToken(memberLoginResp.getId(), memberLoginResp.getMobile());
        memberLoginResp.setToken(token);
        return memberLoginResp;
    }

}
