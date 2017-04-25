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
import com.thinkgem.jeesite.modules.agent.entity.JfAgentUserItem;
import com.thinkgem.jeesite.modules.agent.service.JfAgentUserItemService;

/**
 * 代理用户项目关联表Controller
 * @author jfang
 * @version 2017-03-04
 */
@Controller
@RequestMapping(value = "${adminPath}/agent/jfAgentUserItem")
public class JfAgentUserItemController extends BaseController {

	@Autowired
	private JfAgentUserItemService jfAgentUserItemService;
	
	@ModelAttribute
	public JfAgentUserItem get(@RequestParam(required=false) String id) {
		JfAgentUserItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jfAgentUserItemService.get(id);
		}
		if (entity == null){
			entity = new JfAgentUserItem();
		}
		return entity;
	}
	
	@RequiresPermissions("agent:jfAgentUserItem:view")
	@RequestMapping(value = {"list", ""})
	public String list(JfAgentUserItem jfAgentUserItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JfAgentUserItem> page = jfAgentUserItemService.findPage(new Page<JfAgentUserItem>(request, response), jfAgentUserItem); 
		model.addAttribute("page", page);
		return "modules/agent/jfAgentUserItemList";
	}

	@RequiresPermissions("agent:jfAgentUserItem:view")
	@RequestMapping(value = "form")
	public String form(JfAgentUserItem jfAgentUserItem, Model model) {
		model.addAttribute("jfAgentUserItem", jfAgentUserItem);
		return "modules/agent/jfAgentUserItemForm";
	}

	@RequiresPermissions("agent:jfAgentUserItem:edit")
	@RequestMapping(value = "save")
	public String save(JfAgentUserItem jfAgentUserItem, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jfAgentUserItem)){
			return form(jfAgentUserItem, model);
		}
		jfAgentUserItemService.save(jfAgentUserItem);
		addMessage(redirectAttributes, "保存关联成功");
		return "redirect:"+Global.getAdminPath()+"/agent/jfAgentUserItem/?repage";
	}
	
	@RequiresPermissions("agent:jfAgentUserItem:edit")
	@RequestMapping(value = "delete")
	public String delete(JfAgentUserItem jfAgentUserItem, RedirectAttributes redirectAttributes) {
		jfAgentUserItemService.delete(jfAgentUserItem);
		addMessage(redirectAttributes, "删除关联成功");
		return "redirect:"+Global.getAdminPath()+"/agent/jfAgentUserItem/?repage";
	}

}