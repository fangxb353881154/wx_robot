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
import com.thinkgem.jeesite.modules.red.entity.RedBagGrabLog;
import com.thinkgem.jeesite.modules.red.service.RedBagGrabLogService;

/**
 * 抢包手金额记录Controller
 * @author jfang
 * @version 2017-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/red/redBagGrabLog")
public class RedBagGrabLogController extends BaseController {

	@Autowired
	private RedBagGrabLogService redBagGrabLogService;
	
	@ModelAttribute
	public RedBagGrabLog get(@RequestParam(required=false) String id) {
		RedBagGrabLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = redBagGrabLogService.get(id);
		}
		if (entity == null){
			entity = new RedBagGrabLog();
		}
		return entity;
	}
	
	@RequiresPermissions("red:redBagGrabLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(RedBagGrabLog redBagGrabLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RedBagGrabLog> page = redBagGrabLogService.findPage(new Page<RedBagGrabLog>(request, response), redBagGrabLog); 
		model.addAttribute("page", page);
		return "modules/red/redBagGrabLogList";
	}

	@RequiresPermissions("red:redBagGrabLog:view")
	@RequestMapping(value = "form")
	public String form(RedBagGrabLog redBagGrabLog, Model model) {
		model.addAttribute("redBagGrabLog", redBagGrabLog);
		return "modules/red/redBagGrabLogForm";
	}

	@RequiresPermissions("red:redBagGrabLog:edit")
	@RequestMapping(value = "save")
	public String save(RedBagGrabLog redBagGrabLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, redBagGrabLog)){
			return form(redBagGrabLog, model);
		}
		redBagGrabLogService.save(redBagGrabLog);
		addMessage(redirectAttributes, "保存抢包手金额记录成功");
		return "redirect:"+Global.getAdminPath()+"/red/redBagGrabLog/?repage";
	}
	
	@RequiresPermissions("red:redBagGrabLog:edit")
	@RequestMapping(value = "delete")
	public String delete(RedBagGrabLog redBagGrabLog, RedirectAttributes redirectAttributes) {
		redBagGrabLogService.delete(redBagGrabLog);
		addMessage(redirectAttributes, "删除抢包手金额记录成功");
		return "redirect:"+Global.getAdminPath()+"/red/redBagGrabLog/?repage";
	}

}