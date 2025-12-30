package com.allo.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * desc:
 * verson:
 * create by zhijun on 2024/11/13 19:38
 * update by zhijun on 2024/11/13 19:38
 */
public class IntDeserializer implements JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if ("".equals(json.getAsString())) {
            return 0;
        }
        try {
            return json.getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
