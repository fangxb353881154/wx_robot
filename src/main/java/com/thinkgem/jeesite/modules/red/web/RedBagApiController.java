package com.thinkgem.jeesite.modules.red.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfang.authorization.annotation.Authorization;
import com.jfang.authorization.annotation.CurrentUser;
import com.jfang.authorization.interceptor.AuthorizationInterceptor;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.modules.agent.entity.JfAgentCode;
import com.thinkgem.jeesite.modules.red.entity.*;
import com.thinkgem.jeesite.modules.red.enums.RedBagType;
import com.thinkgem.jeesite.modules.red.service.RedBagGrabLogService;
import com.thinkgem.jeesite.modules.red.service.RedBagSendLogService;
import com.thinkgem.jeesite.modules.red.service.RedConfigService;
import com.thinkgem.jeesite.modules.red.service.RedGameTotalService;
import com.thinkgem.jeesite.modules.red.utils.EmojiUtils;
import com.thinkgem.jeesite.modules.red.utils.RayValueUtils;
import com.thinkgem.jeesite.modules.red.utils.RedConfigUtils;
import com.thinkgem.jeesite.modules.red.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by jfang on 2017/4/11.
 */
@Authorization
@Controller
@RequestMapping(value = "${adminPath}/redBag/api")
public class RedBagApiController {

    private static Logger logger = LoggerFactory.getLogger(JedisUtils.class);
    @Autowired
    private RedConfigService redConfigService;
    @Autowired
    private RedBagSendLogService redBagSendLogService;
    @Autowired
    private RedBagGrabLogService redBagGrabLogService;
    @Autowired
    private RedGameTotalService redGameTotalService;


    private String RED_BAG_SEND_ID = "redBagSendId";
    private String SPACE_KEY = "|%||%||%||%|";


    @ResponseBody
    @RequestMapping(value = "/redResult")
    public Map<String, Object> redBagRecord(@RequestParam(required = true) String jsonStr, @CurrentUser JfAgentCode jfAgentCode) {
        String sendId = null;
        RedBagVo redBagVo = null;
        try {
            redBagVo = (RedBagVo) JsonMapper.fromJsonString(jsonStr, RedBagVo.class);
            //判断是否为指定群红包
            /*String roomNum = RedConfigUtils.getRoomNum(redBagVo.getCreditCode());
            if (StringUtils.isEmpty(roomNum) || StringUtils.equals(redBagVo.getRoomNum(), roomNum)) {
                return ResultUtils.getFailure("非指定群红包!");
            }*/
            redBagVo.setCreditCode(jfAgentCode.getCreditCode());

            //获取缓存数据
            sendId = JedisUtils.get(redBagVo.getCreditCode() + "_" + redBagVo.getSendId());

            if (StringUtils.isNotEmpty(sendId)) {
                return ResultUtils.getFailure("重复请求！");
            }

            redBagVo.countRward();
            int lossRatioAmount = (int) (redBagVo.getTotalAmount() * redBagVo.getLossRatioCount());

            /**
             * 返回计算结果
             */
            Map<String, Object> result = Maps.newConcurrentMap();
            result.put("roomNum", redBagVo.getRoomNum());
            result.put("sendId", redBagVo.getSendId());
            result.put("sendNick", redBagVo.getSendNick());
            result.put("totalNum", redBagVo.getTotalNum());
            result.put("totalAmount", redBagVo.getTotalAmount());
            result.put("wishing", redBagVo.getWishing());
            result.put("bagType", redBagVo.getBagType());
            result.put("message", getMessageRedWx(redBagVo));

            return ResultUtils.getSuccess("计算成功！", result);
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            if (StringUtils.isNotEmpty(message)) {
                HashMap<String, Object> result = new HashMap();
                result.put("flag", ResultUtils.STATE_FAILURE_ERROR);
                result.put(ResultUtils.MESSAGE_KEY_, "@" + redBagVo.getSendNick() + "\n " + message);
                return result;
            }
            return ResultUtils.getFailure("请求参数有误！");
        } finally {
            if (StringUtils.isEmpty(sendId)) {
                /**
                 * 保存发包记录->数据库
                 */
                redBagSendLogService.saveBag(redBagVo);
            }

        }
    }

