/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentCode;
import com.thinkgem.jeesite.modules.agent.vo.AgentCodeTypeGroup;

import java.util.List;

/**
 * 授权码DAO接口
 * @author jfang
 * @version 2017-03-04
 */
@MyBatisDao
public interface JfAgentCodeDao extends CrudDao<JfAgentCode> {

    void batchSave(JfAgentCode jfAgentCode);

    /**
     * 统计授权码使用情况
     * @param jfAgentCode
     * @return
     */
    List<AgentCodeTypeGroup> queryCodeGroupByType(JfAgentCode jfAgentCode);
}