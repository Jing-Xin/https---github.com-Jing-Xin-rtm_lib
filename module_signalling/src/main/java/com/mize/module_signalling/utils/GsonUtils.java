package com.mize.module_signalling.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GsonUtils {
    private static Gson gson = null;

    static {
        if (gson == null) {
//            gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>() {
//            }.getType(), new MapTypeAdapter()).create();
//            gson = new Gson();
            gson = new GsonBuilder()
                    .registerTypeAdapter(
                            new TypeToken<TreeMap<String, Object>>() {
                            }.getType(),
                            new JsonDeserializer<TreeMap<String, Object>>() {
                                @Override
                                public TreeMap<String, Object> deserialize(
                                        JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {

                                    TreeMap<String, Object> treeMap = new TreeMap<>();
                                    JsonObject jsonObject = json.getAsJsonObject();
                                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                                        Object ot = entry.getValue();
                                        if (ot instanceof JsonPrimitive) {
                                            treeMap.put(entry.getKey(), ((JsonPrimitive) ot).getAsString());
                                        } else {
                                            treeMap.put(entry.getKey(), ot);
                                        }
                                    }
                                    return treeMap;
                                }
                            }).create();

        }

    }

    private GsonUtils() {
    }

    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T toBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> ArrayList<T> toList(String gsonString, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(gsonString).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;

    }


    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static TreeMap<String, Object> GsonToMaps(String gsonString) {

        TreeMap<String, Object> map = null;
        if (gson != null) {
            Type typeToken = new TypeToken<TreeMap<String, Object>>() {
            }.getType();
            map = gson.fromJson(gsonString, typeToken);
        }
        return map;
    }

    //转成map
    public static HashMap<String, Object> tomap(Object object) {
        String json = toJson(object);
        HashMap<String, Object> map = null;
        if (gson != null) {
            Type typeToken = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            map = gson.fromJson(json, typeToken);
        }
        return map;
    }


    public static class MapTypeAdapter extends TypeAdapter<Object> {

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();

                case NUMBER:
                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    double dbNum = in.nextDouble();

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }

                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        return lngNum;
                    } else {
                        return dbNum;
                    }

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            // 序列化无需实现
        }

    }

}


