/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUser;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUserItem;

/**
 * 代理用户项目关联表DAO接口
 * @author jfang
 * @version 2017-03-04
 */
@MyBatisDao
public interface JfAgentUserItemDao extends CrudDao<JfAgentUserItem> {

    void batchSave(JfAgentUser jfAgentUser);
}