    @ResponseBody
    @RequestMapping(value = "/getConfig")
    public Map<String, Object> getRedConfig(HttpServletRequest request, @CurrentUser JfAgentCode jfAgentCode) {
        ConfigJson configJson = RedConfigUtils.get(jfAgentCode.getCreditCode());
        if (configJson == null) {
            return ResultUtils.getFailure("配置更新失败！");
        }
        return ResultUtils.getSuccess("配置更新成功！", configJson);
    }

    /**
     * 保存配置
     *
     * @param jsonStr
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/setConfig", method = RequestMethod.POST)
    public Map<String, Object> setRedConfig(String jsonStr, String setType, HttpServletRequest request, @CurrentUser JfAgentCode jfAgentCode) {
        if (StringUtils.isEmpty(jsonStr) || StringUtils.isEmpty(setType)) {
            return ResultUtils.getFailure("配置保存失败, 参数有误！");
        }
        try {
            RedConfig redConfig = redConfigService.get(jfAgentCode.getCreditCode());
            ConfigJson configJson;
            if (redConfig != null) {
                configJson = redConfig.getConfigJson();
            } else {
                configJson = new ConfigJson();
                redConfig = new RedConfig();
                redConfig.setIsNewRecord(true);
                redConfig.setId(jfAgentCode.getCreditCode());
            }
            switch (setType) {
                case "overall":
                    ConfigOverall overall = (ConfigOverall) JsonMapper.fromJsonString(jsonStr, ConfigOverall.class);
                    configJson.setOverall(overall);
                    break;
                case "single":
                    List<ConfigSingle> singleList = (List<ConfigSingle>) JsonMapper.fromJsonString(jsonStr, List.class);
                    configJson.setSingleList(singleList);
                    break;
                case "both":
                    List<ConfigBoth> bothList = (List<ConfigBoth>) JsonMapper.fromJsonString(jsonStr, List.class);
                    configJson.setBothList(bothList);
                    break;
                case "combo":
                    List<ConfigCombo> comboList = (List<ConfigCombo>) JsonMapper.fromJsonString(jsonStr, List.class);
                    configJson.setComboList(comboList);
                    break;
                case "sun":
                    ConfigSun configSun = (ConfigSun) JsonMapper.fromJsonString(jsonStr, ConfigSun.class);
                    configJson.setConfigSun(configSun);
                    break;
                case "jackpot":
                    List<ConfigJackpot> jackpotList = (List<ConfigJackpot>) JsonMapper.fromJsonString(jsonStr, List.class);
                    configJson.setJackpotList(jackpotList);
                    break;
            }
            redConfig.setConfigJsonStr(JsonMapper.toJsonString(configJson));
            redConfigService.save(redConfig);
            return ResultUtils.getSuccess("配置保存成功！");
        } catch (Exception e) {
            return ResultUtils.getFailure("配置保存失败！");
        }


    }


    /**
     * 统计群玩家发包
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryGroupSend", method = RequestMethod.POST)
    public Map<String, Object> queryGroupSend(RedBagSendLog bagSend, @CurrentUser JfAgentCode jfAgentCode) {
        if (StringUtils.isEmpty(bagSend.getRoomNum())) {
            return ResultUtils.getFailure("查询失败，请求参数有误！");
        }

        bagSend.setCreditCode(jfAgentCode.getCreditCode());
        Integer totalAmount = 0, lossRatioAmountCount = 0, rwardAmountCount = 0, sendNum = 0;
        Map<String, Object> result = Maps.newConcurrentMap();


        List<RedBagSendGroupVo> sendList = redBagSendLogService.querySendGroup(bagSend);
        if (sendList != null) {
            for (RedBagSendGroupVo sendGroupVo : sendList) {
                totalAmount += sendGroupVo.getTotalAmount();
                lossRatioAmountCount += sendGroupVo.getLossRatioAmountCount();
                rwardAmountCount += sendGroupVo.getRwardAmountCount();
                sendNum += sendGroupVo.getSendNum();
//                sendGroupVo.setSendUserNick(EmojiUtils.emojiConvert(sendGroupVo.getSendUserNick()));
            }
        }
        result.put("sendLog", sendList);
        result.put("roomNum", bagSend.getRoomNum());
        result.put("totalAmount", totalAmount);
        result.put("lossRatioAmountCount", lossRatioAmountCount);
        result.put("rwardAmountCount", rwardAmountCount);
        result.put("sendNum", sendNum);
        return ResultUtils.getSuccess("查询成功!", result);
    }

    @ResponseBody
    @RequestMapping(value = "querySendLog", method = RequestMethod.POST)
    public Map<String, Object> querySendLog(RedBagSendLog bagSend, @RequestParam(defaultValue = "1") int pageNo, @CurrentUser JfAgentCode jfAgentCode) {
        if (StringUtils.isEmpty(bagSend.getRoomNum())) {
            return ResultUtils.getFailure("查询失败，请求参数有误！");
        }

        bagSend.setCreditCode(jfAgentCode.getCreditCode());
        Page<RedBagSendLog> page = new Page(pageNo, 20);

        Integer totalAmount = 0, lossRatioAmountCount = 0, rwardAmountCount = 0, sendNum = 0;
        boolean isGetMessage = StringUtils.isNotEmpty(bagSend.getSendUserNick());
        Map<String, Object> result = Maps.newConcurrentMap();
        //表情转换
//       、、 bagSend.setSendUserNick(EmojiUtils.emojiConvert(bagSend.getSendUserNick()));
        page = redBagSendLogService.findPage(page, bagSend);
        if (page != null && page.getList() != null) {
            for (RedBagSendLog log : page.getList()) {
                //参数带玩家昵称拼凑字符串
                if (isGetMessage) {
                    log.setRecord(getMessageRedLog(log));
                }else{
                    log.setRecord(null);
                }
                totalAmount += log.getTotalAmount();
                lossRatioAmountCount += log.getLossRatioAmountCount();
                rwardAmountCount += log.getRwardAmountCount();
//                log.setSendUserNick(EmojiUtils.emojiRecovery(log.getSendUserNick()));
            }
            sendNum = page.getList().size();
            result.put("sendLog", page.getList());
        }
        result.put("roomNum", bagSend.getRoomNum());
        result.put("totalAmount", totalAmount);
        result.put("lossRatioAmountCount", lossRatioAmountCount);
        result.put("rwardAmountCount", rwardAmountCount);
        result.put("sendNum", sendNum);
        result.put("pageNo", pageNo);
        return ResultUtils.getSuccess("查询成功!", result);
    }

    /**
     * 抢包手统计
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryGroupGrab", method = RequestMethod.POST)
    public Map<String, Object> queryGroupGrab(@RequestParam String creditCode, Integer avoid, String queryDate) {
        RedBagGrabLog redBagGrabLog = new RedBagGrabLog();
        redBagGrabLog.setCreditcode(creditCode);

        if (queryDate != null) {
            Date date = DateUtils.parseDate(queryDate);
            redBagGrabLog.setStartDate(date);
            redBagGrabLog.setEndDate(DateUtils.addDateByDay(date, 1));
        }
        if (avoid != null) {
            //统计免死抢夺
            redBagGrabLog.setAvoidNum(avoid);
        }
        List<RedBagGrabLog> redBagGrabLogList = redBagGrabLogService.querySendGroup(redBagGrabLog);
        Integer receiveAmount = 0;        // 抢包金额
        Integer avoidAmount = 0;        // 免死金额
        Integer receiveNum = 0;        // 抢包次数
        Integer avoidNum = 0;        // 免死次数
        for (RedBagGrabLog grab : redBagGrabLogList) {
            receiveAmount += grab.getReceiveAmount();
            receiveNum += grab.getReceiveNum();
            avoidAmount += avoidAmount;
            avoidNum += avoidNum;
        }

        Map<String, Object> result = Maps.newConcurrentMap();
        result.put("receiveAmount", receiveAmount);
        result.put("receiveNum", receiveNum);
        result.put("avoidAmount", avoidAmount);
        result.put("avoidNum", avoidNum);
        result.put("grabList", redBagGrabLogList);
        return result;
    }


    /**
     * 发包手（玩家）积分查询
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryGroupGameTotal", method = RequestMethod.POST)
    public Map<String, Object> queryGroupGameTotal(@CurrentUser JfAgentCode agentCode, RedGameTotal gameTotal) {
        /*if (StringUtils.isEmpty(gameTotal.getRoomNum())) {
            return ResultUtils.getFailure("请求参数有误！");
        }*/
        /*if (StringUtils.isNotEmpty(gameTotal.getSendUserNick())) {
            gameTotal.setSendUserNick(EmojiUtils.emojiConvert(gameTotal.getSendUserNick()));
        }*/
        gameTotal.setCreditCode(agentCode.getCreditCode());

