/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.service;

import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.red.utils.EmojiUtils;
import com.thinkgem.jeesite.modules.red.vo.RedBagRecordVo;
import com.thinkgem.jeesite.modules.red.vo.RedBagVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.red.entity.RedBagGrabLog;
import com.thinkgem.jeesite.modules.red.dao.RedBagGrabLogDao;

/**
 * 抢包手金额记录Service
 * @author jfang
 * @version 2017-04-16
 */
@Service
@Transactional(readOnly = true)
public class RedBagGrabLogService extends CrudService<RedBagGrabLogDao, RedBagGrabLog> {

	public RedBagGrabLog get(String id) {
		return super.get(id);
	}
	
	public List<RedBagGrabLog> findList(RedBagGrabLog redBagGrabLog) {
		return super.findList(redBagGrabLog);
	}

	public List<RedBagGrabLog> findListBycreditcode(String creditcode) {
		RedBagGrabLog redBagGrabLog = new RedBagGrabLog();
		redBagGrabLog.setCreditcode(creditcode);
		return findList(redBagGrabLog);
	}

	public List<RedBagGrabLog> findListBycreditcodeNewDay(String creditcode) {
		String newDay = DateUtils.getDate();
		RedBagGrabLog redBagGrabLog = new RedBagGrabLog();
		redBagGrabLog.setCreditcode(creditcode);
		redBagGrabLog.setUpdateDate(DateUtils.parseDate(newDay));

		return findList(redBagGrabLog);
	}
	
	public Page<RedBagGrabLog> findPage(Page<RedBagGrabLog> page, RedBagGrabLog redBagGrabLog) {
		return super.findPage(page, redBagGrabLog);
	}
	
	@Transactional(readOnly = false)
	public void save(RedBagGrabLog redBagGrabLog) {
		if (StringUtils.isNotBlank(redBagGrabLog.getId())) {
			redBagGrabLog.setUpdateDate(new Date());
			dao.cumulativeSum(redBagGrabLog);
		}else{
			//新增
			redBagGrabLog.preInsert();
			dao.insert(redBagGrabLog);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(RedBagGrabLog redBagGrabLog) {
		super.delete(redBagGrabLog);
	}

	public  List<RedBagGrabLog> querySendGroup(RedBagGrabLog redBagGrabLog){
		return dao.querySendGroup(redBagGrabLog);
	}

	/**
	 * 保存抢包手记录
	 * @param redBagVo
	 */
	@Transactional(readOnly = false)
	public void saveBagGrab(RedBagVo redBagVo) {
		/**
		 * 保存抢包手数据
		 */
		String newDay = DateUtils.getDate();
		Date newDateDay = DateUtils.parseDate(newDay);

		RedBagGrabLog redBagGrabLog = new RedBagGrabLog();
		redBagGrabLog.setCreditcode(redBagVo.getCreditCode());
		redBagGrabLog.setCreateDate(newDateDay);
		List<RedBagGrabLog> bagGrabList = this.findList(redBagGrabLog);
		/**
		 * 保存抢包手
		 */
		for (RedBagRecordVo redBagRecordVo : redBagVo.getRecord()) {
			RedBagGrabLog bagGrabLog = new RedBagGrabLog();
			if (bagGrabList != null && bagGrabList.size() > 0) {
				for (RedBagGrabLog bag : bagGrabList) {
					//抢包手是否已在数据库存在
					if (StringUtils.equals(bag.getReceiveName(), redBagRecordVo.getReceiveName())) {
						bagGrabLog = bag;
					}
				}
			}
			bagGrabLog.setCreditcode(redBagVo.getCreditCode());
			bagGrabLog.setUserName(redBagRecordVo.getUserName());
			bagGrabLog.setReceiveName(redBagRecordVo.getReceiveName());
			bagGrabLog.setReceiveAmount(redBagRecordVo.getReceiveAmount());
			bagGrabLog.setReceiveName(redBagRecordVo.getReceiveName());
			bagGrabLog.setReceiveNum(1);
			if (redBagRecordVo.isAvoid()) {
				bagGrabLog.setAvoidAmount(redBagRecordVo.getReceiveAmount());
				bagGrabLog.setAvoidNum(1);
			}
			this.save(bagGrabLog);
		}
	}
}