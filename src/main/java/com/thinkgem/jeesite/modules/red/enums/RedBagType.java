package com.thinkgem.jeesite.modules.red.enums;

/**
 *
 * 抢包玩法 枚举
 * Created by jfang on 2017/4/21.
 */
public enum RedBagType {
    SINGLE("单雷",1), BOTH("连环雷",2), COMBO("大小单双合",3), PRED("龙虎",4), SUM("日爆", 5);

    private String name;
    private int index;

    RedBagType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static String getName(int index) {
        RedBagType redBagType = getRedBagType(index);
        if (redBagType != null){
            return redBagType.getName();
        }
        return null;
    }

    public static RedBagType getRedBagType(int index) {
        for (RedBagType c : RedBagType.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
    }
}
