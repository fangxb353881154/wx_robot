/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.red.entity.RedBagSendLog;
import com.thinkgem.jeesite.modules.red.vo.RedBagSendGroupVo;

import java.util.List;
import java.util.Map;

/**
 * 发包详情DAO接口
 * @author jfang
 * @version 2017-04-13
 */
@MyBatisDao
public interface RedBagSendLogDao extends CrudDao<RedBagSendLog> {

    List<RedBagSendGroupVo> querySendGroup(RedBagSendLog redBagSendLog);

    void deleteByRoomNum(Map<String, String> param);
}