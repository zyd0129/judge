package com.ts.judge.provider.flow.script;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ts.jury.api.request.ApplyRequest;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class JuryInputScriptTest {

    @Test
    public void test() throws IOException {
        String text = "{\n" +
                "\t\"applyId\": \"123\",\n" +
                "\t\"bankCard\": \"123456789\",\n" +
                "\t\"applyMaterial\": {\n" +
                "\t\t\"contact\": {\n" +
                "\t\t\t\"contracts\": [{\n" +
                "\t\t\t\t\"contactName\": \"BhairavSirAig2\",\n" +
                "\t\t\t\t\"contactPhone\": \"9178841463\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"Akshitha\",\n" +
                "\t\t\t\t\"contactPhone\": \"9178841463\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"FaridhaKhanMam\",\n" +
                "\t\t\t\t\"contactPhone\": \"+919989191398\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"Dr.SukeshSir\",\n" +
                "\t\t\t\t\"contactPhone\": \"9178841463\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"ShailjaMam\",\n" +
                "\t\t\t\t\"contactPhone\": \"+918886750012\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"Aarti\",\n" +
                "\t\t\t\t\"contactPhone\": \"+919502718807\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"Komal\",\n" +
                "\t\t\t\t\"contactPhone\": \"+917702487194\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"Dr.AbhinavDmo\",\n" +
                "\t\t\t\t\"contactPhone\": \"900-000-8413\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"Ktha\",\n" +
                "\t\t\t\t\"contactPhone\": \"9398764580\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"Harika\",\n" +
                "\t\t\t\t\"contactPhone\": \"+918686330749\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"Dr.Sathish\",\n" +
                "\t\t\t\t\"contactPhone\": \"8885507637\",\n" +
                "\t\t\t\t\"updateTime\": \"2020-08-19T18:28:47.321\"\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t\"externalChannelData\":{\n" +
                "\t\t\t\"liveDetectionResult\":\"1\",\n" +
                "\t\t\t\"panCardNameVerificationResult\":\"2\",\n" +
                "\t\t\t\"deviceIsAnEmulator\":true,\n" +
                "\t\t\t\"deviceIsInDebugMode\":false,\n" +
                "\t\t\t\"deviceIsRooted\":false,\n" +
                "\t\t\t\"loanPurpose\":\"2\"\n" +
                "\t\t},\n" +
                "\t\t\"msgs\":{\n" +
                "            \"dataList\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"date_sent\":1600735443000,\n" +
                "                    \"read\":0,\n" +
                "                    \"type\":1,\n" +
                "                    \"content\":\"【余额不足提醒】尊敬的客户：截至2020年09月22日 01时39分，您的帐户可用余额已不足5.00元，为不影响您的正常使用，请及时充值缴费。电信用户费用欠交信息已纳入人行征信，为不影响您的个人信誉，请及时、足额交纳欠费。查询当月话费请回复101，或点击http://a.189.cn/Je6JUq 进入电信手机营业厅，查账缴费一手搞定。【中国电信】\",\n" +
                "                    \"seen\":1,\n" +
                "                    \"phone\":\"10001\",\n" +
                "                    \"person\":0,\n" +
                "                    \"time\":1600735447671,\n" +
                "                    \"_id\":51,\n" +
                "                    \"status\":-1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"date_sent\":1600476363000,\n" +
                "                    \"read\":0,\n" +
                "                    \"type\":1,\n" +
                "                    \"content\":\"【余额不足提醒】尊敬的客户：截至2020年09月19日 01时38分，您的帐户可用余额已不足15.00元，为不影响您的正常使用，请及时充值缴费。电信用户费用欠交信息已纳入人行征信，为不影响您的个人信誉，请及时、足额交纳欠费。查询当月话费请回复101，或点击http://a.189.cn/Je6JUq 进入电信手机营业厅，查账缴费一手搞定。【中国电信】\",\n" +
                "                    \"seen\":1,\n" +
                "                    \"phone\":\"10001\",\n" +
                "                    \"person\":0,\n" +
                "                    \"time\":1600476365560,\n" +
                "                    \"_id\":50,\n" +
                "                    \"status\":-1\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "\t\t\"albs\":{\n" +
                "        \"dataList\":[\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"name\":\"1\",\n" +
                "\t\t\t\t\"author\":\"2\",\n" +
                "\t\t\t\t\"height\":\"3\",\n" +
                "\t\t\t\t\"width\":\"4\",\n" +
                "\t\t\t\t\"longitude\":\"5\",\n" +
                "\t\t\t\t\"latitude\":\"6\",\n" +
                "\t\t\t\t\"date\":\"7\",\n" +
                "\t\t\t\t\"createTime\":\"88\",\n" +
                "\t\t\t\t\"model\":\"6\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"name\":\"1\",\n" +
                "\t\t\t\t\"author\":\"2\",\n" +
                "\t\t\t\t\"height\":\"3\",\n" +
                "\t\t\t\t\"width\":\"4\",\n" +
                "\t\t\t\t\"longitude\":\"5\",\n" +
                "\t\t\t\t\"latitude\":\"6\",\n" +
                "\t\t\t\t\"date\":\"7\",\n" +
                "\t\t\t\t\"createTime\":\"88\",\n" +
                "\t\t\t\t\"model\":\"6\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "        },\n" +
                "\t\t\"device\": {\n" +
                "\t\t\t\"apps\": [{\n" +
                "\t\t\t\t\"appName\": \"Myntra\",\n" +
                "\t\t\t\t\"appSource\": \"0\",\n" +
                "\t\t\t\t\"installTime\": \"2020-06-19T00:55:44.736\",\n" +
                "\t\t\t\t\"lastUpdateTime\": \"2020-07-26T20:06:28.408\",\n" +
                "\t\t\t\t\"packName\": null,\n" +
                "\t\t\t\t\"systemType\": \"\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"appName\": \"Ola\",\n" +
                "\t\t\t\t\"appSource\": \"0\",\n" +
                "\t\t\t\t\"installTime\": \"2020-06-19T00:56:28.641\",\n" +
                "\t\t\t\t\"lastUpdateTime\": \"2020-08-17T09:16:26.710\",\n" +
                "\t\t\t\t\"packName\": null,\n" +
                "\t\t\t\t\"systemType\": \"\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"appName\": \"KoalaCash\",\n" +
                "\t\t\t\t\"appSource\": \"0\",\n" +
                "\t\t\t\t\"installTime\": \"2020-09-02T14:57:54.282\",\n" +
                "\t\t\t\t\"lastUpdateTime\": \"2020-09-02T14:57:54.282\",\n" +
                "\t\t\t\t\"packName\": null,\n" +
                "\t\t\t\t\"systemType\": \"\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"appName\": \"getrupee\",\n" +
                "\t\t\t\t\"appSource\": \"0\",\n" +
                "\t\t\t\t\"installTime\": \"2020-09-06T14:19:36.243\",\n" +
                "\t\t\t\t\"lastUpdateTime\": \"2020-09-06T14:19:36.243\",\n" +
                "\t\t\t\t\"packName\": null,\n" +
                "\t\t\t\t\"systemType\": \"\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"appName\": \"MOMO\",\n" +
                "\t\t\t\t\"appSource\": \"0\",\n" +
                "\t\t\t\t\"installTime\": \"2020-09-04T16:10:47.936\",\n" +
                "\t\t\t\t\"lastUpdateTime\": \"2020-09-04T16:10:47.936\",\n" +
                "\t\t\t\t\"packName\": null,\n" +
                "\t\t\t\t\"systemType\": \"\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"ip\": \"49.37.137.117\",\n" +
                "\t\t\t\"latitude\": \"78.447353\",\n" +
                "\t\t\t\"longitude\": \"17.446226\",\n" +
                "\t\t\t\"mobileBootTime\": 32169456,\n" +
                "\t\t\t\"mobileBrand\": \"OnePlus\",\n" +
                "\t\t\t\"mobileLanguage\": \"en\",\n" +
                "\t\t\t\"mobileModels\": \"HD1901\",\n" +
                "\t\t\t\"os\": \"Android\",\n" +
                "\t\t\t\"osVersion\": \"10\",\n" +
                "\t\t\t\"systemAppCount\": 0,\n" +
                "\t\t\t\"totalAppCount\": 142,\n" +
                "\t\t\t\"wifiMac\": \"713dcf81569e4806\"\n" +
                "\t\t},\n" +
                "\t\t\"order\": {\n" +
                "\t\t\t\"applyLoanTime\": \"2020-09-21T17:10:40.941\",\n" +
                "\t\t\t\"applyingLatitude\": \"17.446226\",\n" +
                "\t\t\t\"applyingLongitude\": \"78.447353\",\n" +
                "\t\t\t\"bankCard\": \"50100235048060\",\n" +
                "\t\t\t\"loanAmount\": 3000.0,\n" +
                "\t\t\t\"loanTerm\": 7,\n" +
                "\t\t\t\"loanType\": \"wificash\",\n" +
                "\t\t\t\"orderId\": \"J0921379732229904924672\",\n" +
                "\t\t\t\"termUnit\": 1\n" +
                "\t\t},\n" +
                "\t\t\"user\": {\n" +
                "\t\t\t\"aadhaarInfo\": {\n" +
                "\t\t\t\t\"aadhaar\": \"237295070529\",\n" +
                "\t\t\t\t\"birthday\": \"1998-06-19\",\n" +
                "\t\t\t\t\"gender\": 2,\n" +
                "\t\t\t\t\"phone\": \"9700622641\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"address\": \"Andhra Pradesh Hyderabad  \",\n" +
                "\t\t\t\"applyIpCity\": \"49.37.139.71\",\n" +
                "\t\t\t\"authTime\": \"2020-08-19T18:21:55\",\n" +
                "\t\t\t\"birthday\": \"1998-06-19\",\n" +
                "\t\t\t\"country\": \"20\",\n" +
                "\t\t\t\"degree\": \"Master degree\",\n" +
                "\t\t\t\"email\": \"akhikrh19061998@gmail.com\",\n" +
                "\t\t\t\"emergencyContact\": [{\n" +
                "\t\t\t\t\"contactName\": \"Siri\",\n" +
                "\t\t\t\t\"contactPhone\": \"6302239704\",\n" +
                "\t\t\t\t\"contactRelation\": \"2\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"contactName\": \"Bro***[表情]\",\n" +
                "\t\t\t\t\"contactPhone\": \"8142609498\",\n" +
                "\t\t\t\t\"contactRelation\": \"6\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"gender\": 2,\n" +
                "\t\t\t\"hasChildren\": null,\n" +
                "\t\t\t\"idCard\": \"237295070529\",\n" +
                "\t\t\t\"incomeType\": null,\n" +
                "\t\t\t\"industry\": \"Medical/Health-care\",\n" +
                "\t\t\t\"liveSimilarity\": \"58.257000000000005\",\n" +
                "\t\t\t\"marriage\": 2,\n" +
                "\t\t\t\"mobile\": \"9700622641\",\n" +
                "\t\t\t\"nativeLanguage\": \"India\",\n" +
                "\t\t\t\"profession\": \"Consumer care\",\n" +
                "\t\t\t\"realIpCity\": \"49.37.139.71\",\n" +
                "\t\t\t\"registerChannel\": \"android\",\n" +
                "\t\t\t\"registerTime\": \"2020-08-18T19:30:41\",\n" +
                "\t\t\t\"religion\": null,\n" +
                "\t\t\t\"userId\": \"15322\",\n" +
                "\t\t\t\"userName\": \"Akhila Bandi\",\n" +
                "\t\t\t\"wageBegin\": 3,\n" +
                "\t\t\t\"wageEnd\": 3,\n" +
                "\t\t\t\"workingAddress\": \" aig hospitals , mindspace road \",\n" +
                "\t\t\t\"workingMonths\": \"36\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"callbackUrl\": \"https://www.doloan1.in/act/async/callback/callJudgeResult.do\",\n" +
                "\t\"deviceFingerPrint\": \"713dcf81569e4806\",\n" +
                "\t\"flowCode\": \"test\",\n" +
                "\t\"idCard\": \"237295070529\",\n" +
                "\t\"ip\": \"49.37.137.117\",\n" +
                "\t\"mobile\": \"9178841463\",\n" +
                "\t\"orderId\": \"39141\",\n" +
                "\t\"productCode\": \"82e7f1c93e364028a1eb13a89b006212\",\n" +
                "\t\"taskId\": null,\n" +
                "\t\"tenantCode\": \"ps\",\n" +
                "\t\"transactionTime\": \"2020-09-21T17:10:40.941\",\n" +
                "\t\"userId\": \"15322\",\n" +
                "\t\"userName\": \"Akhila Bandi\",\n" +
                "    \"channel\":\"loanSupermarket\"\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.registerModule(new JavaTimeModule());
        ApplyRequest applyRequest = objectMapper.readValue(text, ApplyRequest.class);
        System.out.println(objectMapper.writeValueAsString(applyRequest));
    }

}