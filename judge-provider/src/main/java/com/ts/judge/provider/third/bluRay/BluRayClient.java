package com.ts.judge.provider.third.bluRay;

import com.alibaba.fastjson.JSONObject;
import com.ts.clerk.common.response.ApiResponse;
import com.ts.panama.sdk.client.OpenClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class BluRayClient {
    static String  url = "http://10.0.53.84:9011/credit/bluray/score/generate";
    String callback = "http://10.0.53.75:8081/judge/bluray/result";
    /**商户账号*/
    static String appId = "cashloan";
    /** 商户私钥 */
    static String privateKey = "VtzFZ42LPy";


    public ApiResponse send(BluRayParams bluRayParams) {
        bluRayParams.setCallUrl(callback);
        OpenClient openClient = new OpenClient(appId,privateKey);
        String s = JSONObject.toJSONString(bluRayParams);
        Map map = JSONObject.parseObject(s, Map.class);
        try {
            String result = openClient.invoke(url, map);
            log.info("taskId{}调用蓝光结果{}", bluRayParams.getAliasId(), result);
            return JSONObject.parseObject(result, ApiResponse.class);
        }catch (Exception e){
            log.info("taskId{}蓝光服务器失败{}", bluRayParams.getAliasId());
            return ApiResponse.error(500, "蓝光连接错误");
        }
    }
}
