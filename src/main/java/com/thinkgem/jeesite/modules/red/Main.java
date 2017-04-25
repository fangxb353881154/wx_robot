package com.thinkgem.jeesite.modules.red;

import com.fasterxml.jackson.core.type.TypeReference;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.DoubleUtils;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.red.utils.RayValueUtils;
import com.thinkgem.jeesite.modules.red.vo.RedBagRecordVo;
import sun.util.calendar.BaseCalendar;

import java.io.IOException;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by jfang on 2017/4/16.
 */
public class Main {
    // 1. 定义枚举类型
    public enum Light {
        /*利用构造函数传参利用构造函数传参
        * 通过括号赋值,而且必须有带参构造器和属性和方法，否则编译出错
        * 赋值必须是都赋值或都不赋值，不能一部分赋值一部分不赋值
        * 如果不赋值则不能写构造器，赋值编译也出错
        * */
        RED("红色"), GREEN("绿色"), YELLOW("黄色");

        // 定义私有变量
        private String clor;

        // 构造函数，枚举类型只能为私有
        private Light(String clor) {
            this.clor = clor;
        }

        public String getClor() {
            return this.clor;
        }

        public void setClor(String clor) {
            this.clor = clor;
        }

        @Override
        public String toString() {
            return this.clor;
        }
    }

    public enum EnumConstant {
        WEEK_00("", "请选择"), WEEK_01("01", "周一"), WEEK_02("02", "周二"), WEEK_03("03", "周三");
        private String key;
        private String value;

        //自定义的构造函数，参数数量，名字随便自己取
        //构造器默认也只能是private, 从而保证构造函数只能在内部使用
        private EnumConstant(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        //重新toString方法，默认的toString方法返回的就是枚举变量的名字，和name()方法返回值一样
        @Override
        public String toString() {
            return this.key + ":" + this.value;

        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String s = "[{\"answer\":\"\",\"receiveAmount\":1064,\"receiveId\":\"1000039501001704137022984341101\",\"receiveName\":\"Sunstrider111\",\"receiveOpenId\":\"1000039501001704137022984341101\",\"receiveTime\":1492067740,\"userName\":\"llkoko111\",\"lossRatio\":0.0,\"avoid\":false,\"rward\":false,\"inLei\":false,\"receiveAmountDouble\":10.64},{\"answer\":\"\",\"receiveAmount\":332,\"receiveId\":\"1000039501001704137022984341101\",\"receiveName\":\"Sunstrider222\",\"receiveOpenId\":\"1000039501001704137022984341101\",\"receiveTime\":1492067736,\"userName\":\"llkoko222\",\"lossRatio\":0.0,\"avoid\":false,\"rward\":false,\"inLei\":false,\"receiveAmountDouble\":3.32},{\"answer\":\"\",\"receiveAmount\":521,\"receiveId\":\"1000039501001704137022984341101\",\"receiveName\":\"Sunstrider333\",\"receiveOpenId\":\"1000039501001704137022984341101\",\"receiveTime\":1492067730,\"userName\":\"llkoko333\",\"lossRatio\":0.0,\"avoid\":false,\"rward\":false,\"inLei\":false,\"receiveAmountDouble\":5.21},{\"answer\":\"\",\"receiveAmount\":1300,\"receiveId\":\"1000039501001704137022984341101\",\"receiveName\":\"Sunstrider444\",\"receiveOpenId\":\"1000039501001704137022984341101\",\"receiveTime\":1492067725,\"userName\":\"llkoko444\",\"lossRatio\":0.0,\"avoid\":false,\"rward\":true,\"inLei\":false,\"receiveAmountDouble\":13.0},{\"answer\":\"\",\"receiveAmount\":315,\"receiveId\":\"1000039501001704137022984341101\",\"receiveName\":\"Sunstrider555\",\"receiveOpenId\":\"1000039501001704137022984341101\",\"receiveTime\":1492067640,\"userName\":\"llkoko555\",\"lossRatio\":0.0,\"avoid\":true,\"rward\":false,\"inLei\":false,\"receiveAmountDouble\":3.15},{\"answer\":\"\",\"receiveAmount\":1639,\"gameTips\":\"手气最佳\",\"receiveId\":\"1000039501000704137022984341104\",\"receiveName\":\"yueyue\",\"receiveOpenId\":\"1000039501000704137022984341101\",\"receiveTime\":1492067634,\"userName\":\"wxid_alx48jocgzik22\",\"lossRatio\":60.0,\"avoid\":false,\"rward\":false,\"inLei\":true,\"receiveAmountDouble\":16.39}]";
        List<RedBagRecordVo> list = null;
        try {
            list = JsonMapper.getInstance().readValue(s, new TypeReference<List<RedBagRecordVo>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (RedBagRecordVo recordVo : list) {
            System.out.println(recordVo.getReceiveName());

        }
        //System.out.println(DoubleUtils.sub(Double.valueOf("5.2") , Double.valueOf("5.20")) == 0);
    }

    /**
     * 演示枚举类型的遍历
     */
    private static void testTraversalEnum() {

        Light[] allLight = Light.values();
        for (Light aLight : allLight) {
            System.out.println(" 当前灯 name ： " + aLight.name());
            System.out.println(" 当前灯 ordinal ： " + aLight.ordinal());
            System.out.println(" 当前灯： " + aLight);
        }
    }

    /**
     * 演示 EnumMap 的使用， EnumMap 跟 HashMap 的使用差不多，只不过 key 要是枚举类型
     */
    private static void testEnumMap() {
        // 1. 演示定义 EnumMap 对象， EnumMap 对象的构造函数需要参数传入 , 默认是 key 的类的类型
        EnumMap<Light, String> currEnumMap = new EnumMap<Light, String>(
                Light.class);
        currEnumMap.put(Light.RED, " 红灯 ");
        currEnumMap.put(Light.GREEN, " 绿灯 ");
        currEnumMap.put(Light.YELLOW, " 黄灯 ");

        // 2. 遍历对象
        for (Light aLight : Light.values()) {
            System.out.println("[key=" + aLight.name() + ",value=" + currEnumMap.get(aLight) + "]");
        }
    }

    /**
     * 演示 EnumSet 如何使用， EnumSet 是一个抽象类，获取一个类型的枚举类型内容 <BR/>
     * 可以使用 allOf 方法
     */
    private static void testEnumSet() {
        EnumSet<Light> currEnumSet = EnumSet.allOf(Light.class);
        for (Light aLightSetElement : currEnumSet) {
            System.out.println(" 当前 EnumSet 中数据为： " + aLightSetElement);
        }
    }
}
