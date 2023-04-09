package com.steelman.iot.platform.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * @author tang
 * date 2020-12-08
 */
public class JsonUtils {

    public final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().registerTypeAdapter(String.class, new StringConverter()).create();

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static String toJson(Object src, Type objType) {
        return gson. toJson(src, objType);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }
}
