package com.thinkgem.jeesite.modules.agent.web;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Maps;
import com.jfang.authorization.entity.TokenModel;
import com.jfang.authorization.manager.TokenManager;
import com.jfang.authorization.manager.impl.RedisTokenManager;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.ResultUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentApiVo;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentCode;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentItem;
import com.thinkgem.jeesite.modules.agent.service.JfAgentCodeService;
import com.thinkgem.jeesite.modules.agent.service.JfAgentItemService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created by asus on 2017/3/7.
 */
@Controller
@RequestMapping(value = "${adminPath}/agent/api")
public class JfAgentInterfaceController {

    @Autowired
    private JfAgentCodeService jfAgentCodeService;
    @Autowired
    private JfAgentItemService jfAgentItemService;
    private Map<String, Object> resultMap = Maps.newHashMap();

    private JfAgentCode agentCode = new JfAgentCode();

    @ResponseBody
    @RequestMapping(value = " /impowerCode2 ")
    public Map<String, Object> getCode(@RequestParam String key, HttpServletRequest request) {
        JfAgentApiVo agentApiVo = decryptKey(key);
        return ResultUtils.getSuccess();
    }

    /**
     * 授权接口
     *
     * @param key json参数加密后字符串
     * @return
     */
    @RequestMapping(value = "/impowerCode", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> impowerCode( String key, HttpServletRequest request) {
        if (StringUtils.isEmpty(key)) {
            return ResultUtils.getFailure("参数有误！");
        }
        System.out.println(key);
        JfAgentApiVo agentApiVo = decryptKey(key);

        Date newDate = new Date();
        //判断授权项目
        if (!verifyAgentItem(agentApiVo)) {
            return resultMap;
        }

        if (StringUtils.isNotEmpty(agentCode.getMachineCode()) || StringUtils.equals(agentCode.getIsUse(), "1")) {
            //授权码已使用
            if (!StringUtils.equals(agentCode.getMachineCode(), agentApiVo.getUdid())) {
                return ResultUtils.getFailure("授权失败，授权码已被使用！");
            } else if (agentCode.getValidDate().getTime() < newDate.getTime()) {
                return ResultUtils.getFailure("授权码已过期，请续费！");
            }
        } else {
            //首次授权
            agentCode.setMachineCode(agentApiVo.getUdid());
            agentCode.setIsUse("1");
            agentCode.setAuthorDate(newDate);
            agentCode.setValidDate(DateUtils.addHours(newDate, agentCode.getCodeType()));
            //todo 新增一条 群红包详情 to red_group_grab_log
        }
        agentCode.setLaseDate(newDate);
        jfAgentCodeService.save(agentCode);

        //生成toke
        TokenManager tokenManager = new RedisTokenManager();
        TokenModel tokenModel = tokenManager.createToken(agentCode.getId());

        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("starttime", agentCode.getAuthorDate().getTime()/1000);
        dataMap.put("endtime", agentCode.getValidDate().getTime()/1000);
        dataMap.put("project", agentApiVo.getProject());
        dataMap.put("codeType", DictUtils.getDictLabel(agentCode.getCodeType().toString(), "agent_code_type", "无"));
        dataMap.put("token", tokenModel.getToken());
        return ResultUtils.getSuccess("授权成功！", dataMap);
    }

    /**
     * 存储用户配置
     * @param agentApiVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/setUserConfig", method = RequestMethod.POST)
    public Map<String, Object> setUserConfig(JfAgentApiVo agentApiVo) {
        //授权项目、授权码判断
        if (!verifyAgentItem(agentApiVo)) {
            return resultMap;
        }
        if (!StringUtils.equals(agentCode.getMachineCode(), agentApiVo.getUdid())) {
            return ResultUtils.getFailure("非法操作！");
        }
        //保存配置
        agentCode.setConfig(agentApiVo.getConfig());
        jfAgentCodeService.save(agentCode);

        return ResultUtils.getSuccess("保存成功！");
    }


    /**
     * 获取授权用户配置
     * @param agentApiVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUserConfig")
    public Map<String, Object> getUserConfig(JfAgentApiVo agentApiVo) {
        //授权项目、授权码判断
        if (!verifyAgentItem(agentApiVo)) {
            return resultMap;
        }
        if (!StringUtils.equals(agentCode.getMachineCode(), agentApiVo.getUdid())) {
            return ResultUtils.getFailure("非法操作！");
        }

        String config = agentCode.getConfig();
        // DesUtils desUtils = new DesUtils();
        //config = desUtils.encryptString(agentCode.getConfig());
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("endtime", (new Date()).getTime());
        resultMap.put("project", agentApiVo.getProject());
        resultMap.put("config", config);
        return ResultUtils.getSuccess("配置获取成功！", resultMap);
    }

    /**
     * 授权项目、授权码判断
     * @param agentApiVo
     * @return
     */
    private boolean verifyAgentItem(JfAgentApiVo agentApiVo) {
        //空指针判断
        if (StringUtils.isBlank(agentApiVo.getProject()) || !isNotAuthAndUdid(agentApiVo)) {
            resultMap = ResultUtils.getFailure("授权失败！");
            return false;
        }
        JfAgentItem agentItem = jfAgentItemService.getItemBySerial(agentApiVo.getProject());
        if (agentItem == null) {
            resultMap = ResultUtils.getFailure("非官方正版软件！");
            return false;
        }
        if (!StringUtils.equals(agentItem.getIsUsable(), "1")) {
            resultMap = ResultUtils.getFailure("软件已停止授权！");
            return false;
        }
        agentCode = jfAgentCodeService.get(agentApiVo.getAuth());
        if (agentCode == null || !StringUtils.equals(agentCode.getIsUseable(), "1") || !StringUtils.equals(agentCode.getItemId(), agentItem.getId())) {
            //授权码是否存在 OR 授权码是否可用 OR 授权码是否与项目编码对的上
            resultMap = ResultUtils.getFailure("非官方授权码！");
            return false;
        }
        return true;
    }

    /**
     * 判断授权码、机器码是否存在
     * @param agentApiVo
     * @return
     */
    private boolean isNotAuthAndUdid(JfAgentApiVo agentApiVo) {
        return (StringUtils.isNotEmpty(agentApiVo.getAuth()) && StringUtils.isNotEmpty(agentApiVo.getUdid()));
    }

    private JfAgentApiVo decryptKey(String key) {
       /* key = key.replaceAll(" ", "+");
        System.out.println(key);
        DesUtils desUtils = new DesUtils();
        key = desUtils.decryptString(key);*/

        try {
            JfAgentApiVo agentApiVo = (JfAgentApiVo) JsonMapper.fromJsonString(key, JfAgentApiVo.class);
            return agentApiVo;
        } catch (Exception e) {

        }
        return new JfAgentApiVo();
    }

}
