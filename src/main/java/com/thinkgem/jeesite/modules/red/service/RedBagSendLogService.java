/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.red.dao.RedBagGrabLogDao;
import com.thinkgem.jeesite.modules.red.entity.RedBagGrabLog;
import com.thinkgem.jeesite.modules.red.entity.RedGameTotal;
import com.thinkgem.jeesite.modules.red.utils.EmojiUtils;
import com.thinkgem.jeesite.modules.red.vo.RedBagRecordVo;
import com.thinkgem.jeesite.modules.red.vo.RedBagSendGroupVo;
import com.thinkgem.jeesite.modules.red.vo.RedBagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.red.entity.RedBagSendLog;
import com.thinkgem.jeesite.modules.red.dao.RedBagSendLogDao;

/**
 * 发包详情Service
 * @author jfang
 * @version 2017-04-13
 */
@Service
@Transactional(readOnly = true)
public class RedBagSendLogService extends CrudService<RedBagSendLogDao, RedBagSendLog> {

	@Autowired
	private RedBagGrabLogService redBagGrabLogService;
	@Autowired
	private RedGameTotalService redGameTotalService;

	public RedBagSendLog get(String id) {
		return super.get(id);
	}
	
	public List<RedBagSendLog> findList(RedBagSendLog redBagSendLog) {
		return super.findList(redBagSendLog);
	}
	
	public Page<RedBagSendLog> findPage(Page<RedBagSendLog> page, RedBagSendLog redBagSendLog) {
		return super.findPage(page, redBagSendLog);
	}
	
	@Transactional(readOnly = false)
	public void save(RedBagSendLog redBagSendLog) {
		super.save(redBagSendLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(RedBagSendLog redBagSendLog) {
		super.delete(redBagSendLog);
	}

	public List<RedBagSendGroupVo> querySendGroup(RedBagSendLog redBagSendLog) {
		return dao.querySendGroup(redBagSendLog);
	}

	@Transactional(readOnly = false)
	public void saveBag(RedBagVo redBagVo) {
		this.saveBagSend(redBagVo);
		//更新抢包手金额
		redBagGrabLogService.saveBagGrab(redBagVo);
		//更新积分
		redGameTotalService.updateGameTotal(redBagVo);
	}

	/**
	 * 保存发包记录
	 * @param redBagVo
	 */
	@Transactional(readOnly = false)
	private void saveBagSend(RedBagVo redBagVo) {
		int lossRatioAmount = (int) (redBagVo.getTotalAmount() * redBagVo.getLossRatioCount());
		RedBagSendLog sendLog = new RedBagSendLog();
		sendLog.setCreateDate(new Date());
		sendLog.setIsNewRecord(true);
		sendLog.setId(redBagVo.getSendId());
		sendLog.setRoomNum(redBagVo.getRoomNum());
		sendLog.setCreditCode(redBagVo.getCreditCode());
		sendLog.setSendUserId(redBagVo.getSendUserName());
		sendLog.setSendUserNick(redBagVo.getSendNick());
		sendLog.setTotalAmount(redBagVo.getTotalAmount());
		sendLog.setTotalNum(redBagVo.getTotalNum());
		sendLog.setWishing(redBagVo.getWishing());
		sendLog.setRayValue(redBagVo.getRayValue());
		sendLog.setRayType(redBagVo.getBagType());    //玩法类型
		sendLog.setRwardAmountCount(redBagVo.getRwardCount());
		sendLog.setLossRatioAmountCount(lossRatioAmount);
		sendLog.setLossRatio(redBagVo.getLossRatioCount());   //总赔付倍率
		sendLog.setRecord(JsonMapper.toJsonString(redBagVo.getRecord()));
		sendLog.setLotteryResult(redBagVo.getLotteryResult());

		dao.insert(sendLog);
		//防止重复提交
		JedisUtils.set(sendLog.getCreditCode() + "_" + sendLog.getId(), sendLog.getId(), 5);
	}

}