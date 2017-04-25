package com.thinkgem.jeesite.modules.red.utils;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.red.dao.RedConfigDao;
import com.thinkgem.jeesite.modules.red.entity.RedConfig;
import com.thinkgem.jeesite.modules.red.vo.*;

import java.util.List;

/**
 * Created by jfang on 2017/4/12.
 */
public class RedConfigUtils {

    private static RedConfigDao redConfigDao = SpringContextHolder.getBean(RedConfigDao.class);
    private static String CONFIG_CACHE_ID_ = "creditCode_";

    private static int cacheSeconds = 24 * 3600;


    /**
     * 获取雷群配置
     *
     * @param creditCode
     * @return
     */
    public static ConfigJson get(String creditCode) {
        String configStr = JedisUtils.get(CONFIG_CACHE_ID_ + creditCode);

        ConfigJson config = null;
        if (StringUtils.isEmpty(configStr)) {
            RedConfig redConfig = redConfigDao.get(creditCode);
            if (redConfig == null || StringUtils.isEmpty(redConfig.getConfigJsonStr())) {
                return null;
            }
            config = redConfig.getConfigJson();
        } else {
            config = (ConfigJson) JsonMapper.fromJsonString(configStr, ConfigJson.class);
        }
        JedisUtils.set(CONFIG_CACHE_ID_ + creditCode, JsonMapper.toJsonString(config), cacheSeconds);
        return config;
    }

    /**
     * 获取全局配置
     *
     * @param creditCode
     * @return
     */
    public static ConfigOverall getOverall(String creditCode) {
        ConfigJson configJson = get(creditCode);
        if (configJson != null && configJson.getOverall() != null) {
            return configJson.getOverall();
        } else {
            throw new RuntimeException("红包计算失败，暂无配置！");
        }
    }

    /**
     * 单雷配置
     *
     * @param creditCode
     * @return
     */
    public static List<ConfigSingle> getSingleList(String creditCode) {
        ConfigJson config = get(creditCode);
        if (config == null || config.getSingleList() == null) {
            return null;
        }
        return config.getSingleList();
    }

    /**
     * 抢包金额奖励
     *
     * @param creditCode
     * @return
     */
    public static List<ConfigJackpot> getJackpotList(String creditCode) {
        ConfigJson config = get(creditCode);
        if (config == null || config.getJackpotList() == null) {
            return null;
        }
        return config.getJackpotList();
    }


    /**
     * 清除
     *
     * @param creditCode
     */
    public static void clearCache(String creditCode) {
        JedisUtils.delObject(CONFIG_CACHE_ID_ + creditCode);
       /* CacheUtils.remove(CONFIG_CACHE, CONFIG_CACHE_ID_ + creditCode);
        CacheUtils.remove(CONFIG_CACHE, CONFIG_AVOID_LIST_ + creditCode);
        CacheUtils.remove(CONFIG_CACHE, CONFIG_SINGLE_LIST_ + creditCode);
        CacheUtils.remove(CONFIG_CACHE, CONFIG_RWARD_BOTTOM_ + creditCode);
        CacheUtils.remove(CONFIG_CACHE, CONFIG_RWARD_TOP_ + creditCode);
        CacheUtils.remove(CONFIG_CACHE, CONFIG_ROOMNUM_ID_ + creditCode);*/
    }
}
