package com.ts.judge.provider.flow.bpmn;

import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.flow.node.NodeInstance;
import com.ts.judge.provider.flow.node.type.StartNode;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SimpleBPMDefinitionTest {

    @Test
    public void bpmCase() {
        List<NodeInstance> nodeInstanceList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();

        NodeInstance startNode = new NodeInstance("startNode", "startNode", null, null, "1", null);
        NodeInstance juryNode = new NodeInstance("webNode", "jury", null, null, "2", null);
        Map<String, Object> nodeInstanceProperties = new HashMap<>();
        nodeInstanceProperties.put("url", "http://10.0.51.91:9075/api/apply");
        String inputScript = "package com.ts.judge.provider.flow.script;\n" +
                "\n" +
                "import com.ts.judge.provider.flow.ProcessInstance;\n" +
                "\n" +
                "import java.util.Map;\n" +
                "\n" +
                "public class JuryInputScript extends DefaultInputScript implements IInputScript{\n" +
                "    @Override\n" +
                "    public Map<String, Object> parse(ProcessInstance processInstance) {\n" +
                "        Map<String, Object> result = super.parse(processInstance);\n" +
                "        result.put(\"applyRequest\", processInstance.getApplyRequest());\n" +
                "        return  result;\n" +
                "    }\n" +
                "}\n";
        String outputScript = "package com.ts.judge.provider.flow.script;\n" +
                "\n" +
                "import com.alibaba.fastjson.JSONObject;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "\n" +
                "import java.util.HashMap;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "\n" +
                "public class JuryOutputScript implements IOutputScript {\n" +
                "    private final Logger logger = LoggerFactory.getLogger(JuryOutputScript.class);\n" +
                "\n" +
                "    @Override\n" +
                "    public Map<String, Object> process(Map<String, Object> output) {\n" +
                "        logger.info(\"jury结果:{}\", JSONObject.toJSONString(output));\n" +
                "        boolean success = (boolean) output.getOrDefault(\"success\", false);\n" +
                "        if (!success) {\n" +
                "            logger.error(\"jury服务出错\");\n" +
                "            return null;\n" +
                "        }\n" +
                "        List data = (List) output.getOrDefault(\"data\", null);\n" +
                "\n" +
                "        /**\n" +
                "         * 基本类型，restTemplate已经自动解析\n" +
                "         * Object类型，json字符串\n" +
                "         */\n" +
                "\n" +
                "        Map<String, Object> juryVariable = new HashMap<>();\n" +
                "        for (Object variableResult : data) {\n" +
                "            Map<String, Object> variableResultMap = (Map<String, Object>) variableResult;\n" +
                "            String varName = (String) variableResultMap.get(\"varName\");\n" +
                "            Object varValue = variableResultMap.getOrDefault(\"varValue\", null);\n" +
                "            String varType = (String) variableResultMap.get(\"varType\");\n" +
                "            if (varValue == null) {\n" +
                "                varValue = variableResultMap.getOrDefault(\"varDefaultValue\", null);\n" +
                "            }\n" +
                "            if (\"Object\".equals(varType)) {\n" +
                "                varValue = JSONObject.parse(varValue.toString());\n" +
                "            }\n" +
                "            juryVariable.put(varName, varValue);\n" +
                "        }\n" +
                "        return juryVariable;\n" +
                "    }\n" +
                "}\n";
        nodeInstanceProperties.put("inputScript", inputScript);
        nodeInstanceProperties.put("outputScript", outputScript);
        juryNode.setProperties(nodeInstanceProperties);

//        NodeInstance ruleNode = new NodeInstance("ruleNode", "ruleNode", null, null, "3",nodeInstanceProperties);
        NodeInstance txScoreNode = new NodeInstance("webNode", "txScore", null, null, "4",null);

        Map<String, Object> nodeInstanceProperties1 = new HashMap<>();
        nodeInstanceProperties1.put("url", "https://apitest.wificash.in/python/Xg_score");
        String inputS = "package com.ts.judge.provider.flow.script;\n" +
                "\n" +
                "import com.ts.judge.provider.flow.ProcessInstance;\n" +
                "\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "public class TxScoreInputScript implements IInputScript {\n" +
                "    /**\n" +
                "     * {\"education\":\"High school\",\n" +
                "     * \"app_amount\":57,\"sex\":\"Male\",\n" +
                "     * \"working_years\":\"more than 5 years\",\n" +
                "     * \"in_big_city\":0,\"salary\":20000.0,\n" +
                "     * \"occupation_class\":\"Manager\",\"real_contact\":261,\"financial_app\":16,\"register_addr\":\"Unnamed Road, Srikaranpur, Rajasthan 335073, India\",\"regist_time\":\"2020-09-23\",\"date_birthday\":\"1992-05-03\",\"contact\":277,\"income_by\":\"Cash\",\"marry_state\":\"Unmarried\",\"supplement_state\":0}\n" +
                "     * @param processInstance\n" +
                "     * @return\n" +
                "     */\n" +
                "    @Override\n" +
                "    public Map<String, Object> parse(ProcessInstance processInstance) {\n" +
                "        Map<String, Object> modelParams = new HashMap<>();\n" +
                "        Map<String, Object> processParams = processInstance.getProcessParams();\n" +
                "        Map<String, Object> jury = (Map<String, Object>) processParams.get(\"jury\");\n" +
                "        String gender = (String) jury.get(\"gender\");\n" +
                "        modelParams.put(\"sex\", genderConverter(gender));\n" +
                "        String education = (String) jury.get(\"education\");\n" +
                "        modelParams.put(\"education\", educationConverter(education));\n" +
                "        String maritalStatus = (String) jury.get(\"maritalStatus\");\n" +
                "        modelParams.put(\"marry_state\", maritalStatusConverter(maritalStatus));\n" +
                "        modelParams.put(\"occupation_class\", jury.get(\"profession\"));\n" +
                "        modelParams.put(\"salary\", jury.get(\"monthlyIncome\"));\n" +
                "        Double workingYears = (Double) jury.get(\"workingYears\");\n" +
                "        modelParams.put(\"working_years\", workingYears);\n" +
                "        modelParams.put(\"register_addr\", jury.get(\"registeredAddress\"));\n" +
                "        Boolean appliedCityIsOpenCity = (Boolean) jury.get(\"appliedCityIsOpenCity\");\n" +
                "        modelParams.put(\"in_big_city\", appliedCityIsOpenCity ? 1 : 0);\n" +
                "        modelParams.put(\"income_by\", jury.get(\"incomeType\"));\n" +
                "        modelParams.put(\"supplement_state\", 1);\n" +
                "        modelParams.put(\"regist_time\", jury.get(\"registerTime\"));\n" +
                "        modelParams.put(\"date_birthday\", jury.get(\"birthday\"));\n" +
                "        modelParams.put(\"app_amount\", jury.get(\"loanAppCount\"));\n" +
                "        modelParams.put(\"contact\", jury.get(\"contactItemCount\"));\n" +
                "        modelParams.put(\"real_contact\", jury.get(\"contactItemCountDistincted\"));\n" +
                "        modelParams.put(\"financial_app\", jury.get(\"loanAppCount\"));\n" +
                "        return modelParams;\n" +
                "    }\n" +
                "\n" +
                "    private String genderConverter(String gender) {\n" +
                "        String result;\n" +
                "        if (\"male\".equals(gender)) {\n" +
                "            result = \"Male\";\n" +
                "        } else if (\"female\".equals(gender)) {\n" +
                "            result = \"Female\";\n" +
                "        } else {\n" +
                "            result = \"Male\";\n" +
                "        }\n" +
                "        return result;\n" +
                "    }\n" +
                "\n" +
                "    private String educationConverter(String education) {\n" +
                "        String result;\n" +
                "        switch (education) {\n" +
                "            case \"belowHighSchool\":\n" +
                "                result = \"Junior high school or below\";\n" +
                "                break;\n" +
                "            case \"highSchool\":\n" +
                "                result = \"High school\";\n" +
                "                break;\n" +
                "            case \"juniorCollege\":\n" +
                "                result = \"Junior college\";\n" +
                "                break;\n" +
                "            case \"bachelor\":\n" +
                "                result = \"Bachelor degree\";\n" +
                "                break;\n" +
                "            case \"master\":\n" +
                "                result = \"Master degree\";\n" +
                "                break;\n" +
                "            case \"doctor\":\n" +
                "                result = \"Doctoral degree\";\n" +
                "                break;\n" +
                "            case \"postdoc\":\n" +
                "                result = \"Postdoctoral degree\";\n" +
                "                break;\n" +
                "            default:\n" +
                "                result = \"others\";\n" +
                "        }\n" +
                "        return result;\n" +
                "    }\n" +
                "\n" +
                "    private String maritalStatusConverter(String marryStatus) {\n" +
                "        String result;\n" +
                "        if (\"married\".equals(marryStatus)) {\n" +
                "            result = \"Married\";\n" +
                "        } else {\n" +
                "            result = \"Unmarried\";\n" +
                "        }\n" +
                "        return result;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * working_years_dict = {'1 to 2 years': 1, '2 to 3 years': 2, '3 to 4 years': 3, '3 to 6 months': 4,\n" +
                "     * '4 to 5 years': 5, '6 to 12 months': 7, 'more than 5 years': 8, 'Within 3 months': 9}\n" +
                "     *\n" +
                "     * @return\n" +
                "     */\n" +
                "    private String workingYearConverter(Double workingYear) {\n" +
                "        String result;\n" +
                "        if (workingYear >= 5) {\n" +
                "            result = \"more than 5 years\";\n" +
                "        } else if (workingYear >= 4) {\n" +
                "            result = \"4 to 5 years\";\n" +
                "        } else if (workingYear >= 3) {\n" +
                "            result = \"3 to 4 years\";\n" +
                "        } else if (workingYear >= 2) {\n" +
                "            result = \"2 to 3 years\";\n" +
                "        } else if (workingYear >= 1) {\n" +
                "            result = \"1 to 2 years\";\n" +
                "        } else if (workingYear >= 0.6) {\n" +
                "            result = \"6 to 12 months\";\n" +
                "        } else if (workingYear >= 0.3) {\n" +
                "            result = \"3 to 6 months\";\n" +
                "        } else {\n" +
                "            result = \"Within 3 months\";\n" +
                "        }\n" +
                "        return result;\n" +
                "    }\n" +
                "}\n";
        String outputS = "package com.ts.judge.provider.flow.script;\n" +
                "\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "public class TxScoreOutputScript implements IOutputScript {\n" +
                "    @Override\n" +
                "    public Map<String, Object> process(Map<String, Object> output) {\n" +
                "        Integer point = (Integer) output.getOrDefault(\"point\", -99);\n" +
                "        Map<String, Object> processResult = new HashMap<>();\n" +
                "        processResult.put(\"point\", point);\n" +
                "        return processResult;\n" +
                "    }\n" +
                "}\n";
        nodeInstanceProperties1.put("inputScript", inputS);
        nodeInstanceProperties1.put("outputScript", outputS);
        txScoreNode.setProperties(nodeInstanceProperties1);

        NodeInstance endNode = new NodeInstance("endNode", "endNode", null, null, "5", null);

        nodeInstanceList.add(startNode);
        nodeInstanceList.add(juryNode);
//        nodeInstanceList.add(ruleNode);
        nodeInstanceList.add(txScoreNode);
        nodeInstanceList.add(endNode);

        Edge edge1 = new Edge("1", "2", "true");
        Edge edge2 = new Edge("2", "4", "true");
//        Edge edge3 = new Edge("3","4","true");
        Edge edge4 = new Edge("4","5","true");
        edgeList.add(edge1);
        edgeList.add(edge2);
//        edgeList.add(edge3);
        edgeList.add(edge4);
        SimpleBPMDefinition bpm = new SimpleBPMDefinition(nodeInstanceList, edgeList);

        System.out.println(JSONObject.toJSONString(bpm));

    }

    @Test
    public void grooyTest() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("a", "fgg");
        Binding binding = new Binding(properties);
        GroovyShell shell = new GroovyShell(binding);
        System.out.println((boolean) shell.evaluate("a=='fgg'"));
    }
}