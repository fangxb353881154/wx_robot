/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 玩家上下分记录Entity
 * @author jfang
 * @version 2017-04-22
 */
public class RedGameTotalUpDown extends DataEntity<RedGameTotalUpDown> {
	
	private static final long serialVersionUID = 1L;
	private String creditCode;		// 授权码
	private String roomNum;		// 群号
	private String sendUserId;		// 玩家WXID
	private String sendUserNick;		// 玩家昵称
	private BigDecimal total;		// 积分
	private String type;		// 1:上分，2:下分

	public static String TOTAL_TYPE_UP = "1";
	public static String TOTAL_TYPE_DOWM = "2";

	public RedGameTotalUpDown() {
		super();
	}

	public RedGameTotalUpDown(String id){
		super(id);
	}

	@Length(min=0, max=64, message="授权码长度必须介于 0 和 64 之间")
	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	
	@Length(min=0, max=64, message="群号长度必须介于 0 和 64 之间")
	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	
	@Length(min=0, max=100, message="玩家WXID长度必须介于 0 和 100 之间")
	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	
	@Length(min=0, max=100, message="玩家昵称长度必须介于 0 和 100 之间")
	public String getSendUserNick() {
		return sendUserNick;
	}

	public void setSendUserNick(String sendUserNick) {
		this.sendUserNick = sendUserNick;
	}
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	@Length(min=0, max=1, message="1:上分，2:下分长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}