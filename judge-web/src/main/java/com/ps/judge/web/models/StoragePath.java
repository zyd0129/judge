package com.ps.judge.web.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class StoragePath implements Serializable {
    private String url;
    private String host;
    private String file;
    private String path;
}
