package org.jeecg.modules.publishlist.tools;

public class Result{
    Integer code;
    String msg;
    String tenant_access_token;
    Integer expire;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTenant_access_token() {
        return tenant_access_token;
    }

    public void setTenant_access_token(String tenant_access_token) {
        this.tenant_access_token = tenant_access_token;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }
}
