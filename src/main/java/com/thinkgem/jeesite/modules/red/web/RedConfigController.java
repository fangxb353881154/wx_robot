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
import com.thinkgem.jeesite.modules.red.entity.RedConfig;
import com.thinkgem.jeesite.modules.red.service.RedConfigService;

/**
 * wx群配置Controller
 * @author jfang
 * @version 2017-04-12
 */
@Controller
@RequestMapping(value = "${adminPath}/red/redConfig")
public class RedConfigController extends BaseController {

	@Autowired
	private RedConfigService redConfigService;
	
	@ModelAttribute
	public RedConfig get(@RequestParam(required=false) String id) {
		RedConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = redConfigService.get(id);
		}
		if (entity == null){
			entity = new RedConfig();
		}
		return entity;
	}
	
	@RequiresPermissions("red:redConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(RedConfig redConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RedConfig> page = redConfigService.findPage(new Page<RedConfig>(request, response), redConfig); 
		model.addAttribute("page", page);
		return "modules/red/redConfigList";
	}

	@RequiresPermissions("red:redConfig:view")
	@RequestMapping(value = "form")
	public String form(RedConfig redConfig, Model model) {
		model.addAttribute("redConfig", redConfig);
		return "modules/red/redConfigForm";
	}

	@RequiresPermissions("red:redConfig:edit")
	@RequestMapping(value = "save")
	public String save(RedConfig redConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, redConfig)){
			return form(redConfig, model);
		}
		redConfigService.save(redConfig);
		addMessage(redirectAttributes, "保存wx群配置成功");
		return "redirect:"+Global.getAdminPath()+"/red/redConfig/?repage";
	}
	
	@RequiresPermissions("red:redConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(RedConfig redConfig, RedirectAttributes redirectAttributes) {
		redConfigService.delete(redConfig);
		addMessage(redirectAttributes, "删除wx群配置成功");
		return "redirect:"+Global.getAdminPath()+"/red/redConfig/?repage";
	}

}