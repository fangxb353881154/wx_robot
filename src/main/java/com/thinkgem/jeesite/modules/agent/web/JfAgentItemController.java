/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.agent.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.agent.entity.JfAgentItem;
import com.thinkgem.jeesite.modules.agent.service.JfAgentItemService;

/**
 * 授权项目Controller
 * @author jfang
 * @version 2017-03-04
 */
@Controller
@RequestMapping(value = "${adminPath}/agent/jfAgentItem")
public class JfAgentItemController extends BaseController {

	@Autowired
	private JfAgentItemService jfAgentItemService;
	
	@ModelAttribute
	public JfAgentItem get(@RequestParam(required=false) String id) {
		JfAgentItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jfAgentItemService.get(id);
		}
		if (entity == null){
			entity = new JfAgentItem();
		}
		return entity;
	}
	
	@RequiresPermissions("agent:jfAgentItem:view")
	@RequestMapping(value = {"list", ""})
	public String list(JfAgentItem jfAgentItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JfAgentItem> page = jfAgentItemService.findPage(new Page<JfAgentItem>(request, response), jfAgentItem); 
		model.addAttribute("page", page);
		return "modules/agent/jfAgentItemList";
	}

	@RequiresPermissions("agent:jfAgentItem:view")
	@RequestMapping(value = "form")
	public String form(JfAgentItem jfAgentItem, Model model) {
		model.addAttribute("jfAgentItem", jfAgentItem);
		return "modules/agent/jfAgentItemForm";
	}

	@RequiresPermissions("agent:jfAgentItem:edit")
	@RequestMapping(value = "save")
	public String save(JfAgentItem jfAgentItem, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jfAgentItem)){
			return form(jfAgentItem, model);
		}
		jfAgentItemService.save(jfAgentItem);
		addMessage(redirectAttributes, "保存项目成功");
		return "redirect:"+Global.getAdminPath()+"/agent/jfAgentItem/list?repage";
	}

	@RequiresPermissions("agent:jfAgentItem:edit")
	@RequestMapping(value = "usable")
	public String usable(JfAgentItem jfAgentItem, RedirectAttributes redirectAttributes) {
		jfAgentItem.setIsUsable(StringUtils.equals(jfAgentItem.getIsUsable(), "1") ? "0" : "1");//切换状态
		jfAgentItemService.save(jfAgentItem);
		addMessage(redirectAttributes, "操作成功！");
		return "redirect:"+Global.getAdminPath()+"/agent/jfAgentItem/list?repage";
	}

	@RequiresPermissions("agent:jfAgentItem:edit")
	@RequestMapping(value = "delete")
	public String delete(JfAgentItem jfAgentItem, RedirectAttributes redirectAttributes) {
		jfAgentItemService.delete(jfAgentItem);
		addMessage(redirectAttributes, "删除项目成功");
		return "redirect:"+Global.getAdminPath()+"/agent/jfAgentItem/list?repage";
	}

}