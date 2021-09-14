package com.claim.server.common.vo;

import java.io.Serializable;

/**
 * 统一返回对象
 * @param <T>
 */
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 返回结果状态
     */
    public int status;

    /**
     * 返回结果描述
     */
    private String msg;

    /**
     * 返回结果信息
     */
    private T data;

    public ApiResponse() {
        status = ResponseCode.SUCCESS.code;
        msg = "Success";
    }

    public ApiResponse(ResponseCode status, String msg) {
        super();
        this.status = status.code;
        this.msg = msg;
    }

    public ApiResponse(ResponseCode status, String msg, T data) {
        super();
        this.status = status.code;
        this.msg = msg;
        this.data = data;
    }

    public ApiResponse(T data) {
        super();
        if (data instanceof Exception) {
            status = ResponseCode.INTERNAL_SERVER_ERROR.code;
            msg = ((Exception) data).getMessage();
        } else {
            status = ResponseCode.SUCCESS.code;
            msg = "Success";
            this.data = data;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
