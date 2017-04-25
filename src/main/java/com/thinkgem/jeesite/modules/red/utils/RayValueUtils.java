package com.thinkgem.jeesite.modules.red.utils;

import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * Created by jfang on 2017/4/21.
 */
public class RayValueUtils {
    public enum NumberEscape {
        NUMBER_LOWER(new String[]{"〇", "一", "二", "三", "四", "五", "六", "七,", "八", "九"}),
        NUMBER_UPPER(new String[]{"零", "壹", "貮", "叁", "肆", "伍", "陆", "柒", "捌", "玖"}),
        NUMBER_SYMBOL(new String[]{"〇", "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨"}),
        NUMBER_EMOJI(new String[]{"0️⃣", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"}),;
        private String[] array;

        NumberEscape(String[] array) {
            this.array = array;
        }

        public String[] getArray() {
            return array;
        }

        public void setArray(String[] array) {
            this.array = array;
        }

        /**
         * 转义
         *
         * @param num
         * @return
         */
        public static int escape(String num) {
            /*NumberEscape[] escapes = NumberEscape.values();
            for (NumberEscape e : escapes) {
                String[] strings = e.getStr();
                for (int i = 0 ;  i < strings.length ; i++) {
                    String s = strings[i];
                    if (StringUtils.equals(num, s)) {
                        return i;
                    }
                }
            }*/
            return 0;
        }

        /**
         * 将中文数字转义成数字
         *
         * @param str
         * @return
         */
        public static String escapeToString(String str) {
            NumberEscape[] escapes = NumberEscape.values();
            for (NumberEscape e : escapes) {
                String[] arrays = e.getArray();
                for (int i = 0; i < arrays.length; i++) {
                    str = str.replaceAll(arrays[i], String.valueOf(i));
                   /* if (str.indexOf(arrays[i]) > 0) {
                        resultStr += i;
                    }*/
                }
            }
            return str;
        }
    }

    /**
     * 中文表情转义
     */
    public enum BredEscape {
        BRED_DRAGON("龙", new String[]{"[[%F0%9F%90%B2]]", "龙", "龍"}),
        BRED_BRAVE("虎", new String[]{"[[%F0%9F%90%AF]]", "虎"}),
        BRED_JOIN("合", new String[]{"閤", "和", "[[%F0%9F%88%B4]]"}),
        BRED_SINGLE("单",new String[]{"單"}),
        BRED_DOUBLE("双", new String[]{"雙"}),
        BRED_SUM("日", new String[]{"鈤","曰","月","太阳","爆","曝","暴","☀"});
        private String key;
        private String[] values;

        BredEscape(String key, String[] values) {
            this.key = key;
            this.values = values;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String[] getValues() {
            return values;
        }

        public void setValues(String[] values) {
            this.values = values;
        }

        public static String escapeToString(String str){
            BredEscape[] escapes = BredEscape.values();
            for (BredEscape escape : escapes) {
                String escapeKey = escape.getKey();
                String[] arrays = escape.getValues();
                for (String s : arrays) {
                    str = str.replace(s, escapeKey);
                }
            }
            return str;
        }
    }


    public static String escape(String str) {
        str = NumberEscape.escapeToString(str);
        str = BredEscape.escapeToString(str);
        return str;
    }
    /**
     * 判断是否由纯数字组成
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        String regex = "[0-9]*";
        return str.matches(regex);
    }

    /**
     * 判断是否为豹子
     *
     * @param str
     * @return
     */
    public static boolean isLeopard(String str) {
        if (str.length() > 1) {
            String regex = str.substring(0, 1) + "{" + str.length() + "}";
            return str.matches(regex);
        }
        return false;
    }


    /**
     * 判断是否为顺子
     *
     * @param str
     * @return
     */
    public static boolean isStraight(String str) {
        if (str.length() > 2) {
            return "123456789".contains(str) || "9876543321".contains(str);
        }
        return false;
    }

    /**
     * 是否为不同的字符组成
     *
     * @param str
     * @return
     */
    public static boolean isNotEqualNum(String str) {
        String r = "";
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            String cc = String.valueOf(c);
            if (r.contains(cc)) {
                return false;
            }
            r += cc;
        }
        return true;
    }

    /**
     * 截取雷位
     * @param receiveAmount
     * @param sectType
     * @return
     */
    public static String getSectValue(Integer receiveAmount, int sectType) {
        String money = "000"+receiveAmount.toString();
        int moneyLength = money.length() - 1;
        Integer value = 0;
        int yuan = Integer.valueOf(String.valueOf(money.charAt(moneyLength - 2)));
        int jiao = Integer.valueOf(String.valueOf(money.charAt(moneyLength - 1 )));
        int fen = Integer.valueOf(String.valueOf(money.charAt(moneyLength - 0)));
        switch (sectType) {
            case 0:
                value = yuan;
                break;
            case 1 :
                value = jiao;
                break;
            case 2:
                value = fen;
                break;
            case 3:
                value = yuan + jiao;
                break;
            case 4:
                value = yuan + jiao + fen;
                break;
        }
        return String.valueOf((value%10));
    }

    public static void main(String[] args) {
        System.out.println(getSectValue(321, 0));
    }
}
