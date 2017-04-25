package com.jfang.authorization.manager;

import com.jfang.authorization.entity.TokenModel;

import javax.servlet.http.HttpServletRequest;

/**
 * 对Token进行管理的接口
 * @author ScienJus
 * @date 2015/7/31.
 */
public interface TokenManager {

    /**
     * 通过key删除关联关系
     * @param key
     */
    void delRelationshipByKey(String key);

    /**
     * 通过token删除关联关系
     * @param token
     */
    void delRelationshipByToken(String token);

    /**
     * 创建关联关系
     * @param key
     * @param token
     */
    void createRelationship(String key, String token);

    /**
     * 通过token获得对应的key
     * @param token
     * @return
     */
    String getKey(String token);

    /**
     * 创建一个token关联上指定用户
     * @param key 指定用户的id
     * @return 生成的token
     */
     TokenModel createToken(String key);
}
