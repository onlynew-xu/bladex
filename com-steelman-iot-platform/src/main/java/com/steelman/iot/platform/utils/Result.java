package com.steelman.iot.platform.utils;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tang
 * date 2020-11-20
 */
@ApiModel(description = "返回结果封装")
public class Result<T> implements Serializable {

    @ApiModelProperty(value = "返回状态码//-3:参数错误;-2:服务器异常;-1:数据为空;0:操作失败;1:操作成功;" )
    private int code;

    @ApiModelProperty(value = "错误信息 默认为空")
    private String msg;

    @ApiModelProperty(value = "返回数据时间戳")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    private T data;

    public Result() {
        timestamp = new Date();
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        timestamp = new Date();
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        timestamp = new Date();
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
        timestamp = new Date();
    }

    public Result(int code, String msg, Date timestamp) {
        this.code = code;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        if (this.code == 200) {
            this.msg = "ok";
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static Result paramError(Result result) {
        result.setMsg("param error");
        result.setCode(0);
        return result;
    }

    public static Result empty(Result result) {
        result.setMsg("empty");
        result.setCode(-1);
        return result;
    }
    public static Result exceptionRe(Result result){
        result.setMsg("exception");
        result.setCode(-2);
        return result;
    }

    public static Result success(String msg, Object data) {
        Result result = new Result();
        result.setCode(1);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result success() {
        Result result = new Result();
        result.setCode(1);
        result.setMsg("操作成功");
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(1);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static Result paramsError() {
        Result result = new Result();
        result.setCode(-1);
        result.setMsg("参数错误");
        return result;
    }


    public static Result fail(String msg, Object data) {
        Result result = new Result();
        result.setCode(-1);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result MissingToken() {
        Result result = new Result();
        result.setCode(1000);
        result.setMsg("缺少令牌");
        return result;
    }
}
