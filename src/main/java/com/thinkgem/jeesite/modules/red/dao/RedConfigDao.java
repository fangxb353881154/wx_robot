/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.red.entity.RedConfig;

/**
 * wx群配置DAO接口
 * @author jfang
 * @version 2017-04-12
 */
@MyBatisDao
public interface RedConfigDao extends CrudDao<RedConfig> {
	
}