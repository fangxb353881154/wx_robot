package com.jfang.authorization.manager.impl;


import com.jfang.authorization.entity.TokenModel;
import com.jfang.authorization.exception.MethodNotSupportException;
import com.jfang.authorization.manager.TokenManager;

import java.util.UUID;

/**
 * Token管理的基础类
 * @author jfang
 * @date 2015/10/27.
 */
public abstract class AbstractTokenManager implements TokenManager {

    protected int tokenExpireSeconds = 7 * 24 * 3600;

    protected boolean singleTokenWithUser = true;

    protected boolean flushExpireAfterOperation = true;

    public void setTokenExpireSeconds(int tokenExpireSeconds) {
        this.tokenExpireSeconds = tokenExpireSeconds;
    }

    public void setSingleTokenWithUser(boolean singleTokenWithUser) {
        this.singleTokenWithUser = singleTokenWithUser;
    }

    public void setFlushExpireAfterOperation(boolean flushExpireAfterOperation) {
        this.flushExpireAfterOperation = flushExpireAfterOperation;
    }

    @Override
    public void delRelationshipByKey(String key) {
        //如果是多个Token关联同一个Key，不允许直接通过Key删除所有Token，防止误操作
        if (!singleTokenWithUser) {
            throw new MethodNotSupportException("非单点登录时无法调用该方法");
        }
        delSingleRelationshipByKey(key);
    }

    @Override
    public TokenModel createToken(String key) {
        //使用uuid作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenModel model = new TokenModel(key, token);
        createRelationship(key, token);
        return model;
    }

    /**
     * 一个用户只能绑定一个Token时通过Key删除关联关系
     * @param key
     */
    protected abstract void delSingleRelationshipByKey(String key);

    @Override
    public void createRelationship(String key, String token) {
        //根据设置的每个用户是否只允许绑定一个Token，调用不同的方法
        if (singleTokenWithUser) {
            createSingleRelationship(key, token);
        } else {
            createMultipleRelationship(key, token);
        }
    }

    /**
     * 一个用户可以绑定多个Token时创建关联关系
     * @param key
     * @param token
     */
    protected abstract void createMultipleRelationship(String key, String token);

    /**
     * 一个用户只能绑定一个Token时创建关联关系
     * @param key
     * @param token
     */
    protected abstract void createSingleRelationship(String key, String token);

    @Override
    public String getKey(String token) {
        String key = getKeyByToken(token);
        //根据设置，在每次有效操作后刷新过期时间
        if (key != null && flushExpireAfterOperation) {
            flushExpireAfterOperation(key, token);
        }
        return key;
    }

    /**
     * 通过Token获得Key
     * @param token
     * @return
     */
    protected abstract String getKeyByToken(String token);

    /**
     * 在操作后刷新Token的过期时间
     * @param key
     * @param token
     */
    protected abstract void flushExpireAfterOperation(String key, String token);
}
