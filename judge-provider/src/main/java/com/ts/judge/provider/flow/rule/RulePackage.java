package com.ts.judge.provider.flow.rule;

import lombok.Data;

import java.util.List;

@Data
public class RulePackage {
   private Integer id;
   private String name;
   private List<Rule> ruleList;
}
