/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.red.entity.RedGameTotalUpDown;

import java.util.Map;

/**
 * 玩家上下分记录DAO接口
 * @author jfang
 * @version 2017-04-22
 */
@MyBatisDao
public interface RedGameTotalUpDownDao extends CrudDao<RedGameTotalUpDown> {

    void deleteByRoomNum(Map<String, String> param);
}