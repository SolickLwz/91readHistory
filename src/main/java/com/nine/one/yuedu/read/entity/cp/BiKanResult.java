package com.nine.one.yuedu.read.entity.cp;

public class BiKanResult {


    /**
     * code : A00000
     * data :
     * msg : Success
     */

    private String code;
    private Object data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BiKanResult(String code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
