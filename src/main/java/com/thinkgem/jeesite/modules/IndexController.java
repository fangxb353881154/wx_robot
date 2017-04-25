package com.thinkgem.jeesite.modules;

import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentCode;
import com.thinkgem.jeesite.modules.agent.service.JfAgentCodeService;
import com.thinkgem.jeesite.modules.red.utils.EmojiUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by jfang on 2017/4/16.
 */
@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    private JfAgentCodeService jfAgentCodeService;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        /*JedisUtils.set("key", "value12312", 5);
        System.out.println("=====设置======");

        System.out.println("=======获取=======");
        System.out.println(JedisUtils.get("key"));*/
        return "/index";
    }

    @RequestMapping(value = "index", method = RequestMethod.POST)
    public String index(@RequestParam String creditCode, String vCode, HttpServletRequest request, Model model) {
        System.out.println(creditCode);

        String message = "";
        model.addAttribute("creditCode", creditCode);
        try {
            if (StringUtils.isEmpty(vCode)) {
                throw new RuntimeException("请输入验证码！");
            }
            HttpSession session = request.getSession();
            String code = (String) session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
            if (!code.toLowerCase().equals(vCode.toLowerCase())) {
                throw new RuntimeException("验证码错误, 请重试.");
            }
            JfAgentCode agentCode = jfAgentCodeService.get(creditCode);
            if (agentCode != null && StringUtils.equals(agentCode.getIsUseable(), "1")) {
                if (StringUtils.equals(agentCode.getIsUse(), "1")) {
                    message = "授权码已被使用！";
                } else {
                    message = "授权码可使用";
                }
            } else {
                message = "非官方授权码！";
            }
        } catch (Exception e) {
            message = e.getMessage();
        }
        model.addAttribute("message", message);
        return "/index";
    }
}
