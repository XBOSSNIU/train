package com.xboss.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xboss.train.common.context.LoginMemberContext;
import com.xboss.train.common.resp.PageResp;
import com.xboss.train.common.util.SnowUtil;
import com.xboss.train.member.domain.Member;
import com.xboss.train.member.domain.MemberExample;
import com.xboss.train.member.domain.Passenger;
import com.xboss.train.member.domain.PassengerExample;
import com.xboss.train.member.enums.PassengerTypeEnum;
import com.xboss.train.member.mapper.MemberMapper;
import com.xboss.train.member.mapper.PassengerMapper;
import com.xboss.train.member.req.PassengerQueryReq;
import com.xboss.train.member.req.PassengerSaveReq;
import com.xboss.train.member.resp.PassengerQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PassengerService {
    private static final Logger LOG = LoggerFactory.getLogger(PassengerService.class);
    @Resource
    private PassengerMapper passengerMapper;

    @Resource
    private MemberMapper memberMapper;

    public Passenger save(PassengerSaveReq passengerSaveReq){
        Passenger passenger=new Passenger();
        BeanUtils.copyProperties(passengerSaveReq,passenger);
        if(passenger.getId()==null) {
            passenger.setMemberId(LoginMemberContext.getId());
            passenger.setId(IdUtil.getSnowflakeNextId());
            passenger.setCreateTime(DateTime.now());
            passengerMapper.insert(passenger);
        }
        else{
            passenger.setUpdateTime(DateTime.now());
            passengerMapper.updateByPrimaryKey(passenger);
        }
        return passenger;
    }

    public PageResp<PassengerQueryResp> queryList(PassengerQueryReq passengerQueryReq){
        PassengerExample example=new PassengerExample();
        example.setOrderByClause("id desc");
        if (ObjectUtil.isNotNull(passengerQueryReq.getMemberId())) {
            example.createCriteria().andMemberIdEqualTo(passengerQueryReq.getMemberId());
        }
        LOG.info("查询页码：{}", passengerQueryReq.getPage());
        LOG.info("每页条数：{}", passengerQueryReq.getSize());
        PageHelper.startPage(passengerQueryReq.getPage(), passengerQueryReq.getSize());
        List<Passenger> passengerList = passengerMapper.selectByExample(example);

        PageInfo<Passenger> pageInfo = new PageInfo<>(passengerList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<PassengerQueryResp> list = BeanUtil.copyToList(passengerList, PassengerQueryResp.class);

        PageResp<PassengerQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        passengerMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询我的所有乘客
     */
    public List<PassengerQueryResp> queryMine() {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("name asc");
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        criteria.andMemberIdEqualTo(LoginMemberContext.getId());
        List<Passenger> list = passengerMapper.selectByExample(passengerExample);
        return BeanUtil.copyToList(list, PassengerQueryResp.class);
    }

    /**
     * 初始化乘客，如果没有张三，就增加乘客张三，李四、王五同理，防止线上体验时乘客被删光
     */
    public void init() {
        DateTime now = DateTime.now();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo("13000000000");
        List<Member> memberList = memberMapper.selectByExample(memberExample);
        Member member = memberList.get(0);

        List<Passenger> passengerList = new ArrayList<>();

        List<String> nameList = Arrays.asList("张三", "李四", "王五");
        for (String s : nameList) {
            Passenger passenger = new Passenger();
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setMemberId(member.getId());
            passenger.setName(s);
            passenger.setIdCard("123456789123456789");
            passenger.setType(PassengerTypeEnum.ADULT.getCode());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            passengerList.add(passenger);
        }

        for (Passenger passenger : passengerList) {
            PassengerExample passengerExample = new PassengerExample();
            passengerExample.createCriteria().andNameEqualTo(passenger.getName());
            long l = passengerMapper.countByExample(passengerExample);
            if (l == 0) {
                passengerMapper.insert(passenger);
            }
        }
    }
}
