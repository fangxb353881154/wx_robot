/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.Date;

/**
 * 发包详情Entity
 * @author jfang
 * @version 2017-04-13
 */
public class RedBagSendLog extends DataEntity<RedBagSendLog> {
	
	private static final long serialVersionUID = 1L;
	private String creditCode;		// 授权码
	private String roomNum;			//群号
	private String sendUserId;		// wxID
	private String sendUserNick;		// 昵称
	private Integer totalAmount;		// total_amount
	private Integer totalNum;		// total_num
	private String wishing;		// wishing
	private String rayValue;		// ray_value
	private Integer rayType;		//玩法类型
	private Integer lossRatioAmountCount;		// 赔付金额
	private Double lossRatio;			//赔付倍率
	private String lotteryResult;		//开奖结果
	private Integer rwardAmountCount;		// 奖励总金额
	private String record;		// 抢包详情json

	private Date startDate;
	private Date endDate;


	public RedBagSendLog() {
		super();
	}

	public RedBagSendLog(String id){
		super(id);
	}

	@JsonIgnore
	@Length(min=0, max=64, message="授权码长度必须介于 0 和 64 之间")
	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	@JsonIgnore
	@Length(min=0, max=64, message="群号长度必须介于 0 和 100 之间")
	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	@JsonIgnore
	@Length(min=0, max=100, message="wxID长度必须介于 0 和 100 之间")
	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	
	@Length(min=0, max=100, message="昵称长度必须介于 0 和 100 之间")
	public String getSendUserNick() {
		return sendUserNick;
	}

	public void setSendUserNick(String sendUserNick) {
		this.sendUserNick = sendUserNick;
	}
	
	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	
	@Length(min=0, max=45, message="wishing长度必须介于 0 和 45 之间")
	public String getWishing() {
		return wishing;
	}

	public void setWishing(String wishing) {
		this.wishing = wishing;
	}
	
	@Length(min=0, max=10, message="ray_value长度必须介于 0 和 10 之间")
	public String getRayValue() {
		return rayValue;
	}

	public int getRayType() {
		return rayType;
	}

	public void setRayType(Integer rayType) {
		this.rayType = rayType;
	}

	public Double getLossRatio() {
		return lossRatio;
	}

	public void setLossRatio(Double lossRatio) {
		this.lossRatio = lossRatio;
	}

	public String getLotteryResult() {
		return lotteryResult;
	}

	public void setLotteryResult(String lotteryResult) {
		this.lotteryResult = lotteryResult;
	}

	public void setRayValue(String rayValue) {
		this.rayValue = rayValue;
	}
	
	public Integer getLossRatioAmountCount() {
		return lossRatioAmountCount;
	}

	public void setLossRatioAmountCount(Integer lossRatioAmountCount) {
		this.lossRatioAmountCount = lossRatioAmountCount;
	}
	
	public Integer getRwardAmountCount() {
		return rwardAmountCount;
	}

	public void setRwardAmountCount(Integer rwardAmountCount) {
		this.rwardAmountCount = rwardAmountCount;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	@JsonIgnore
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonIgnore
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}