package com.teacheredu.easy.enenum;

/**
 * Created by kiera1 on 15/9/18.
 */
public enum MethodType {
    GET("GET"), POST("POST");

    private String type;

    private MethodType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
