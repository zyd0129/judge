package com.ps.judge.web.service;


import com.ps.judge.web.models.AuditTaskTriggeredRuleBO;

import java.util.List;


public interface AuditTaskTriggeredRuleService {
    List<AuditTaskTriggeredRuleBO> queryByTaskId(int id);
}
