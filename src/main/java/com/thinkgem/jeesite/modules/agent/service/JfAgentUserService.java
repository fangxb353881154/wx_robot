/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.service;

import java.util.List;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentItem;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUserItem;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUser;
import com.thinkgem.jeesite.modules.agent.dao.JfAgentUserDao;

/**
 * 代理用户Service
 * @author jfang
 * @version 2017-03-04
 */
@Service
@Transactional(readOnly = true)
public class JfAgentUserService extends CrudService<JfAgentUserDao, JfAgentUser> {

	@Autowired
	private SystemService systemService;
	@Autowired
	private JfAgentUserItemService jfAgentUserItemService;
	@Autowired
	private JfAgentItemService jfAgentItemService;

	public JfAgentUser get(String id) {
		return super.get(id);
	}
	
	public List<JfAgentUser> findList(JfAgentUser jfAgentUser) {
		return super.findList(jfAgentUser);
	}
	
	public Page<JfAgentUser> findPage(Page<JfAgentUser> page, JfAgentUser jfAgentUser) {
		return super.findPage(page, jfAgentUser);
	}
	
	@Transactional(readOnly = false)
	public void save(JfAgentUser jfAgentUser) {
		List<JfAgentUserItem> jfAgentUserItems = Lists.newArrayList();
		if (StringUtils.isBlank(jfAgentUser.getId())) {
			jfAgentUser.setIsNewRecord(true);//自定义ID

			List<String> roleIdList = Lists.newArrayList();
			roleIdList.add("94ca5c8e725a482caff7bbfb023d11cd");//设置默认角色ID
			jfAgentUser.getUser().setRoleIdList(roleIdList);

			//设置默认公司ID
			jfAgentUser.getUser().setCompany(UserUtils.getUser().getCompany());
			jfAgentUser.getUser().setOffice(UserUtils.getUser().getOffice());

		}else{
			JfAgentUserItem userItem = new JfAgentUserItem();
			userItem.setUserId(jfAgentUser.getId());
			jfAgentUserItems = jfAgentUserItemService.findList(userItem);
		}
		jfAgentUser.getUser().setName(jfAgentUser.getName());
		systemService.saveUser(jfAgentUser.getUser());//保存登录用户

		List<JfAgentUserItem> userItems = jfAgentUser.getJfAgentUserItems();
		if (userItems != null && userItems.size() > 0) {
			boolean isEdit = (jfAgentUserItems != null && jfAgentUserItems.size() > 0); //是否已用项目
			for (JfAgentUserItem ui : userItems) {
				ui.setLinesNotUse(ui.getLinesCount());
				if (isEdit) {
					//对已用项目额度更新
					for (JfAgentUserItem oUi : jfAgentUserItems) {
						if (StringUtils.equals(oUi.getJfAgentItem().getId(), ui.getItemId())) {
							if (oUi.getLinesCount() - ui.getLinesCount() > oUi.getLinesNotUse()) {
								JfAgentItem item = jfAgentItemService.get(ui.getItemId());
								throw new RuntimeException(item.getName() + "(" + item.getSerial() + ")项目剩余可用额度不足！");
							}else {
								//额度计算
								ui.setLinesUse(oUi.getLinesUse());
								ui.setLinesNotUse(ui.getLinesCount() - ui.getLinesUse());
							}
						}
					}
				}
			}
			jfAgentUser.setJfAgentUserItems(userItems);
		}
		jfAgentUser.setId(jfAgentUser.getUser().getId());
		super.save(jfAgentUser);

		//保存额度
		jfAgentUserItemService.delete(new JfAgentUserItem(jfAgentUser.getId()));//删除原有的
		jfAgentUserItemService.batchSave(jfAgentUser);

	}
	
	@Transactional(readOnly = false)
	public void delete(JfAgentUser jfAgentUser) {
		super.delete(jfAgentUser);
	}

	@Transactional(readOnly = false)
	public void usable(JfAgentUser jfAgentUser) {
		dao.update(jfAgentUser);
	}
}