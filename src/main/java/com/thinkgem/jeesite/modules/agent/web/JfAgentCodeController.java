/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentItem;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUser;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUserItem;
import com.thinkgem.jeesite.modules.agent.service.JfAgentItemService;
import com.thinkgem.jeesite.modules.agent.service.JfAgentUserItemService;
import com.thinkgem.jeesite.modules.agent.service.JfAgentUserService;
import com.thinkgem.jeesite.modules.agent.vo.AgentCodeTypeGroup;
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
import com.thinkgem.jeesite.modules.agent.entity.JfAgentCode;
import com.thinkgem.jeesite.modules.agent.service.JfAgentCodeService;

import java.util.Date;
import java.util.List;

/**
 * 授权码Controller
 * @author jfang
 * @version 2017-03-04
 */
@Controller
@RequestMapping(value = "${adminPath}/agent/jfAgentCode")
public class JfAgentCodeController extends BaseController {

	@Autowired
	private JfAgentCodeService jfAgentCodeService;
	@Autowired
	private JfAgentUserItemService jfAgentUserItemService;
	@Autowired
	private JfAgentUserService jfAgentUserService;

	
	@ModelAttribute
	public JfAgentCode get(@RequestParam(required=false) String id) {
		JfAgentCode entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jfAgentCodeService.get(id);
		}
		if (entity == null){
			entity = new JfAgentCode();
		}
		return entity;
	}
	
	@RequiresPermissions("agent:jfAgentCode:view")
	@RequestMapping(value = {"list", ""})
	public String list(JfAgentCode jfAgentCode, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (StringUtils.isBlank(jfAgentCode.getItemId())) {
			throw new RuntimeException("未知错误，请联系管理员！");
		}
		jfAgentCode.setUser(UserUtils.getUser());
		List<AgentCodeTypeGroup> codeTypeGroups = jfAgentCodeService.queryCodeGroupByType(jfAgentCode);
		model.addAttribute("codeTypeGroups", codeTypeGroups);

		Page<JfAgentCode> page = jfAgentCodeService.findPage(new Page<JfAgentCode>(request, response), jfAgentCode);
		model.addAttribute("page", page);

		return "modules/agent/jfAgentCodeList";
	}

	@RequiresPermissions("agent:jfAgentCode:view")
	@RequestMapping(value = "form")
	public String form(JfAgentCode jfAgentCode, Model model) {
		JfAgentUserItem userItem = new JfAgentUserItem();
		userItem.setUserId(UserUtils.getUser().getId());
		userItem.setItemId(jfAgentCode.getItemId());
		List<JfAgentUserItem> userItems = jfAgentUserItemService.findList(userItem);
		if (userItems == null || userItems.size() <= 0) {
			addMessage(model, "生成失败，暂无代理权限！");
			return "redirect:" + Global.getAdminPath() + "/agent/jfAgentCode/list?itemId=" + jfAgentCode.getItemId();
		}
		model.addAttribute("jfAgentUserItem", userItems.get(0));
		model.addAttribute("jfAgentCode", jfAgentCode);
		return "modules/agent/jfAgentCodeForm";
	}

	@RequiresPermissions("agent:jfAgentCode:edit")
	@RequestMapping(value = "save")
	public String save(JfAgentCode jfAgentCode,@RequestParam(required = true, defaultValue = "1") Integer codeNumber, Model model, RedirectAttributes redirectAttributes) {
		try {
			jfAgentCodeService.batchSave(jfAgentCode, codeNumber);
			addMessage(redirectAttributes, "保存授权码成功");
			return "redirect:" + Global.getAdminPath() + "/agent/jfAgentCode/list?itemId=" + jfAgentCode.getItemId();
		} catch (Exception e) {
			addMessage(model, e.getMessage());
			return form(jfAgentCode, model);
		}
	}

	@RequiresPermissions("agent:jfAgentCode:edit")
	@RequestMapping(value = "usable")
	public String usable(JfAgentCode jfAgentCode, Model model, RedirectAttributes redirectAttributes) {
		//切换状态
		jfAgentCode.setIsUseable(StringUtils.equals(jfAgentCode.getIsUseable(), "1") ? "0" : "1");
		jfAgentCodeService.save(jfAgentCode);
		addMessage(redirectAttributes, "修改授权码状态成功");
		return "redirect:" + Global.getAdminPath() + "/agent/jfAgentCode/list?itemId=" + jfAgentCode.getItemId();
	}

	@RequiresPermissions("agent:jfAgentCode:edit")
	@RequestMapping(value = "delete")
	public String delete(JfAgentCode jfAgentCode, RedirectAttributes redirectAttributes) {
		if (StringUtils.equals(jfAgentCode.getIsUse(), "1")) {
			//已使用授权码删除时，判断是否可以删除 -> 用户有效期限
			JfAgentUser agentUser = jfAgentUserService.get(UserUtils.getUser().getId());
			int useHour = StringUtils.isNotEmpty(agentUser.getUseHour()) ? Integer.parseInt(agentUser.getUseHour()) : 0;
			if (new Date().getTime() - jfAgentCode.getAuthorDate().getTime() > useHour * DateUtils.MILLIS_PER_MINUTE) {
				addMessage(redirectAttributes, "删除授权码失败，授权时间过长！");
				return "redirect:" + Global.getAdminPath() + "/agent/jfAgentCode/list?itemId=" + jfAgentCode.getItemId();
			}
		}
		jfAgentCodeService.delete(jfAgentCode);
		addMessage(redirectAttributes, "删除授权码成功");
		return "redirect:" + Global.getAdminPath() + "/agent/jfAgentCode/list?itemId=" + jfAgentCode.getItemId();
	}

	@RequiresPermissions("agent:jfAgentCode:edit")
	@RequestMapping(value = "renewalCode")
	public String renewalCode(JfAgentCode jfAgentCode,Integer renewalNumber, RedirectAttributes redirectAttributes) {
		if (renewalNumber > 0 && StringUtils.equals(jfAgentCode.getIsUse(), "1")) {
			//已授权的码才可以续费
			Date validDate = (jfAgentCode.getValidDate().getTime() - new Date().getTime() > 0) ? jfAgentCode.getValidDate() : new Date();
			validDate = DateUtils.addDays(validDate, renewalNumber * 31);//每个月按31天算
			jfAgentCode.setValidDate(validDate);
			jfAgentCodeService.save(jfAgentCode);
			addMessage(redirectAttributes, "授权码续费成功，有效期至" + DateUtils.formatDateTime(jfAgentCode.getValidDate()));
		} else {
			addMessage(redirectAttributes, "授权码续费失败");
		}
		return "redirect:" + Global.getAdminPath() + "/agent/jfAgentCode/list?itemId=" + jfAgentCode.getItemId();
	}

}