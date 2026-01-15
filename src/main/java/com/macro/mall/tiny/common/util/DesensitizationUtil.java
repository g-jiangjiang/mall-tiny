package com.macro.mall.tiny.common.util;

import cn.hutool.core.util.StrUtil;

/**
 * 数据脱敏工具类
 */
public class DesensitizationUtil {

    private static final String SYMBOL = "*";

    public static String desensitizeUsername(String username) {
        if (StrUtil.isEmpty(username)) {
            return username;
        }
        int length = username.length();
        if (length <= 2) {
            return username.charAt(0) + SYMBOL.repeat(length - 1);
        }
        int midStart = length / 3;
        int midEnd = length - midStart;
        return username.substring(0, midStart) + SYMBOL.repeat(midEnd - midStart) + username.substring(midEnd);
    }

    public static String desensitizeNickName(String nickName) {
        if (StrUtil.isEmpty(nickName)) {
            return nickName;
        }
        int length = nickName.length();
        if (length <= 2) {
            return nickName.charAt(0) + SYMBOL.repeat(Math.max(0, length - 1));
        }
        int midStart = length / 3;
        int midEnd = length - midStart;
        return nickName.substring(0, midStart) + SYMBOL.repeat(midEnd - midStart) + nickName.substring(midEnd);
    }

    public static String desensitizePhone(String phone) {
        if (StrUtil.isEmpty(phone)) {
            return phone;
        }
        if (phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        }
        return desensitizeCommon(phone, 3, 3);
    }

    public static String desensitizeEmail(String email) {
        if (StrUtil.isEmpty(email)) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return email;
        }
        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        if (username.length() <= 2) {
            return username.charAt(0) + SYMBOL + domain;
        }
        return username.substring(0, 2) + SYMBOL.repeat(Math.max(0, username.length() - 2)) + domain;
    }

    public static String desensitizeIcon(String icon) {
        if (StrUtil.isEmpty(icon)) {
            return icon;
        }
        return "***";
    }

    public static String desensitizePassword(String password) {
        if (StrUtil.isEmpty(password)) {
            return password;
        }
        return "******";
    }

    public static String desensitizeRemark(String remark) {
        if (StrUtil.isEmpty(remark)) {
            return remark;
        }
        int length = remark.length();
        if (length <= 10) {
            return remark;
        }
        return remark.substring(0, 10) + "...";
    }

    private static String desensitizeCommon(String str, int keepStart, int keepEnd) {
        if (StrUtil.isEmpty(str)) {
            return str;
        }
        int length = str.length();
        if (length <= keepStart + keepEnd) {
            return str;
        }
        return str.substring(0, keepStart) + SYMBOL.repeat(length - keepStart - keepEnd) + str.substring(length - keepEnd);
    }
}
