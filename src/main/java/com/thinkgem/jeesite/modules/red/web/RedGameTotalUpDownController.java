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
import com.thinkgem.jeesite.modules.red.entity.RedGameTotalUpDown;
import com.thinkgem.jeesite.modules.red.service.RedGameTotalUpDownService;

/**
 * 玩家上下分记录Controller
 * @author jfang
 * @version 2017-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/red/redGameTotalUpDown")
public class RedGameTotalUpDownController extends BaseController {

	@Autowired
	private RedGameTotalUpDownService redGameTotalUpDownService;
	
	@ModelAttribute
	public RedGameTotalUpDown get(@RequestParam(required=false) String id) {
		RedGameTotalUpDown entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = redGameTotalUpDownService.get(id);
		}
		if (entity == null){
			entity = new RedGameTotalUpDown();
		}
		return entity;
	}
	
	@RequiresPermissions("red:redGameTotalUpDown:view")
	@RequestMapping(value = {"list", ""})
	public String list(RedGameTotalUpDown redGameTotalUpDown, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RedGameTotalUpDown> page = redGameTotalUpDownService.findPage(new Page<RedGameTotalUpDown>(request, response), redGameTotalUpDown); 
		model.addAttribute("page", page);
		return "modules/red/redGameTotalUpDownList";
	}

	@RequiresPermissions("red:redGameTotalUpDown:view")
	@RequestMapping(value = "form")
	public String form(RedGameTotalUpDown redGameTotalUpDown, Model model) {
		model.addAttribute("redGameTotalUpDown", redGameTotalUpDown);
		return "modules/red/redGameTotalUpDownForm";
	}

	@RequiresPermissions("red:redGameTotalUpDown:edit")
	@RequestMapping(value = "save")
	public String save(RedGameTotalUpDown redGameTotalUpDown, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, redGameTotalUpDown)){
			return form(redGameTotalUpDown, model);
		}
		redGameTotalUpDownService.save(redGameTotalUpDown);
		addMessage(redirectAttributes, "保存玩家上下分记录成功");
		return "redirect:"+Global.getAdminPath()+"/red/redGameTotalUpDown/?repage";
	}
	
	@RequiresPermissions("red:redGameTotalUpDown:edit")
	@RequestMapping(value = "delete")
	public String delete(RedGameTotalUpDown redGameTotalUpDown, RedirectAttributes redirectAttributes) {
		redGameTotalUpDownService.delete(redGameTotalUpDown);
		addMessage(redirectAttributes, "删除玩家上下分记录成功");
		return "redirect:"+Global.getAdminPath()+"/red/redGameTotalUpDown/?repage";
	}

}