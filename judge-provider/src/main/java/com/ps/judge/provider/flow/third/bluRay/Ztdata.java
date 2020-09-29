package com.ps.judge.provider.flow.third.bluRay;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * @author <a href="sunshaokun@adpanshi.com">sunshaokun</a>
 * @date 2020/7/21 16:37
 */
@Data
public class Ztdata {
  private String serialNumber;
  private String imeiIsSame;
  private String aadhaar;
  private String pan;
  private String salaryRange;
  private String brandModel;
  private String age;
  private String sex;
  private String marryState;
  private String jobPosition;
  private String applyTime;
  private String phoneNumber1;
  private String phoneNumber2;
  private String education;
  private String contact1;
  private String contact2;

  public String getPhoneNumber1() {
    if (StringUtils.isBlank(phoneNumber1)) {
      return phoneNumber1;
    } else {
      return DigestUtils.md5DigestAsHex(phoneNumber1.getBytes());
    }
  }

  public String getPhoneNumber2() {
    if (StringUtils.isBlank(phoneNumber2)) {
      return phoneNumber2;
    } else {
      return DigestUtils.md5DigestAsHex(phoneNumber2.getBytes());
    }
  }
}
