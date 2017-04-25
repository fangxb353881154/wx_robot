/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.entity;


import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.red.utils.EmojiUtils;
import com.thinkgem.jeesite.modules.red.vo.ConfigJson;

/**
 * wx群配置Entity
 * @author jfang
 * @version 2017-04-12
 */
public class RedConfig extends DataEntity<RedConfig> {
	
	private static final long serialVersionUID = 1L;
	private String configJsonStr;		// 配置JSON字符串
	
	public RedConfig() {
		super();
	}

	public RedConfig(String id){
		super(id);
	}

	public String getConfigJsonStr() {
		return configJsonStr;
	}

	public void setConfigJsonStr(String configJsonStr) {
		this.configJsonStr = configJsonStr;
	}

	public ConfigJson getConfigJson() {
//		String jsonString = EmojiUtils.emojiRecovery(getConfigJsonStr());
		return (ConfigJson) JsonMapper.fromJsonString(getConfigJsonStr(), ConfigJson.class);
	}
}