package com.xboss.train.member.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import com.xboss.train.common.context.LoginMemberContext;
import com.xboss.train.member.domain.Passenger;
import com.xboss.train.member.mapper.PassengerMapper;
import com.xboss.train.member.req.PassengerSaveReq;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {
    @Resource
    private PassengerMapper passengerMapper;

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
}
