package com.allo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * desc:
 * verson:
 * create by zhijun on 2024/11/13 19:38
 * update by zhijun on 2024/11/13 19:38
 */
public class ListObjectDeserializer implements JsonDeserializer<List<?>> {

    @Override
    public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            try {
                return new Gson().fromJson(json, typeOfT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Collections.EMPTY_LIST;
    }
}
