package com.scottlogic.deg.generator.cucumber;

import java.util.HashMap;
import java.util.Map;

public class DegTestOperatorMapping {
    private static Map<String, String> mapping = new HashMap<String, String>(){
        {
            put("match regex", "matchingRegex");
        }
    };

    public static String getOperator(String key){
        return DegTestOperatorMapping.mapping.get(key);
    }

}
