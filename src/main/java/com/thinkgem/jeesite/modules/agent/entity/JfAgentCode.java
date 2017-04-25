/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.entity;

import com.jfang.authorization.repository.UserModelRepository;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.ResultUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.red.utils.AgentJedisUtils;
import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.springframework.http.MediaType;

/**
 * 授权码Entity
 * @author jfang
 * @version 2017-03-04
 */
public class JfAgentCode extends DataEntity<JfAgentCode> implements UserModelRepository<JfAgentCode> {
	
	private static final long serialVersionUID = 1L;
	private String creditCode;		// 授权码
	private String machineCode;		// 机器码
	private String agentType;		// 授权方式标识
	private String isUseable;		// 是否可用
	private String isUse;		// 是否已用
	private User user;		// 代理用户
	private String itemId;		// item_id
	private String userCode;		// user_code
	private Date authorDate;		// 授权时间
	private Date validDate;		// 有效时间
	private Date laseDate;		// lase_date
	private String rank;		// rank
	private Integer codeType;
	private String config;

	private List<String> codeList;
	
	public JfAgentCode() {
		super();
	}

	public JfAgentCode(String id){
		super(id);
	}

	@Length(min=0, max=64, message="授权码长度必须介于 0 和 64 之间")
	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	
	@Length(min=0, max=64, message="机器码长度必须介于 0 和 64 之间")
	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}
	
	@Length(min=1, max=1, message="授权方式标识长度必须介于 1 和 1 之间")
	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	
	@Length(min=1, max=1, message="是否可用长度必须介于 1 和 1 之间")
	public String getIsUseable() {
		return isUseable;
	}

	public void setIsUseable(String isUseable) {
		this.isUseable = isUseable;
	}
	
	@Length(min=1, max=1, message="是否已用长度必须介于 1 和 1 之间")
	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	
	@NotNull(message="代理用户不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=64, message="item_id长度必须介于 0 和 64 之间")
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	@Length(min=0, max=255, message="user_code长度必须介于 0 和 255 之间")
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAuthorDate() {
		return authorDate;
	}

	public void setAuthorDate(Date authorDate) {
		this.authorDate = authorDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLaseDate() {
		return laseDate;
	}

	public void setLaseDate(Date laseDate) {
		this.laseDate = laseDate;
	}
	
	@Length(min=0, max=1, message="rank长度必须介于 0 和 1 之间")
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	@Length(min=0, max=1000, message="config长度必须介于 0 和 1000 之间")
	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public List<String> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
	}

	@NotNull(message="授权码类型不能为空")
	public Integer getCodeType() {
		return codeType;
	}

	public void setCodeType(Integer codeType) {
		this.codeType = codeType;
	}


	@Override
	public JfAgentCode getCurrentUser(String key) {
		//JfAgentCode agentCode = AccreditUtils.get(key);
		JfAgentCode agentCode = AgentJedisUtils.get(key);
		return agentCode;
	}

	@Override
	public JfAgentCode isVail(String key,HttpServletResponse response) {
		JfAgentCode agentCode = getCurrentUser(key);
		String message = AgentJedisUtils.verifyCode(agentCode);
		if (StringUtils.isNotEmpty(message)) {
			Map<String, Object> result = ResultUtils.getFailure(message);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
				writer.write(JsonMapper.toJsonString(result));
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return agentCode;
	}
}