        List<RedGameTotal> gameTotalList = redGameTotalService.findList(gameTotal);
        /*for (RedGameTotal total : gameTotalList) {
            //转义苹果表情
            total.setSendUserNick(EmojiUtils.emojiRecovery(total.getSendUserNick()));
        }*/
        return ResultUtils.getSuccess("查询成功！", gameTotalList);
    }


    /**
     * 玩家上下积分
     *
     * @param agentCode
     * @param upDown
     * @param totalId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/gameTotalUpDown", method = RequestMethod.POST)
    public Map<String, Object> gameTotalUpDown(@CurrentUser JfAgentCode agentCode, RedGameTotalUpDown upDown, String totalId) {
        if (StringUtils.isEmpty(upDown.getRoomNum())) {
            return ResultUtils.getFailure("请求参数有误！");
        }
        RedGameTotal gameTotal = null;
        if (StringUtils.isNotEmpty(totalId)) {
            gameTotal = redGameTotalService.get(totalId);
        }
        String msg = "";
        if (gameTotal == null) {
            if (StringUtils.equals(upDown.getType(), RedGameTotalUpDown.TOTAL_TYPE_DOWM)) {
                return ResultUtils.getFailure("玩家积分不足，剩余积分：0");
            }
            gameTotal = new RedGameTotal();
            gameTotal.setSendUserId(upDown.getSendUserId());
            gameTotal.setSendUserNick(upDown.getSendUserNick());
            gameTotal.setCreditCode(agentCode.getCreditCode());
            gameTotal.setGameTotal(upDown.getTotal());
            redGameTotalService.save(gameTotal);
            msg = "上分";
        } else {
            if (StringUtils.equals(upDown.getType(), RedGameTotalUpDown.TOTAL_TYPE_DOWM)) {
                if (gameTotal.getGameTotal().compareTo(upDown.getTotal()) == -1) {
                    return ResultUtils.getFailure("玩家积分不足，剩余积分：" + gameTotal.getGameTotal());
                }
                gameTotal.setGameTotal(gameTotal.getGameTotal().subtract(upDown.getTotal()));
                msg = "下分";
            } else {
                gameTotal.setGameTotal(gameTotal.getGameTotal().add(upDown.getTotal()));
                msg = "上分";
            }
            redGameTotalService.save(gameTotal);
        }
        Map<String, Object> resultMap = ResultUtils.getSuccess(msg + "成功");
        resultMap.put("total", gameTotal.getGameTotal());
        return resultMap;
    }


    /**
     * 按群号清空积分
     *
     * @param agentCode
     * @param roomNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/clearTotal", method = RequestMethod.POST)
    public Map<String, Object> clearTotal(@CurrentUser JfAgentCode agentCode, String roomNum) {
        if (StringUtils.isEmpty(roomNum)) {
            return ResultUtils.getFailure("请求参数有误!");
        }
        Map<String, String> param = Maps.newConcurrentMap();
        param.put("roomNum", roomNum);
        param.put("creditCode", agentCode.getCreditCode());
        redGameTotalService.deleteByRoomNum(param);
        return ResultUtils.getSuccess();
    }

    /**
     * 生成消息
     *
     * @param redBagVo
     * @return
     */
    private String getMessageRedWx(RedBagVo redBagVo) {
        StringBuffer message = new StringBuffer("发包详情：\n");
        message.append(redBagVo.getSendNick() + " 的红包[" + redBagVo.getWishing() + "]");
        message.append("实际金额包数" + DoubleUtils.divPercentToString(redBagVo.getTotalAmount()) + "/" + redBagVo.getTotalNum() + "\n");
        message.append("玩法识别：" + redBagVo.getRedBagType().getName() + "\n");
        switch (redBagVo.getRedBagType()) {
            case BOTH:
                message.append("（" + redBagVo.getLossRatioCount() + "）倍");
                break;
        }

        String inLeiTitle = "中雷明细：\n";
        String inLeiDetailMsg = "";
        int louInt = redBagVo.getRecord().size();
        for (RedBagRecordVo recordVo : redBagVo.getRecord()) {
            //抢包金额
            String receiveAmount = DoubleUtils.divPercentToString(recordVo.getReceiveAmount());
            String msg = SPACE_KEY + recordVo.getReceiveName() + "：" + receiveAmount + "元/" + louInt + "楼";
            if (recordVo.isInLei() && !recordVo.isAvoid()) {    //中雷 且非 免死
                inLeiDetailMsg += msg;
                switch (redBagVo.getRedBagType()) {
                    case SINGLE:
                        inLeiDetailMsg += "中雷";
                        if (recordVo.getLossRatio() > 0) {
                            inLeiDetailMsg += "(" + recordVo.getLossRatio() + "倍)";
                        }
                        break;
                    case BOTH:
                        inLeiDetailMsg += "中雷";
                        break;
                    case COMBO:
                        inLeiDetailMsg += " " + redBagVo.getLotteryResult();
                        break;
                    case SUM:
                        if (RayValueUtils.isLeopard(String.valueOf(recordVo.getReceiveAmount()))) {
                            inLeiDetailMsg += "豹子";
                        }
                        if (RayValueUtils.isStraight(String.valueOf(recordVo.getReceiveAmount()))) {
                            inLeiDetailMsg += "顺子";
                        }
                        inLeiDetailMsg += "(金额" + DoubleUtils.divPercentToString(recordVo.getRwardAmount()) + ")";
                }
                inLeiDetailMsg += "\n";
            }
            louInt--;
        }
        int profit = -(redBagVo.getLossRatioAmountCount() - redBagVo.getTotalAmount());
        message.append(inLeiTitle).append((StringUtils.isNotEmpty(inLeiDetailMsg) ? inLeiDetailMsg : "无\n"));
        message.append("盈利亏损：" + DoubleUtils.divPercentToString(profit) + "元");
        logger.debug(message.toString());
        return message.toString();
    }


    private String getMessageRedLog(RedBagSendLog sendLog) {
        StringBuffer message = new StringBuffer();
        message.append("祝福语：" + sendLog.getWishing()+ "\n" +
                "实际金额：" + DoubleUtils.divPercentToString(sendLog.getTotalAmount()) + "\n" +
                "实际包数：" + sendLog.getTotalNum() + "\n");
        RedBagType redBagType = RedBagType.getRedBagType(sendLog.getRayType());
        if (redBagType != null) {
            message.append("玩法：" + sendLog.getTotalNum() + "包" + redBagType.getName() + "\n");
//            String recordStr = EmojiUtils.emojiRecovery(sendLog.getRecord());
            List<RedBagRecordVo> recordList = (List<RedBagRecordVo>) JsonMapper.fromJsonString(sendLog.getRecord(), new TypeReference<List<RedBagRecordVo>>() {
            });
            ;
            if (recordList != null && recordList.size() > 0) {
                for (int i = recordList.size() - 1; i >= 0; i--) {
                    RedBagRecordVo recordVo = recordList.get(i);
                    message.append(recordVo.getReceiveName() + "：" + DoubleUtils.divPercentToString(recordVo.getReceiveAmount()) + "\n");
                }
            }
            String lossRatioAmountCount = DoubleUtils.divPercentToString(sendLog.getLossRatioAmountCount());
            message.append("结算：");
            if (sendLog.getLossRatioAmountCount() > 0) {    //是否有赔付
                switch (redBagType) {
                    case SINGLE:
                        message.append(sendLog.getLotteryResult() + "中雷,");
                        message.append("赔付积分：" + lossRatioAmountCount + "(" + String.valueOf(sendLog.getLossRatio()) + "倍)");
                        break;
                    case BOTH:
                        message.append(redBagType.getName() + "中" + sendLog.getRayValue().length() + "个雷,");
                        message.append("赔付积分：" + lossRatioAmountCount + "(" + String.valueOf(sendLog.getLossRatio()) + "倍)");
                        break;
                    case COMBO:
                        message.append("开 " + sendLog.getLotteryResult() + ", ");
                        message.append("赔付积分：" + lossRatioAmountCount + "(" + String.valueOf(sendLog.getLossRatio()) + "倍)");
                    case SUM:
                        message.append("抢到 " + sendLog.getLotteryResult() + "，");
                        message.append("赔付积分：" + lossRatioAmountCount);
                }

            } else {
                message.append("无");
            }
        } else {
            message.append("玩法：福利包");
        }
        return message.toString();
    }

    private String getCreditCode(HttpServletRequest request) {
        return (String) request.getAttribute(AuthorizationInterceptor.REQUEST_CURRENT_KEY);
    }

}
