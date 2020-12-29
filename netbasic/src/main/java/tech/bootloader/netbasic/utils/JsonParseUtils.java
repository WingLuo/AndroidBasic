package tech.bootloader.netbasic.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/6/5.
 */

public class JsonParseUtils {

    private final static String TAG = JsonParseUtils.class.getSimpleName();
    /**
     * json格式 [{a:a,b:b}]
     * @param json json
     * @return
     */
    public static Map<String, Object> json2Map(String json, Boolean isArray){
        if (isArray){
            if (json.length()>2&&json.startsWith("[")&&json.endsWith("]")) {
                json=(String) json.subSequence(1, json.length()-1);
            }
        }
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        Map<String, Object> map = g.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
        return map;
    }
    /**
     * json格式 [{a:a,b:b}]
     * @param json json
     * @return
     */
    public static<T extends Object> T json2T(String json, Class<T> clazz, Boolean isArray){
        if (isArray){
            if (json.length()>2&&json.startsWith("[")&&json.endsWith("]")) {
                json=(String) json.subSequence(1, json.length()-1);
            }else{
                Log.d(TAG,"this is array,but this is too short,or format is wrong:"+json);
            }
        }

        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
         T t= g.fromJson(json, clazz);
        Log.d(TAG, "Gson " + "解析完成");
        Log.d(TAG, "Gson t" + t);
        return t;
    }

    public static<T> List<T> jsonArray2List(String jsonArray, Class<T> clazz){
        Gson gson = new Gson();
        Type objectType = type(List.class, clazz);
        List<T> list=gson.fromJson(jsonArray, objectType);
        if(list==null||list.isEmpty()||list.size()==0){
            Log.e(TAG, "jsonArray2List Exception: " + "list is empty:"+jsonArray);
            return new ArrayList();
        }else{
            return list;

        }

    }




    private static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
