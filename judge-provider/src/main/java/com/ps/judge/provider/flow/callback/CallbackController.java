package com.ps.judge.provider.flow.callback;

import com.alibaba.fastjson.JSONObject;
import com.ps.judge.provider.flow.ProcessEngine;
import com.ps.jury.api.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("callback")
@Slf4j
public class CallbackController {
    @Autowired
    ProcessEngine flowEngine;

    @PostMapping("bluRay")
    ApiResponse bluRay(@RequestBody Map<String, Object> bluRayCallbackResult) {
        log.info("收到bluRay回调, result:{}", JSONObject.toJSONString(bluRayCallbackResult));
        try {
            Integer flowInstanceId = Integer.parseInt(bluRayCallbackResult.get("aliasId").toString());
            flowEngine.callback(flowInstanceId, bluRayCallbackResult);
        } catch (Exception e) {
            log.error("bluRay解析错误，无法获取aliasId");
        }

        return ApiResponse.success();
    }
}
