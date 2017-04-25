/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.agent.entity.JfAgentItem;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUserItem;
import com.thinkgem.jeesite.modules.agent.service.JfAgentItemService;
import com.thinkgem.jeesite.modules.agent.service.JfAgentUserItemService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUser;
import com.thinkgem.jeesite.modules.agent.service.JfAgentUserService;

import java.util.List;

/**
 * 代理用户Controller
 *
 * @author jfang
 * @version 2017-03-04
 */
@Controller
@RequestMapping(value = "${adminPath}/agent/jfAgentUser")
public class JfAgentUserController extends BaseController {

	@Autowired
	private JfAgentUserService jfAgentUserService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private JfAgentItemService jfAgentItemService;
	@Autowired
	private JfAgentUserItemService jfAgentUserItemService;



	@ModelAttribute

	public JfAgentUser get(@RequestParam(required = false) String id) {
		JfAgentUser entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = jfAgentUserService.get(id);
			entity.setUser(UserUtils.get(id));
		}
		if (entity == null) {
			entity = new JfAgentUser();
			entity.setUser(new User());
		}
		return entity;
	}

	@RequiresPermissions("agent:jfAgentUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(JfAgentUser jfAgentUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JfAgentUser> page = jfAgentUserService.findPage(new Page<JfAgentUser>(request, response), jfAgentUser);
		model.addAttribute("page", page);
		return "modules/agent/jfAgentUserList";
	}

	@RequiresPermissions("agent:jfAgentUser:view")
	@RequestMapping(value = "form")
	public String form(JfAgentUser jfAgentUser, Model model) {
		JfAgentItem agentItem = new JfAgentItem();
		if (StringUtils.isNotEmpty(jfAgentUser.getId())) {
			agentItem.getJfAgentUserItem().setUserId(jfAgentUser.getId());
		}
		List<JfAgentItem> itemList = jfAgentItemService.findAllList(agentItem);
		model.addAttribute("itemList", itemList);
		model.addAttribute("jfAgentUser", jfAgentUser);
		return "modules/agent/jfAgentUserForm";
	}

	@RequiresPermissions("agent:jfAgentUser:edit")
	@RequestMapping(value = "save")
	public String save(JfAgentUser jfAgentUser, Model model, RedirectAttributes redirectAttributes) {
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(jfAgentUser.getNewPassword())) {
			jfAgentUser.getUser().setPassword(SystemService.entryptPassword(jfAgentUser.getNewPassword()));
		}
		if (StringUtils.isBlank(jfAgentUser.getId()) && !"true".equals(checkLoginName(jfAgentUser.getUser().getLoginName()))) {
			addMessage(model, "保存用户'" + jfAgentUser.getLoginName() + "'失败，登录名已存在");
			return form(jfAgentUser, model);
		}
		if (!beanValidator(model, jfAgentUser)) {
			return form(jfAgentUser, model);
		}

		jfAgentUserService.save(jfAgentUser);
		addMessage(redirectAttributes, "保存代理用户成功");
		return "redirect:" + Global.getAdminPath() + "/agent/jfAgentUser/?repage";
	}

	@RequiresPermissions("agent:jfAgentUser:edit")
	@RequestMapping(value = "usable")
	public String usable(JfAgentUser jfAgentUser, RedirectAttributes redirectAttributes) {
		jfAgentUser.setIsUsable(StringUtils.equals(jfAgentUser.getIsUsable(), "1") ? "0" : "1");//切换状态
		jfAgentUserService.usable(jfAgentUser);
		addMessage(redirectAttributes, "代理用户状态更新成功");
		return "redirect:" + Global.getAdminPath() + "/agent/jfAgentUser/?repage";
	}

	@RequiresPermissions("agent:jfAgentUser:edit")
	@RequestMapping(value = "delete")
	public String delete(JfAgentUser jfAgentUser, RedirectAttributes redirectAttributes) {
		jfAgentUserService.delete(jfAgentUser);
		addMessage(redirectAttributes, "删除代理用户成功");
		return "redirect:" + Global.getAdminPath() + "/agent/jfAgentUser/?repage";
	}

	public String checkLoginName(String loginName) {
		if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}
}