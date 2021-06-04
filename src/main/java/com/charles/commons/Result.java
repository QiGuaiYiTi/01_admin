package com.charles.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description : 返回结果统一封装类
 *
 * @author : Charles
 * @created : 2021/6/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {

    //状态码
    private int code;
    //提示信息
    private String msg;
    //数据
    private Object data;

    /**
     * 成功
     * @param data
     * @return
     */
    public static Result success(Object data){
        return success(200,"操作成功",data);
    }

    private static Result success( int code,String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * 失败
     * @param msg
     * @return
     */
    public static Result fail(String msg){
        return fail(400,msg,null);
    }
    public static Result fail(String msg,Object data){
        return fail(400,msg,data);
    }

    private static Result fail( int code,String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}
