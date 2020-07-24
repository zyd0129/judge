package com.ps.judge.provider.rule.builder;

import com.ps.judge.provider.rule.model.ConditionRelationEnum;
import com.ps.judge.provider.rule.model.ConditionVO;
import com.ps.judge.provider.rule.model.RuleVO;

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
            + "import com.ps.judge.dao.entity.AuditTaskTriggeredRuleDO;" + LINE_SEPARATOR;

    @Override
    String buildImports() {
        return IMPORTS;
    }

    @Override
    String buildAttributes(RuleVO rule) {
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
    String buildLHS(List<ConditionVO> conditionList) {
        StringBuilder lhsStr = new StringBuilder();
        lhsStr.append("when").append(LINE_SEPARATOR);
        lhsStr.append("$arrayList : ArrayList( )").append(LINE_SEPARATOR);
        lhsStr.append("$map : Map(");
        int index = 0;
        for (ConditionVO condition : conditionList) {
            index++;
            lhsStr.append(this.buildCondition(index, condition));
        }

        //除去最后一个多余的逻辑关系符合
        ConditionVO lastCondition = conditionList.get(conditionList.size() - 1);
        if (lastCondition.getRelation() == ConditionRelationEnum.AND.code()) {
            lhsStr.deleteCharAt(lhsStr.length() - ConditionRelationEnum.AND.value().length());
        }  else {
            lhsStr.deleteCharAt(lhsStr.length() - ConditionRelationEnum.OR.value().length());
        }

        lhsStr.append(")").append(LINE_SEPARATOR);
        return new String(lhsStr);
    }

    @Override
    String buildRHS(RuleVO rule) {
        StringBuilder rhsStr = new StringBuilder();
        rhsStr.append("then").append(LINE_SEPARATOR);
        rhsStr.append("AuditTaskTriggeredRuleDO triggeredRule = new AuditTaskTriggeredRuleDO();").append(LINE_SEPARATOR);
        rhsStr.append("triggeredRule.setRuleCode(\"").append(rule.getRuleCode()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("triggeredRule.setRuleName(\"").append(rule.getRuleName()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("triggeredRule.setRuleVersion(\"").append(rule.getRuleVersion()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("triggeredRule.setRulePackageCode(\"").append(rule.getRuleFlowGroup()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("triggeredRule.setRulePackageName(\"").append(rule.getRulePackageName()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("triggeredRule.setRulePackageVersion(\"").append(rule.getRulePackageVersion()).append("\");").append(LINE_SEPARATOR);

        StringBuilder expression = new StringBuilder();
        StringBuilder conditionValue = new StringBuilder();
        StringBuilder param = new StringBuilder();
        int index = 0;
        for (ConditionVO condition : rule.getConditionList()) {
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

        rhsStr.append("triggeredRule.setExpression(\"").append(expression).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("triggeredRule.setCondition(\"").append(conditionValue).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("triggeredRule.setParam(").append(param).append(");").append(LINE_SEPARATOR);
        rhsStr.append("triggeredRule.setResult(\"").append(rule.getResult()).append("\");").append(LINE_SEPARATOR);
        rhsStr.append("triggeredRule.setScore(").append(rule.getScore()).append(");").append(LINE_SEPARATOR);
        rhsStr.append("$arrayList.add(triggeredRule);").append(LINE_SEPARATOR);
        rhsStr.append("end").append(LINE_SEPARATOR);
        return new String(rhsStr);
    }

    private String buildCondition(int index, ConditionVO condition) {
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
