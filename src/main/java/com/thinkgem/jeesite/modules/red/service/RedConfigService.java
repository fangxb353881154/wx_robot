/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.service;

import java.util.List;

import com.thinkgem.jeesite.modules.red.utils.RedConfigUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.red.entity.RedConfig;
import com.thinkgem.jeesite.modules.red.dao.RedConfigDao;

/**
 * wx群配置Service
 * @author jfang
 * @version 2017-04-12
 */
@Service
@Transactional(readOnly = true)
public class RedConfigService extends CrudService<RedConfigDao, RedConfig> {

	public RedConfig get(String id) {
		return super.get(id);
	}
	
	public List<RedConfig> findList(RedConfig redConfig) {
		return super.findList(redConfig);
	}
	
	public Page<RedConfig> findPage(Page<RedConfig> page, RedConfig redConfig) {
		return super.findPage(page, redConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(RedConfig redConfig) {
		super.save(redConfig);
		//清除缓存
		RedConfigUtils.clearCache(redConfig.getId());
	}
	
	@Transactional(readOnly = false)
	public void delete(RedConfig redConfig) {
		super.delete(redConfig);
	}
	
}