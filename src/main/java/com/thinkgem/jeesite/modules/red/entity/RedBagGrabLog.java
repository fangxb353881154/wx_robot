/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.Date;

/**
 * 抢包手金额记录Entity
 * @author jfang
 * @version 2017-04-16
 */
public class RedBagGrabLog extends DataEntity<RedBagGrabLog> {
	
	private static final long serialVersionUID = 1L;
	private String creditcode;		// creditcode
	private String userName;		// 抢包手wxID
	private String receiveName;		// 抢包手昵称
	private Integer receiveAmount;		// 抢包金额
	private Integer avoidAmount;		// 免死金额
	private Integer receiveNum;		// 抢包次数
	private Integer avoidNum;		// 免死次数
	private String roomNum;

	private Date startDate;
	private Date endDate;
	
	public RedBagGrabLog() {
		super();
		receiveAmount = 0;
		receiveNum = 0;
		avoidAmount = 0;
		avoidNum = 0;
	}

	public RedBagGrabLog(String id){
		super(id);
	}

	@JsonIgnore
	@Length(min=1, max=64, message="creditcode长度必须介于 1 和 64 之间")
	public String getCreditcode() {
		return creditcode;
	}

	public void setCreditcode(String creditcode) {
		this.creditcode = creditcode;
	}

	@JsonIgnore
	@Length(min=0, max=45, message="抢包手wxID长度必须介于 0 和 45 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=0, max=45, message="抢包手昵称长度必须介于 0 和 45 之间")
	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	
	public Integer getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(Integer receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
	
	public Integer getAvoidAmount() {
		return avoidAmount;
	}

	public void setAvoidAmount(Integer avoidAmount) {
		this.avoidAmount = avoidAmount;
	}
	
	public Integer getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(Integer receiveNum) {
		this.receiveNum = receiveNum;
	}
	
	public Integer getAvoidNum() {
		return avoidNum;
	}

	public void setAvoidNum(Integer avoidNum) {
		this.avoidNum = avoidNum;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@JsonIgnore
	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
}