/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentItem;
import com.thinkgem.jeesite.modules.agent.dao.JfAgentItemDao;

/**
 * 授权项目Service
 * @author jfang
 * @version 2017-03-04
 */
@Service
@Transactional(readOnly = true)
public class JfAgentItemService extends CrudService<JfAgentItemDao, JfAgentItem> {

	public JfAgentItem get(String id) {
		return super.get(id);
	}

	public List<JfAgentItem> findAllList(JfAgentItem jfAgentItem) {
		return dao.findAllList(jfAgentItem);
	}

	public List<JfAgentItem> findList(JfAgentItem jfAgentItem) {
		return super.findList(jfAgentItem);
	}
	
	public Page<JfAgentItem> findPage(Page<JfAgentItem> page, JfAgentItem jfAgentItem) {
		return super.findPage(page, jfAgentItem);
	}

	public JfAgentItem getItemBySerial(String serial) {
		JfAgentItem item = new JfAgentItem();
		item.setSerial(serial);
		List<JfAgentItem> list = dao.findList(item);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void save(JfAgentItem jfAgentItem) {
		super.save(jfAgentItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(JfAgentItem jfAgentItem) {
		super.delete(jfAgentItem);
	}
	
}