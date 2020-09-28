package com.ps.judge.provider.flow.third.txScore;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * {
 *
 *
 *
 *
 *
 *,,,,}
 */
@Data
public class ModelParams implements Serializable {
    private String sex; //"sex":"Male",
    private String education; //"education":"High school",
    private String marryState; //"marry_state":"Unmarried"
    private String occupationClass; // occupation_class":"Manager"
    private Double salary; //"salary":20000.0,"
    private String workingYears; //"working_years":"more than 5 years",
    private String registerAddr; //"/register_addr":"Unnamed Road, Srikaranpur, Rajasthan 335073, India",
    private Integer inBigCity; //"in_big_city":0,
    private String incomeBy;//,"income_by":"Cash"
    private Integer supplementState;//"supplement_state":0
    private Date registTime; //"regist_time":"2020-09-23",
    private Date  dateBirthday; //"date_birthday":"1992-05-03"
    private Integer appAmount;//"app_amount":57,
    private Integer contact; //"contact":277,
    private Integer realContact; //"real_contact":261
    private String incomeType;
    private Integer financialApp; //"financial_app":16,

    @JsonProperty("marry_state")
    public String getMarryState() {
        return marryState;
    }

    @JsonProperty("occupation_class")
    public String getOccupationClass() {
        return occupationClass;
    }

    @JsonProperty("working_years")
    public String getWorkingYears() {
        return workingYears;
    }

    @JsonProperty("register_addr")
    public String getRegisterAddr() {
        return registerAddr;
    }

    @JsonProperty("in_big_city")
    public Integer getInBigCity() {
        return inBigCity;
    }

    @JsonProperty("income_by")
    public String getIncomeBy() {
        return incomeBy;
    }

    @JsonProperty("supplement_state")
    public Integer getSupplementState() {
        return supplementState;
    }

    @JsonProperty("regist_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getRegistTime() {
        return registTime;
    }

    @JsonProperty("date_birthday")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getDateBirthday() {
        return dateBirthday;
    }

    @JsonProperty("app_amount")
    public Integer getAppAmount() {
        return appAmount;
    }

    @JsonProperty("real_contact")
    public Integer getRealContact() {
        return realContact;
    }

    @JsonProperty("income_type")
    public String getIncomeType() {
        return incomeType;
    }

    @JsonProperty("financial_app")
    public Integer getFinancialApp() {
        return financialApp;
    }
}
