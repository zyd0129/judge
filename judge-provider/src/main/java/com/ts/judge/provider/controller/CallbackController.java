package com.ts.judge.provider.controller;

import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.flow.ProcessEngine;
import com.ts.clerk.common.response.ApiResponse;
import com.ts.jury.api.request.ApplyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("callback")
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

    @PostMapping("jury")
    ApiResponse jury(@RequestBody ApplyRequest applyRequest) {
        log.info("收到bluRay回调, result:{}", JSONObject.toJSONString(applyRequest));
        return ApiResponse.success();
    }

    @PostMapping("txScore")
    ApiResponse jury(@RequestBody String text) {
        log.info("收到txScore请求, result:{}", text);
        return ApiResponse.success();
    }
}
