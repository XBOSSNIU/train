package com.xboss.train.batch.feign;

import com.xboss.train.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "member")
public interface MemberFeign {

    @GetMapping("/member/passenger/init")
    CommonResp init();

}
