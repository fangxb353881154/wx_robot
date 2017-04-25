/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.red.entity.RedGameTotalUpDown;
import com.thinkgem.jeesite.modules.red.dao.RedGameTotalUpDownDao;

/**
 * 玩家上下分记录Service
 * @author jfang
 * @version 2017-04-22
 */
@Service
@Transactional(readOnly = true)
public class RedGameTotalUpDownService extends CrudService<RedGameTotalUpDownDao, RedGameTotalUpDown> {

	public RedGameTotalUpDown get(String id) {
		return super.get(id);
	}
	
	public List<RedGameTotalUpDown> findList(RedGameTotalUpDown redGameTotalUpDown) {
		return super.findList(redGameTotalUpDown);
	}
	
	public Page<RedGameTotalUpDown> findPage(Page<RedGameTotalUpDown> page, RedGameTotalUpDown redGameTotalUpDown) {
		return super.findPage(page, redGameTotalUpDown);
	}
	
	@Transactional(readOnly = false)
	public void save(RedGameTotalUpDown redGameTotalUpDown) {
		super.save(redGameTotalUpDown);
	}
	
	@Transactional(readOnly = false)
	public void delete(RedGameTotalUpDown redGameTotalUpDown) {
		super.delete(redGameTotalUpDown);
	}
	
}