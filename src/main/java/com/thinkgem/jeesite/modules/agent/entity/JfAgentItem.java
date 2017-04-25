/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 授权项目Entity
 * @author jfang
 * @version 2017-03-04
 */
public class JfAgentItem extends DataEntity<JfAgentItem> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// name
	private String serial;		// 编号
	private String isUsable;		// 是否可用
	private String versionNumber;
	private String codeFormat;        //生成格式 1,2
	private String codeFormatPrefix;	//格式前缀

	private JfAgentUserItem jfAgentUserItem;
	
	public JfAgentItem() {
		super();
		this.jfAgentUserItem = new JfAgentUserItem();
	}

	public JfAgentItem(String id){
		super(id);
	}

	@Length(min=1, max=255, message="name长度必须介于 1 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=10, message="编号长度必须介于 1 和 10 之间")
	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	@Length(min=0, max=1, message="是否可用长度必须介于 0 和 1 之间")
	public String getIsUsable() {
		return isUsable;
	}

	public void setIsUsable(String isUsable) {
		this.isUsable = isUsable;
	}

	@Length(min=1, max=20, message="编号长度必须介于 1 和 20 之间")
	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	@Length(min=0, max=1, message="生成格式长度必须介于 0 和 1 之间")
	public String getCodeFormat() {
		return codeFormat;
	}

	public void setCodeFormat(String codeFormat) {
		this.codeFormat = codeFormat;
	}

	@Length(max=10, message="前缀长度必须介于 0 和 10 之间")
	public String getCodeFormatPrefix() {
		return codeFormatPrefix;
	}

	public void setCodeFormatPrefix(String codeFormatPrefix) {
		this.codeFormatPrefix = codeFormatPrefix;
	}

	public JfAgentUserItem getJfAgentUserItem() {
		return jfAgentUserItem;
	}

	public void setJfAgentUserItem(JfAgentUserItem jfAgentUserItem) {
		this.jfAgentUserItem = jfAgentUserItem;
	}


}