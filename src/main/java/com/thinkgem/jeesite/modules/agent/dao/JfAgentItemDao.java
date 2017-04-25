/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentItem;

/**
 * 授权项目DAO接口
 * @author jfang
 * @version 2017-03-04
 */
@MyBatisDao
public interface JfAgentItemDao extends CrudDao<JfAgentItem> {
	
}