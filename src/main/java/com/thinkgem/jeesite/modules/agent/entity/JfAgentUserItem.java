/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 代理用户项目关联表Entity
 * @author jfang
 * @version 2017-03-04
 */
public class JfAgentUserItem extends DataEntity<JfAgentUserItem> {
	
	private static final long serialVersionUID = 1L;
	private String  userId;		// user_id
	private String itemId;		// item_id
	private Integer linesCount;		// 总额度
	private Integer linesUse;		// 已使用额度
	private Integer linesNotUse;		// 未使用额度

	private JfAgentItem jfAgentItem;
	
	public JfAgentUserItem() {
		super();
		this.linesCount = 0;
		this.linesUse = 0;
		this.linesNotUse = 0;
	}

	public JfAgentUserItem(String id){
		super(id);
	}

	@NotNull(message="user_id不能为空")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=1, max=64, message="item_id长度必须介于 1 和 64 之间")
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public Integer getLinesCount() {
		return linesCount;
	}

	public void setLinesCount(Integer linesCount) {
		this.linesCount = linesCount;
	}
	
	public Integer getLinesUse() {
		return linesUse;
	}

	public void setLinesUse(Integer linesUse) {
		this.linesUse = linesUse;
	}
	
	public Integer getLinesNotUse() {
		return linesNotUse;
	}

	public void setLinesNotUse(Integer linesNotUse) {
		this.linesNotUse = linesNotUse;
	}

	public JfAgentItem getJfAgentItem() {
		return jfAgentItem;
	}

	public void setJfAgentItem(JfAgentItem jfAgentItem) {
		this.jfAgentItem = jfAgentItem;
		this.itemId = jfAgentItem.getId();
	}
}