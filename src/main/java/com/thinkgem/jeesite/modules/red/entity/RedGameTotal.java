/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 玩家积分Entity
 * @author jfang
 * @version 2017-04-22
 */
public class RedGameTotal extends DataEntity<RedGameTotal> {
	
	private static final long serialVersionUID = 1L;
	private String creditCode;		// 授权码
	private String roomNum;		// 群号
	private String sendUserId;		// 玩家wxID
	private String sendUserNick;		// 玩家昵称
	private BigDecimal gameTotal;		// 总积分
	
	public RedGameTotal() {
		super();
	}

	public RedGameTotal(String id){
		super(id);
	}

	@JsonIgnore
	@Length(min=1, max=64, message="授权码长度必须介于 1 和 64 之间")
	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	@Length(min=1, max=64, message="群号长度必须介于 1 和 64 之间")
	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	
	@Length(min=0, max=100, message="玩家wxID长度必须介于 0 和 100 之间")
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
	
	@NotNull(message="总积分不能为空")
	public BigDecimal getGameTotal() {
		return gameTotal;
	}

	public void setGameTotal(BigDecimal gameTotal) {
		this.gameTotal = gameTotal;
	}
	
}