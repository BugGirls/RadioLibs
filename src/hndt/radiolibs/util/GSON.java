package hndt.radiolibs.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 https://sites.google.com/site/gson/gson-user-guide
 //Type type = new TypeToken<List<ChannelRoleBean>>() {}.getType();
  new GsonBuilder()
 .setLenient()// json宽松
 .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
 .serializeNulls() //智能null
 .setPrettyPrinting()// 调教格式
 .disableHtmlEscaping() //默认是GSON把HTML 转义的
 .create();
 */
public class GSON {

    private static GsonBuilder builder;
    private static String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    private GSON() {
    }

    public static GsonBuilder getInstance() {
        if (builder == null) {
            builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.disableHtmlEscaping();
            builder.setDateFormat(DATEFORMAT);
            builder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        }
        return builder;
    }

    public static String toJson(Object bean) {
        return getInstance().create().toJson(bean);
    }

    public static <T> T toObject(String json, Class<T> t) {
        T obj = getInstance().create().fromJson(json, t);
        return obj;
    }

    // 将多个值类型为Integer的String字符串转换成Object（List）类型的数据
    public static <T> T toObject(String json, Type type) {
        T obj = getInstance().create().fromJson(json, type);
        return obj;
    }

    public static <T> List<T> toList(String json, Type type) {
        List<T> list = getInstance().create().fromJson(json, type);
        return list;
    }

    public static <T> List<T> toList(String json, Class<T> elementClass) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();
        JsonElement root = new JsonParser().parse(json);
        for (JsonElement e : root.getAsJsonArray()) {
            T entity = gson.fromJson(e, elementClass);
            list.add(entity);
        }
        return list;
    }

}


class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    @Override public LocalTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        } else {
            return LocalTime.parse(json.getAsString());
        }
    }

    @Override public JsonElement serialize(LocalTime src, Type type, JsonSerializationContext context) {
        String value = "";
        if (src != null) {
            value = src.toString();
        }
        return new JsonPrimitive(value);
    }
}
