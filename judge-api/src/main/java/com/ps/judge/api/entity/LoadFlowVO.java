package com.ps.judge.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadFlowVO {
    @NotBlank(message = "flowCode 不能为空")
    String flowCode;
    boolean load;
}
