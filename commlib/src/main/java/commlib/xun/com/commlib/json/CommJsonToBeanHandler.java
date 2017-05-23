package commlib.xun.com.commlib.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import commlib.xun.com.commlib.exception.JsonFormatException;

/**
 * Created by xunwang on 17/5/23.
 */

public class CommJsonToBeanHandler {
    public static final String tag = CommJsonToBeanHandler.class.getSimpleName();

    private static CommJsonToBeanHandler instance;

    private Gson gsonMapper;

    private CommJsonToBeanHandler() {
        GsonBuilder builer = new GsonBuilder();
        builer.disableHtmlEscaping();

        gsonMapper = builer.create();
    }

    private static synchronized void initInstanceSyn() {
        if (instance == null) {
            instance = new CommJsonToBeanHandler();
        }
    }

    public static synchronized CommJsonToBeanHandler getInstance() {
        if (instance == null) {
            initInstanceSyn();
        }
        return instance;
    }

    /**
     * 数据源转化为 对象bean
     */
    public <T> T fromJsonString(String data, Class<T> cls) throws JsonFormatException {
        try {
            return gsonMapper.fromJson(data, cls);
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new JsonFormatException("json format to " + cls.getName() + " exception:" + data);
        }
    }

    /**
     * 数据源转化为 对象List<bean>
     */
    public <T> List<T> fromJsonStringList(String data, Class<T> cls) throws JsonFormatException {
        try {
            Type type = new ListParameterizedType(cls);
            List<T> result = gsonMapper.fromJson(data, type);
            return result;
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new JsonFormatException("json format to " + cls.getName() + " exception:" + data);
        }
    }


    /**
     * 数据源转化为 对象bean
     */
    public <T> T fromByteArray(byte[] data, Class<T> cls) throws JsonFormatException {
        if (data == null) {
            return null;
        }

        String dataString = new String(data);

        //bug compatiblity
        if (dataString.lastIndexOf("}") != dataString.length() - 1) {
            dataString = dataString.substring(0, dataString.lastIndexOf("}") + 1);
        }

        return fromJsonString(dataString, cls);
    }

    /**
     * 对象bean转化为json字符串
     */
    public String toJsonString(Object value) throws JsonFormatException {
        return gsonMapper.toJson(value);
    }

    /**
     * 对象bean转化为json byte数组
     */
    public byte[] toByteArray(Object value) throws JsonFormatException {
        return toJsonString(value).getBytes();
    }
}
