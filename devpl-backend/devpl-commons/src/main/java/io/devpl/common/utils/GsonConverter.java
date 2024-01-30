package io.devpl.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonConverter implements JSONConverter {

    private final Gson gson;

    private final Gson gsonPrettyPrinter;

    public GsonConverter() {
        final GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls(); // 不忽略null值
        // builder.registerTypeAdapter(PropertyFill.class, null);
        this.gson = builder.create();
        gsonPrettyPrinter = new GsonBuilder()
            .setPrettyPrinting().serializeNulls().create();
    }

    @Override
    public String toJSONString(Object obj) {
        return toJSONString(obj, false);
    }

    @Override
    public String toJSONString(Object obj, boolean prettyPrinting) {
        if (prettyPrinting) {
            return gsonPrettyPrinter.toJson(obj);
        }
        return gson.toJson(obj);
    }

    @Override
    public <T> T toObject(String jsonString, Class<T> type) {
        return gson.fromJson(jsonString, type);
    }
}
