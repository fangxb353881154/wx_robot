/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.service;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.agent.dao.JfAgentItemDao;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentItem;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUserItem;
import com.thinkgem.jeesite.modules.agent.vo.AgentCodeTypeGroup;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.xmlbeans.impl.values.JavaQNameHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentCode;
import com.thinkgem.jeesite.modules.agent.dao.JfAgentCodeDao;

/**
 * 授权码Service
 * @author jfang
 * @version 2017-03-04
 */
@Service
@Transactional(readOnly = true)
public class JfAgentCodeService extends CrudService<JfAgentCodeDao, JfAgentCode> {

	@Autowired
	private JfAgentItemDao jfAgentItemDao;
	@Autowired
	private JfAgentUserItemService jfAgentUserItemService;

	public JfAgentCode get(String id) {
		return super.get(id);
	}
	
	public List<JfAgentCode> findList(JfAgentCode jfAgentCode) {
		return super.findList(jfAgentCode);
	}
	
	public Page<JfAgentCode> findPage(Page<JfAgentCode> page, JfAgentCode jfAgentCode) {
		return super.findPage(page, jfAgentCode);
	}
	
	@Transactional(readOnly = false)
	public void save(JfAgentCode jfAgentCode) {
		super.save(jfAgentCode);
	}

	@Transactional(readOnly = false)
	public void batchSave(JfAgentCode jfAgentCode, Integer codeNumber) {
		JfAgentUserItem userItem = new JfAgentUserItem();
		userItem.setUserId(UserUtils.getUser().getId());
		userItem.setItemId(jfAgentCode.getItemId());
		List<JfAgentUserItem> userItems = jfAgentUserItemService.findList(userItem);
		if (userItems == null || userItems.size() <= 0) {
			throw new RuntimeException("授权码生成失败，暂无代理权限！");
		}
		userItem = userItems.get(0);
		if (userItem.getLinesNotUse() < codeNumber) {
			throw new RuntimeException("授权码生成失败，额度不足，剩余额度为：" + userItem.getLinesNotUse());
		}
		Date newDate = new Date();
		jfAgentCode.setUser(UserUtils.getUser());
		jfAgentCode.setAgentType("1");
		jfAgentCode.setCreateDate(newDate);
		//计算到期时间
		//jfAgentCode.setValidDate(DateUtils.addDays(newDate, jfAgentCode.getCodeType()));
		List<String> codeList = Lists.newArrayList();
		JfAgentItem item = jfAgentItemDao.get(jfAgentCode.getItemId());
		if (item == null || StringUtils.isBlank(item.getSerial())) {
			throw new RuntimeException("授权码生成失败，项目不存在！");
		}
		// ——————————授权码生成规则————————————————
		String createType = item.getCodeFormat();
		String prefix = item.getCodeFormatPrefix();
		Integer prefixLength = StringUtils.isNotEmpty(prefix) ? prefix.length() : 0;

		String idGenStr = null;
        Integer idGenLength = 0;
        switch (createType){
			case  "1" :
				idGenStr = IdGen.ID_GEN_AF_09;
                idGenLength = 32;
                break;
			case "2" :
				idGenStr = IdGen.ID_GEN_AZ_09;
                idGenLength = 16;
                break;
			default:
				break;
		}
        prefix = StringUtils.isNotEmpty(prefix) ? prefix : "";
        for (int i = 0 ; i < codeNumber; i++) {
            String code = prefix + IdGen.randomUuId(idGenStr, idGenLength - prefixLength);
            codeList.add(code);
		}
		jfAgentCode.setCodeList(codeList);
		dao.batchSave(jfAgentCode);//批量生成

		//更新用户额度
		jfAgentUserItemService.updateItemUseLines(userItem, codeNumber);
	}
	
	@Transactional(readOnly = false)
	public void delete(JfAgentCode jfAgentCode) {
		super.delete(jfAgentCode);
		JfAgentUserItem userItem = new JfAgentUserItem();
		userItem.setUserId(UserUtils.getUser().getId());
		userItem.setItemId(jfAgentCode.getItemId());
		List<JfAgentUserItem> userItems = jfAgentUserItemService.findList(userItem);
		userItem = userItems.get(0);
		//更新额度
		jfAgentUserItemService.updateItemUseLines(userItem, -1);
	}


	public List<AgentCodeTypeGroup> queryCodeGroupByType(JfAgentCode jfAgentCode){
		return dao.queryCodeGroupByType(jfAgentCode);
	}

	public List<AgentCodeTypeGroup> queryCodeGroupByType(String itemId){
		return queryCodeGroupByType(itemId, null);
	}

	public List<AgentCodeTypeGroup> queryCodeGroupByType(String itemId, Integer codeType) {
		JfAgentCode jfAgentCode = new JfAgentCode();
		jfAgentCode.setItemId(itemId);
		jfAgentCode.setCodeType(codeType);
		return queryCodeGroupByType(jfAgentCode);
	}
}