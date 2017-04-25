/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.red.web;

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
import com.thinkgem.jeesite.modules.red.entity.RedBagSendLog;
import com.thinkgem.jeesite.modules.red.service.RedBagSendLogService;

/**
 * 发包详情Controller
 * @author jfang
 * @version 2017-04-13
 */
@Controller
@RequestMapping(value = "${adminPath}/red/redBagSendLog")
public class RedBagSendLogController extends BaseController {

	@Autowired
	private RedBagSendLogService redBagSendLogService;
	
	@ModelAttribute
	public RedBagSendLog get(@RequestParam(required=false) String id) {
		RedBagSendLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = redBagSendLogService.get(id);
		}
		if (entity == null){
			entity = new RedBagSendLog();
		}
		return entity;
	}
	
	@RequiresPermissions("red:redBagSendLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(RedBagSendLog redBagSendLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RedBagSendLog> page = redBagSendLogService.findPage(new Page<RedBagSendLog>(request, response), redBagSendLog); 
		model.addAttribute("page", page);
		return "modules/red/redBagSendLogList";
	}

	@RequiresPermissions("red:redBagSendLog:view")
	@RequestMapping(value = "form")
	public String form(RedBagSendLog redBagSendLog, Model model) {
		model.addAttribute("redBagSendLog", redBagSendLog);
		return "modules/red/redBagSendLogForm";
	}

	@RequiresPermissions("red:redBagSendLog:edit")
	@RequestMapping(value = "save")
	public String save(RedBagSendLog redBagSendLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, redBagSendLog)){
			return form(redBagSendLog, model);
		}
		redBagSendLogService.save(redBagSendLog);
		addMessage(redirectAttributes, "保存发包详情成功");
		return "redirect:"+Global.getAdminPath()+"/red/redBagSendLog/?repage";
	}
	
	@RequiresPermissions("red:redBagSendLog:edit")
	@RequestMapping(value = "delete")
	public String delete(RedBagSendLog redBagSendLog, RedirectAttributes redirectAttributes) {
		redBagSendLogService.delete(redBagSendLog);
		addMessage(redirectAttributes, "删除发包详情成功");
		return "redirect:"+Global.getAdminPath()+"/red/redBagSendLog/?repage";
	}

}