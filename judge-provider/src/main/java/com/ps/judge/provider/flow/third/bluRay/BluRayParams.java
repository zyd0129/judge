package com.ps.judge.provider.flow.third.bluRay;

import lombok.Data;

/**
 * @author <a href="sunshaokun@adpanshi.com">sunshaokun</a>
 * @date 2020/7/21 16:25
 */
@Data
public class BluRayParams {
  private String aliasId;
  private Object extend;
  private String callUrl;
  private Ztdata ztdata;
  private Msgs msgs;
  private Txls txls;
  private Albs albs;
  private Apps apps;
}
