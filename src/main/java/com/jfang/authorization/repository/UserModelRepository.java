package com.jfang.authorization.repository;

import javax.servlet.http.HttpServletResponse;

/**
 * 通过Key获得用户模型的接口
 * @author jfang
 * @date 2015/10/26.
 */
public interface UserModelRepository<T> {
    /**
     * 通过Key获得用户模型
     * @param key
     * @return
     */
    T getCurrentUser(String key);

    T isVail(String key, HttpServletResponse response);
}
