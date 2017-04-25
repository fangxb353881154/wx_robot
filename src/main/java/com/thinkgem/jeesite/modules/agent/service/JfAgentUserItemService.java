/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.service;

import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.modules.agent.entity.JfAgentUser;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUserItem;
import com.thinkgem.jeesite.modules.agent.dao.JfAgentUserItemDao;

/**
 * 代理用户项目关联表Service
 * @author jfang
 * @version 2017-03-04
 */
@Service
@Transactional(readOnly = true)
public class JfAgentUserItemService extends CrudService<JfAgentUserItemDao, JfAgentUserItem> {

	public JfAgentUserItem get(String id) {
		return super.get(id);
	}
	
	public List<JfAgentUserItem> findList(JfAgentUserItem jfAgentUserItem) {
		return super.findList(jfAgentUserItem);
	}
	
	public Page<JfAgentUserItem> findPage(Page<JfAgentUserItem> page, JfAgentUserItem jfAgentUserItem) {
		return super.findPage(page, jfAgentUserItem);
	}


	@Transactional(readOnly = false)
	public void save(JfAgentUserItem jfAgentUserItem) {
		super.save(jfAgentUserItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(JfAgentUserItem jfAgentUserItem) {
		super.delete(jfAgentUserItem);
	}

	@Transactional(readOnly = false)
	public void batchSave(JfAgentUser jfAgentUser){
		dao.batchSave(jfAgentUser);
	}

	/**
	 *  更新用户项目额度
	 * @param userItem
	 * @param linesNumber
	 */
	@Transactional(readOnly = false)
	public void updateItemUseLines(JfAgentUserItem userItem, int linesNumber) {
		//更新用户额度
		userItem.setLinesUse(userItem.getLinesUse() + linesNumber);
		userItem.setLinesNotUse(userItem.getLinesCount() - userItem.getLinesUse());
		userItem.setUpdateBy(UserUtils.getUser());
		userItem.setUpdateDate(new Date());
		userItem.setItemId(userItem.getJfAgentItem().getId());
		dao.update(userItem);
	}
}