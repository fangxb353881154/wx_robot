package com.thinkgem.jeesite.modules.red.enums;

import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * Created by jfang on 2017/4/21.
 */
public enum RedCommand {
    /**
     * type 对应 RedBagType
     */
    COMBO(3,new String[]{"大","单","小","双","大单","大双","小单","小双","合"}),
    PRED(4, new String[]{"龙","虎"}),
    SUM(5,new String[]{"日"});

    RedCommand(int type, String[] commoand) {
        this.type = type;
        this.commoand = commoand;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getCommoand() {
        return commoand;
    }

    public void setCommoand(String[] commoand) {
        this.commoand = commoand;
    }

    private int type;
    private String[] commoand;

    /**
     * 根据命令获取玩法类型
     * @param rayValue
     * @return
     */
    public static RedBagType getRedBagType(String rayValue) {
        RedCommand[] redCommands = RedCommand.values();
        for (RedCommand redCommand : redCommands) {
            String[] arrays = redCommand.getCommoand();
            for (String s : arrays) {
                if (StringUtils.equals(s, rayValue)) {
                    return RedBagType.getRedBagType(redCommand.getType());
                }
            }
        }
        return null;
    }
}
