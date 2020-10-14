package com.ts.judge.provider.flow.script;

import com.ts.judge.provider.flow.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

public class TxScoreInputScript implements IInputScript {
    /**
     * {"education":"High school",
     * "app_amount":57,"sex":"Male",
     * "working_years":"more than 5 years",
     * "in_big_city":0,"salary":20000.0,
     * "occupation_class":"Manager","real_contact":261,"financial_app":16,"register_addr":"Unnamed Road, Srikaranpur, Rajasthan 335073, India","regist_time":"2020-09-23","date_birthday":"1992-05-03","contact":277,"income_by":"Cash","marry_state":"Unmarried","supplement_state":0}
     * @param processInstance
     * @return
     */
    @Override
    public Map<String, Object> parse(ProcessInstance processInstance) {
        Map<String, Object> modelParams = new HashMap<>();
        Map<String, Object> processParams = processInstance.getProcessParams();
        Map<String, Object> jury = (Map<String, Object>) processParams.get("jury");
        String gender = (String) jury.get("gender");
        modelParams.put("sex", genderConverter(gender));
        String education = (String) jury.get("education");
        modelParams.put("education", educationConverter(education));
        String maritalStatus = (String) jury.get("maritalStatus");
        modelParams.put("marry_state", maritalStatusConverter(maritalStatus));
        modelParams.put("occupation_class", jury.get("profession"));
        modelParams.put("salary", jury.get("monthlyIncome"));
        Double workingYears = (Double) jury.get("workingYears");
        modelParams.put("working_years", workingYears);
        modelParams.put("register_addr", jury.get("registeredAddress"));
        Boolean appliedCityIsOpenCity = (Boolean) jury.get("appliedCityIsOpenCity");
        modelParams.put("in_big_city", appliedCityIsOpenCity ? 1 : 0);
        modelParams.put("income_by", jury.get("incomeType"));
        modelParams.put("supplement_state", 1);
        modelParams.put("regist_time", jury.get("registerTime"));
        modelParams.put("date_birthday", jury.get("birthday"));
        modelParams.put("app_amount", jury.get("loanAppCount"));
        modelParams.put("contact", jury.get("contactItemCount"));
        modelParams.put("real_contact", jury.get("contactItemCountDistincted"));
        modelParams.put("financial_app", jury.get("loanAppCount"));
        return modelParams;
    }

    private String genderConverter(String gender) {
        String result;
        if ("male".equals(gender)) {
            result = "Male";
        } else if ("female".equals(gender)) {
            result = "Female";
        } else {
            result = "Male";
        }
        return result;
    }

    private String educationConverter(String education) {
        String result;
        switch (education) {
            case "belowHighSchool":
                result = "Junior high school or below";
                break;
            case "highSchool":
                result = "High school";
                break;
            case "juniorCollege":
                result = "Junior college";
                break;
            case "bachelor":
                result = "Bachelor degree";
                break;
            case "master":
                result = "Master degree";
                break;
            case "doctor":
                result = "Doctoral degree";
                break;
            case "postdoc":
                result = "Postdoctoral degree";
                break;
            default:
                result = "others";
        }
        return result;
    }

    private String maritalStatusConverter(String marryStatus) {
        String result;
        if ("married".equals(marryStatus)) {
            result = "Married";
        } else {
            result = "Unmarried";
        }
        return result;
    }

    /**
     * working_years_dict = {'1 to 2 years': 1, '2 to 3 years': 2, '3 to 4 years': 3, '3 to 6 months': 4,
     * '4 to 5 years': 5, '6 to 12 months': 7, 'more than 5 years': 8, 'Within 3 months': 9}
     *
     * @return
     */
    private String workingYearConverter(Double workingYear) {
        String result;
        if (workingYear >= 5) {
            result = "more than 5 years";
        } else if (workingYear >= 4) {
            result = "4 to 5 years";
        } else if (workingYear >= 3) {
            result = "3 to 4 years";
        } else if (workingYear >= 2) {
            result = "2 to 3 years";
        } else if (workingYear >= 1) {
            result = "1 to 2 years";
        } else if (workingYear >= 0.6) {
            result = "6 to 12 months";
        } else if (workingYear >= 0.3) {
            result = "3 to 6 months";
        } else {
            result = "Within 3 months";
        }
        return result;
    }
}
