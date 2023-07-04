package com.zhuooo.workflow.utils;

import com.googlecode.aviator.AviatorEvaluator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

public class ConditionUtils {
    public static boolean calculate(String expression, Map<String, Object> map) {
        if (StringUtils.isEmpty(expression) || CollectionUtils.isEmpty(map)) {
            return false;
        }
        return (boolean) AviatorEvaluator.execute(expression, map);
    }

}
