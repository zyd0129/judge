package com.ts.judge.provider.controller;

import com.ts.clerk.common.response.ApiResponse;
import com.ts.judge.provider.flow.script.ScriptCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("admin")
@RestController
public class SystemController {

    @Autowired
    ScriptCache scriptCache;

    @PostMapping("/process/script/clear")
    public ApiResponse clear() {
        scriptCache.clear();
        return ApiResponse.success();
    }

}
