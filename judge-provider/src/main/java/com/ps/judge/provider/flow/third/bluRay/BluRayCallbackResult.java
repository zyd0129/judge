package com.ps.judge.provider.flow.third.bluRay;

import lombok.Data;

/**
 * {
 *     "code": 0,
 *     "msg": "成功",
 *     "httpCode": 0,
 *     "data": {
 *         "status": "true",
 *         "type": "IndiaFirstLoan",
 *         "aliasId": "786470",
 *         "data": "745.0",
 *         "extend": "{\"redisKeyBlacklist\":\"blacklist:Shivashankara:+919110650891:271075336622\",\"redisKeyJson\":\"json:Shivashankara:+919110650891:271075336622\",\"phone\":\"+919110650891\",\"fullName\":\"Shivashankara\",\"redisKeyPdf\":\"pdf:Shivashankara:+919110650891:271075336622\",\"identifyInfo\":\"271075336622\"}"
 *     }
 * }
 */

@Data
public class BluRayCallbackResult {
    private String status;
    private String type;
    private String aliasId;
    private String data;
    private String extend;
}
