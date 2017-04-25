package com.thinkgem.jeesite.common.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ResultUtils {

    public final static String STATE_FAILURE = "0";
    public final static String STATE_SUCCESS = "1";
    public final static String STATE_FAILURE_ERROR = "2";
    public final static String MESSAGE_KEY_ = "msg";


    public static Map<String, Object> getFailure() {
        return getResults(STATE_FAILURE, "操作失败！", null);
    }

    public static Map<String,Object> getSuccess(){
        return getResults(STATE_SUCCESS, "操作成功！", null);
    }


    public static Map<String, Object> getSuccess(Object data) {
        return getResults(STATE_SUCCESS, "操作成功！", data);
    }

    public static Map<String, Object> getFailure(String message){
        return getFailure(MESSAGE_KEY_, message);
    }

    public static Map<String, Object> getFailure(String messageKey, String message) {
        HashMap<String, Object> result = new HashMap();
        result.put("flag", STATE_FAILURE);
        result.put(messageKey, message);
        return result;
    }

    public static Map<String, Object> getSuccess(String message){
        return getSuccess(MESSAGE_KEY_, message);

    }
    public static Map<String, Object> getSuccess(String messageKey, String message) {
        HashMap<String, Object> result = new HashMap();
        result.put("flag", STATE_SUCCESS);
        result.put(messageKey, message);
        return result;
    }

    public static Map<String, Object> getFailure(String message, Object data){
        return getResults(STATE_FAILURE, message, data);
    }

    public static Map<String, Object> getSuccess(String message, Object data){
        return getResults(STATE_SUCCESS, message, data);
    }

    public static Map<String, Object> getResults(String flag, String message, Object data) {
        return getResults(flag, MESSAGE_KEY_, message, data);
    }

    public static Map<String, Object> getResults(String flag,String messageKey, String message, Object data ) {
        HashMap<String, Object> result = new HashMap();
        result.put("flag", flag);
        result.put(messageKey, message);
        result.put("data", data);
        return result;
    }
}
