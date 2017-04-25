/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.utils.BigDecimalUtils;
import com.thinkgem.jeesite.modules.red.dao.RedBagSendLogDao;
import com.thinkgem.jeesite.modules.red.dao.RedGameTotalUpDownDao;
import com.thinkgem.jeesite.modules.red.utils.EmojiUtils;
import com.thinkgem.jeesite.modules.red.vo.RedBagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.red.entity.RedGameTotal;
import com.thinkgem.jeesite.modules.red.dao.RedGameTotalDao;

/**
 * 玩家积分Service
 * @author jfang
 * @version 2017-04-22
 */
@Service
@Transactional(readOnly = true)
public class RedGameTotalService extends CrudService<RedGameTotalDao, RedGameTotal> {

	@Autowired
	private RedGameTotalUpDownDao redGameTotalUpDownDao;
	@Autowired
	private RedBagSendLogDao redBagSendLogDao;

	public RedGameTotal get(String id) {
		return super.get(id);
	}
	
	public List<RedGameTotal> findList(RedGameTotal redGameTotal) {
		return super.findList(redGameTotal);
	}
	
	public Page<RedGameTotal> findPage(Page<RedGameTotal> page, RedGameTotal redGameTotal) {
		return super.findPage(page, redGameTotal);
	}
	
	@Transactional(readOnly = false)
	public void save(RedGameTotal redGameTotal) {
		super.save(redGameTotal);
	}

	@Transactional(readOnly = false)
	public void delete(RedGameTotal redGameTotal) {
		super.delete(redGameTotal);
	}

	@Transactional(readOnly = false)
	public void deleteByRoomNum(Map<String,String> param) {
		dao.deleteByRoomNum(param);

		redGameTotalUpDownDao.deleteByRoomNum(param);

		redBagSendLogDao.deleteByRoomNum(param);
	}

	@Transactional(readOnly = false)
	public void updateGameTotal(RedBagVo redBagVo) {
		if (redBagVo.getLossRatioAmountCount() > 0 || redBagVo.getRwardCount() > 0) {
			RedGameTotal gameTotal = new RedGameTotal();
			//根据群号，授权码, 用户名 查询用户积分
			gameTotal.setRoomNum(redBagVo.getRoomNum());
			gameTotal.setCreditCode(redBagVo.getCreditCode());
			gameTotal.setSendUserNick(redBagVo.getSendNick());
			List<RedGameTotal> totals = findList(gameTotal);
			if (totals != null && totals.size() == 1) {
				gameTotal = totals.get(0);
			}
			BigDecimal total =  BigDecimal.valueOf(redBagVo.getLossRatioAmountCount() + redBagVo.getRwardCount());
			total = BigDecimalUtils.dividePoint(total);
			BigDecimal oldTotal = gameTotal.getGameTotal();
			if (oldTotal == null) {
				oldTotal = new BigDecimal(0);
			}
			gameTotal.setGameTotal(total.add(oldTotal));
			save(gameTotal);
		}
	}
}