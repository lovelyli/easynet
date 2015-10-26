package com.teacheredu.easy.base;

/**
 * Created by kiera1 on 15/9/22.
 */
public abstract class BaseBean {
    protected String msg;
    protected int status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
