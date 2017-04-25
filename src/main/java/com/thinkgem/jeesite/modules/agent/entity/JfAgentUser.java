/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.List;

/**
 * 代理用户Entity
 * @author jfang
 * @version 2017-03-04
 */
public class JfAgentUser extends DataEntity<JfAgentUser> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// name
	private String isUsable;		// 状态
	private String useHour;		// 试用时长
	private User user;
	private String loginName;// 登录名
	private String newPassword;	// 新密码
	private String loginFlag;	// 是否允许登陆

	private List<JfAgentUserItem> jfAgentUserItems;

	
	public JfAgentUser() {
		super();
	}

	public JfAgentUser(List<JfAgentUserItem> jfAgentUserItems) {
		super();
		this.jfAgentUserItems = jfAgentUserItems;
	}

	public JfAgentUser(String id){
		super(id);
	}

	@Length(min=0, max=255, message="name长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getIsUsable() {
		return isUsable;
	}

	public void setIsUsable(String isUsable) {
		this.isUsable = isUsable;
	}
	
	@Length(min=0, max=11, message="试用时长长度必须介于 0 和 11 之间")
	public String getUseHour() {
		return useHour;
	}

	public void setUseHour(String useHour) {
		this.useHour = useHour;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	public List<JfAgentUserItem> getJfAgentUserItems() {
		return jfAgentUserItems;
	}

	public void setJfAgentUserItems(List<JfAgentUserItem> jfAgentUserItems) {
		this.jfAgentUserItems = jfAgentUserItems;
	}
}