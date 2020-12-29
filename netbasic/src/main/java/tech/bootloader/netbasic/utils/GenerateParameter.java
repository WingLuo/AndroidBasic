package tech.bootloader.netbasic.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/7/5.
 */

public class GenerateParameter {
    private GenerateParameter() {}

    private final String TAG = GenerateParameter.class.getSimpleName();

    public RequestBody getRequestBody(Map map){
        return getRequestBody(map,false);
    }
    public RequestBody getListRequestBody(Map map){
        return getRequestBody(map,true);
    }
    public RequestBody getRequestBody(List list){
        return getRequestBody(list,false);
    }
    public RequestBody getListRequestBody(List list) {
        return getRequestBody(list,true);
    }
    public RequestBody getRequestBody(Object entity){
        return getRequestBody(entity,false);
    }
    public RequestBody getListRequestBody(Object entity){
        return getRequestBody(entity,true);
    }



    /**
     * 通过map对象转换成RequestBody，因项目有特殊需求，这里判断是否要加"[]"
     *
     * @param obj    Request实例
     * @param isArray ture在参数两端添加[]"[{a:a,b:b}]",flase 不在参数两段添加[]"{a:a,b:b}"
     * @return RequestBody
     */
    public RequestBody getRequestBody(Object obj, boolean isArray) {
//        Gson gson=new Gson();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .enableComplexMapKeySerialization()
                .create();
        String parameters = gson.toJson(obj);
        StringBuffer string = new StringBuffer(parameters.length() + 2);
        if (isArray) {
            string.append("[");
            string.append(parameters);
            string.append("]");
        } else {
            string.append(parameters);
        }

        if (string.length() < 10000) {
            Log.d(TAG,string.toString());
        } else {
            Log.d(TAG,string.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; encoding=utf-8"), string.toString());
        return body;
    }
    /**
     * 将list转换成jsonArray
     *
     * @param list 集合
     * @return
     */
    public RequestBody list2RequestBody(List list) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .enableComplexMapKeySerialization()
                .create();
        String parameters = gson.toJson(list);
        if (parameters.length() < 10000) {
            Log.d(TAG, "parameters:" + parameters);
        } else {
            Log.d(TAG, "parameters:" + "parameters too long");
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; encoding=utf-8"), parameters.toString());
        return body;
    }


    /**
     * @param name     partname
     * @param filename 文件名
     * @param file     文件
     * @return
     */
    public MultipartBody.Part file2MultipartBody_Part(String name, String filename, File file) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(name, filename, requestBody);
        return body;
    }

    public static GenerateParameter getInstance() {
        return GenerateParameterInnerHolder.instance;
    }

    private static class GenerateParameterInnerHolder {
        public static GenerateParameter instance = new GenerateParameter();
    }


    /**
     * 拼接get请求的url请求地址
     */
    public static String getRqstUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(url);
        boolean isFirst = true;
        for (String key : params.keySet()) {
            if (key != null && params.get(key) != null) {
                if (isFirst) {
                    isFirst = false;
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(key)
                        .append("=")
                        .append(params.get(key));
            }
        }
        return builder.toString();
    }

}
