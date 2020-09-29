package com.ts.judge.provider.flow.rule.builder;

import com.ts.judge.provider.flow.rule.model.ConditionRelationEnum;
import com.ts.judge.provider.flow.rule.model.Condition;
import com.ts.judge.provider.flow.rule.model.Rule;

import java.util.List;

/**
 * 功能描述
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/16
 */
public class DroolsRuleTemplate extends RuleTemplate {
    private static final String IMPORTS = "import java.util.Map;" + LINE_SEPARATOR
            + "import java.util.ArrayList;" + LINE_SEPARATOR
            + "import com.ps.judge.dao.entity.AuditTaskTriggeredRuleDO;" + LINE_SEPARATOR
            + "import com.ps.judge.provider.flow.rule.model.HitRule;" + LINE_SEPARATOR;

    @Override
    String buildImports() {
        return IMPORTS;
    }

    @Override
    String buildAttributes(Rule rule) {
        StringBuilder attributesStr = new StringBuilder();
        attributesStr.append(LINE_SEPARATOR);
        attributesStr.append("rule \"").append(rule.getRuleCode()).append("\"").append(LINE_SEPARATOR);
        attributesStr.append("dialect \"java\"").append(LINE_SEPARATOR);
        attributesStr.append("agenda-group \"").append(rule.getAgendaGroup()).append("\"").append(LINE_SEPARATOR);
        attributesStr.append("ruleflow-group \"").append(rule.getRuleFlowGroup()).append("\"").append(LINE_SEPARATOR);
        attributesStr.append("salience ").append(rule.getSalience()).append(LINE_SEPARATOR);
        return new String(attributesStr);
    }

    @Override
    String buildLHS(List<Condition> conditionList) {
        StringBuilder lhsStr = new StringBuilder();
        lhsStr.append("when").append(LINE_SEPARATOR);
        lhsStr.append("$arrayList : ArrayList( )").append(LINE_SEPARATOR);
        lhsStr.append("$map : Map(");
        int index = 0;
        for (Condition condition : conditionList) {
            index++;
            lhsStr.append(this.buildCondition(index, condition));
        }

        //除去最后一个多余的逻辑关系符合
        Condition lastCondition = conditionList.get(conditionList.size() - 1);
        if (lastCondition.getRelation() == ConditionRelationEnum.AND.code()) {
            lhsStr.deleteCharAt(lhsStr.length() - ConditionRelationEnum.AND.value().length());
        }  else {
            lhsStr.deleteCharAt(lhsStr.length() - ConditionRelationEnum.OR.value().length());
        }

        lhsStr.append(")").append(LINE_SEPARATOR);
        return new String(lhsStr);
    }

    @Override
    String buildRHS(Rule rule) {
        StringBuilder rhsStr = new StringBuilder();
        rhsStr.append("then").append(LINE_SEPARATOR);

        rhsStr.append("HitRule hitRule = new HitRule();").append(LINE_SEPARATOR);
        rhsStr.append("hitRule.setRuleCode(\"").append(rule.getRuleCode()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("hitRule.setRuleName(\"").append(rule.getRuleName()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("hitRule.setRuleVersion(\"").append(rule.getRuleVersion()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("hitRule.setRulePackageCode(\"").append(rule.getRuleFlowGroup()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("hitRule.setRulePackageName(\"").append(rule.getRulePackageName()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("hitRule.setRulePackageVersion(\"").append(rule.getRulePackageVersion()).append("\");").append(LINE_SEPARATOR);

        StringBuilder expression = new StringBuilder();
        StringBuilder conditionValue = new StringBuilder();
        StringBuilder param = new StringBuilder();
        int index = 0;
        for (Condition condition : rule.getConditionList()) {
            index++;
            if (index != rule.getConditionList().size()) {
                expression.append(condition.getOperator()).append("|");
                conditionValue.append(condition.getOperand()).append("|");
                param.append("$param").append(index).append("+").append("\"|\"+");
            } else {
                expression.append(condition.getOperator());
                conditionValue.append(condition.getOperand());
                param.append("$param").append(index).append("+").append("\"\"");
            }
        }

        rhsStr.append("hitRule.setExpression(\"").append(expression).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("hitRule.setCondition(\"").append(conditionValue).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("hitRule.setParam(").append(param).append(");").append(LINE_SEPARATOR);

        rhsStr.append("hitRule.setResult(\"").append(rule.getResult()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("hitRule.setScore(").append(rule.getScore()).append(");").append(LINE_SEPARATOR);
        rhsStr.append("$arrayList.add(hitRule);").append(LINE_SEPARATOR);
        rhsStr.append("end").append(LINE_SEPARATOR);
        return new String(rhsStr);
    }

    private String buildCondition(int index, Condition condition) {
        StringBuilder conditionalStr = new StringBuilder();
        conditionalStr.append(" $param").append(index).append(" : ");
        conditionalStr.append("this.get(\"").append(condition.getVariableCode()).append("\") ");
        conditionalStr.append(condition.getOperator()).append(" ");
        conditionalStr.append(condition.getOperand()).append(" ");
        if (condition.getRelation() == ConditionRelationEnum.AND.code()) {
            conditionalStr.append(ConditionRelationEnum.AND.value());
        }  else {
            conditionalStr.append(ConditionRelationEnum.OR.value());
        }
        return new String(conditionalStr);
    }
}
