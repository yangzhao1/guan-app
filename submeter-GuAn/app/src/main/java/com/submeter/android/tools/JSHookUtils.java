package com.submeter.android.tools;

import android.text.TextUtils;

import com.submeter.android.constants.SystemConstant;

import java.util.HashMap;

public class JSHookUtils {
    public static String COMMODITY_PAY_HOOK = "commodity_pay";
    public static String CLOSE_PAGE = "closePage";

    public static String getFunctionName(String url) {
        if (TextUtils.isEmpty(url) || !url.startsWith(SystemConstant.JS_SCHEME)) {
            return null;
        }

        int sepIndex = url.indexOf("?");
        if (sepIndex == -1) {
            return url.substring(SystemConstant.JS_SCHEME.length());
        } else {
            return url.substring(SystemConstant.JS_SCHEME.length(), sepIndex);
        }
    }

    public static HashMap<String, String> getParam(String url) {
        if (TextUtils.isEmpty(url) || !url.startsWith(SystemConstant.JS_SCHEME)) {
            return null;
        }

        int sepIndex = url.indexOf("?");
        if (sepIndex < 0) {
            return null;
        }

        String[] nameValues = url.substring(sepIndex + 1).split("&");
        if (null != nameValues) {
            HashMap<String, String> map = new HashMap<>(nameValues.length);
            for (String item : nameValues) {
                sepIndex = item.indexOf("=");
                if (sepIndex > -1) {
                    map.put(item.substring(0, sepIndex), item.substring(sepIndex + 1));
                }
            }

            return map;
        }

        return null;
    }
}