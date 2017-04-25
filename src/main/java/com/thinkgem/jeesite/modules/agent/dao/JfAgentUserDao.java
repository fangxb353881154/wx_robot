/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUser;

/**
 * 代理用户DAO接口
 * @author jfang
 * @version 2017-03-04
 */
@MyBatisDao
public interface JfAgentUserDao extends CrudDao<JfAgentUser> {
	
}