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
import com.thinkgem.jeesite.modules.red.entity.RedGameTotal;
import com.thinkgem.jeesite.modules.red.service.RedGameTotalService;

/**
 * 玩家积分Controller
 * @author jfang
 * @version 2017-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/red/redGameTotal")
public class RedGameTotalController extends BaseController {

	@Autowired
	private RedGameTotalService redGameTotalService;
	
	@ModelAttribute
	public RedGameTotal get(@RequestParam(required=false) String id) {
		RedGameTotal entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = redGameTotalService.get(id);
		}
		if (entity == null){
			entity = new RedGameTotal();
		}
		return entity;
	}
	
	@RequiresPermissions("red:redGameTotal:view")
	@RequestMapping(value = {"list", ""})
	public String list(RedGameTotal redGameTotal, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RedGameTotal> page = redGameTotalService.findPage(new Page<RedGameTotal>(request, response), redGameTotal); 
		model.addAttribute("page", page);
		return "modules/red/redGameTotalList";
	}

	@RequiresPermissions("red:redGameTotal:view")
	@RequestMapping(value = "form")
	public String form(RedGameTotal redGameTotal, Model model) {
		model.addAttribute("redGameTotal", redGameTotal);
		return "modules/red/redGameTotalForm";
	}

	@RequiresPermissions("red:redGameTotal:edit")
	@RequestMapping(value = "save")
	public String save(RedGameTotal redGameTotal, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, redGameTotal)){
			return form(redGameTotal, model);
		}
		redGameTotalService.save(redGameTotal);
		addMessage(redirectAttributes, "保存玩家积分成功");
		return "redirect:"+Global.getAdminPath()+"/red/redGameTotal/?repage";
	}
	
	@RequiresPermissions("red:redGameTotal:edit")
	@RequestMapping(value = "delete")
	public String delete(RedGameTotal redGameTotal, RedirectAttributes redirectAttributes) {
		redGameTotalService.delete(redGameTotal);
		addMessage(redirectAttributes, "删除玩家积分成功");
		return "redirect:"+Global.getAdminPath()+"/red/redGameTotal/?repage";
	}

}