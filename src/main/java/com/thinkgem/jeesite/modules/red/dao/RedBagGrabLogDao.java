/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.red.entity.RedBagGrabLog;

import java.util.List;

/**
 * 抢包手金额记录DAO接口
 * @author jfang
 * @version 2017-04-16
 */
@MyBatisDao
public interface RedBagGrabLogDao extends CrudDao<RedBagGrabLog> {

    /**
     * 累加 抢包金额 抢包次数 免死金额 免死次数
     * @param redBagGrabLog
     */
    void cumulativeSum(RedBagGrabLog redBagGrabLog);

    /**
     * 统计抢包手数据
     * @param redBagGrabLog
     * @return
     */
    List<RedBagGrabLog> querySendGroup(RedBagGrabLog redBagGrabLog);

}