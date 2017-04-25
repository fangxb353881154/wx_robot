package com.thinkgem.jeesite.modules.red.utils;

import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.agent.dao.JfAgentCodeDao;
import com.thinkgem.jeesite.modules.agent.dao.JfAgentItemDao;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentCode;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentItem;

import java.util.Date;

/**
 * Created by jfang on 2017/4/5.
 */
public class AgentJedisUtils {

    public static final String ACCREDIT_CACHE_ID_ = "Jedis_code_";
    private static final int cacheSeconds = 24 * 3600;

    private static JfAgentCodeDao jfAgentCodeDao = SpringContextHolder.getBean(JfAgentCodeDao.class);
    private static JfAgentItemDao jfAgentItemDao = SpringContextHolder.getBean(JfAgentItemDao.class);

    /**
     * 根据授权码获取授权码信息
     * @param code
     * @return
     */
    public static JfAgentCode get(String code){
        JfAgentCode agentCode = (JfAgentCode) JedisUtils.getObject(ACCREDIT_CACHE_ID_ + code);
        if (agentCode == null) {
            agentCode = jfAgentCodeDao.get(code);
            if (agentCode == null) {
                return null;
            }
            JedisUtils.setObject(ACCREDIT_CACHE_ID_ + code, agentCode, cacheSeconds);
        }
        return agentCode;
    }

    /**
     *
     * @param agentCode
     * @return
     */
    public static String verifyCode(JfAgentCode agentCode) {
        String message = "";
        if (agentCode == null) {
            message = "请求参数有误！";
        }else if (!StringUtils.equals(agentCode.getIsUseable(), "1") ) {
            message = "非官方授权码！";
        }else{
            JfAgentItem  agentItem = jfAgentItemDao.get(agentCode.getItemId());
            if (agentItem == null) {
                message = "非官方正版软件！";
            }else if (!StringUtils.equals(agentItem.getIsUsable(), "1")){
                message = "软件已停止授权";
            } else if (agentCode.getValidDate().getTime() < new Date().getTime()) {
                message = "授权码已过期，请续费！";
            }
        }
        return message;
    }
}
