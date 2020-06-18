package com.ps.common.query;

import lombok.Data;

@Data
public class UserQuery {
    private String fuzzyValue;
    private String role;
}
//CONFIG_SERVER=192.168.40.150:8848;CONFIG_SERVER_NAMESPACE=b90363b4-1d0e-4cbb-9410-a9b4e8d58e16;CONFIG_ACTIVE=dev