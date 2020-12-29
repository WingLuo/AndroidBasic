package tech.bootloader.netbasic.rxjava2_retrofit;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import tech.bootloader.netbasic.service.RetrofitService;
import tech.bootloader.netbasic.ssl.SSLParams;
import tech.bootloader.netbasic.ssl.SimpleSSLParams;
import tech.bootloader.netbasic.utils.NetworkUtil;

/**
 * @author by luoyong on 2017/7/5.
 * compile 'com.squareup.okhttp3:okhttp:3.6.0'
 * compile 'com.squareup.okio:okio:1.11.0'
 * compile 'com.zhy:okhttputils:2.6.2'
 * compile 'com.google.code.gson:gson:2.8.0'
 * compile "io.reactivex.rxjava2:rxjava:2.x.y"
 * compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
 * compile 'com.squareup.retrofit2:retrofit:2.1.0'
 * compile 'com.squareup.retrofit2:converter-gson:2.1.0'
 * compile 'com.squareup.retrofit2:converter-scalars:2.1.0'
 * //**adapter-rxjava只支持rxjava
 * //    compile 'com.squareup.retrofit2:adapter-rxjava:2.1.0'
 * compile 'com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0'
 */

public abstract class BaseRxJava2RetrofitImp implements RxJava2Retrofit {

    private static final long DEFAULT_TIMEOUT = 5;
    /**
     * 一次超时 时间
     */
    private long oneceTimeout = -1L;
    private List<Interceptor> interceptors = null;

    private Gson gson = new GsonBuilder().setLenient().create();
    private Converter.Factory gsonConverterFactory = GsonConverterFactory.create(gson);
    protected final Retrofit.Builder builder = new Retrofit.Builder()
            //此转换器可将response转为string
            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    protected SSLParams sslParams = SimpleSSLParams.getSslSocketFactory(null, null, null);
    private Context context;

    private final String TAG = getClass().getSimpleName();

    /**
     * 生成 OkHttpClient
     *
     * @return
     */
    protected OkHttpClient getOkHttpClient() {
        OkHttpClient client;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(oneceTimeout > 0 ? oneceTimeout : DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(oneceTimeout > 0 ? oneceTimeout : DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(oneceTimeout > 0 ? oneceTimeout : DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        oneceTimeout = -1;
        //给client添加拦截器
        if (interceptors != null && !interceptors.isEmpty()) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        //设置okHttp的缓存
        if (context != null) {
            builder.cache(new Cache(new File(context.getExternalCacheDir(), "network_cache"), 64 * 1024 * 1024));
        }


        client = builder.build();
        return client;
    }

    public BaseRxJava2RetrofitImp setTimeout(long timeOut) {
        if (timeOut > 0) {
            oneceTimeout = timeOut;
        }
        return this;
    }

    public void initSSL(SSLParams params) {
        sslParams = params;
    }

    /**
     * @param baseUrl          基础的url
     * @param url              目标url
     * @param body             body
     * @param onNextAtIOThread 在子线程执行方法
     * @param onNext           在主线程执行方法
     * @param onError          返回错误执行方法
     * @param onComplete       方法执行完成后执行方法
     */
    public void doPost(String baseUrl, final String url, RequestBody body, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                       Action onComplete) {
        doPost(sslParams, baseUrl, url, body, onNextAtIOThread, onNext, onError, onComplete);
    }

    public void doPost(SSLParams params, String baseUrl, final String url, RequestBody body, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                       Action onComplete) {
        //    BusinessRetrofit retrofit=new BusinessRetrofit.Builder()
////                .client(new OkHttpClient().newBuilder().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).build())
//            .addConverterFactory(ScalarsConverterFactory.create()) //此转换器可将response转为string
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//rxjava2适配
//            .baseUrl(baseUrl)
//            .build();
//        SimpleSSLParams sslParams=SimpleSSLParams.getSslSocketFactory(null,null,null);
        Retrofit retrofit = builder.baseUrl(baseUrl)
                .client(getOkHttpClient().newBuilder().sslSocketFactory(params.sSLSocketFactory, params.trustManager).build())
                .build();
        RetrofitService post = retrofit.create(RetrofitService.class);
        post.postJson(url, body)
                //在子线程中执行数据请求
                .subscribeOn(Schedulers.newThread())
                //标记子线程
                .observeOn(Schedulers.io())
                //返回成功在子线程中执行
                .doOnNext(onNextAtIOThread)
                //在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                //分别在主线程中执行成功，失败，完成方法
                .subscribe(onNext, onError, onComplete);
    }

    public void doPostMap(String baseUrl, final String url, Map<String, String> maps, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                          Action onComplete) {
        doPostMap(sslParams, baseUrl, url, maps, onNextAtIOThread, onNext, onError, onComplete);
    }

    public void doPostMap(SSLParams params, String baseUrl, final String url, Map<String, String> maps, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                          Action onComplete) {
        Retrofit retrofit = builder.baseUrl(baseUrl)
                .client(getOkHttpClient().newBuilder().sslSocketFactory(params.sSLSocketFactory, params.trustManager).build())
                .build();
        RetrofitService post = retrofit.create(RetrofitService.class);
        post.postMap(url, maps)
                //在子线程中执行数据请求
                .subscribeOn(Schedulers.newThread())
                //标记子线程
                .observeOn(Schedulers.io())
                //返回成功在子线程中执行
                .doOnNext(onNextAtIOThread)
                //在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                //分别在主线程中执行成功，失败，完成方法
                .subscribe(onNext, onError, onComplete);
    }


    /**
     * 单文件
     *
     * @param baseUrl
     * @param url
     * @param partMap
     * @param part
     * @param onNextAtIOThread
     * @param onNext
     * @param onError
     * @param onComplete
     */
    public void doPostFileFromData(String baseUrl, final String url, Map<String, RequestBody> partMap, MultipartBody.Part part, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                                   Action onComplete) {
        doPostFileFromData(sslParams, baseUrl, url, partMap, part, onNextAtIOThread, onNext, onError, onComplete);
    }

    /**
     * 单文件带ssl
     *
     * @param params
     * @param baseUrl
     * @param url
     * @param partMap
     * @param part
     * @param onNextAtIOThread
     * @param onNext
     * @param onError
     * @param onComplete
     */
    public void doPostFileFromData(SSLParams params, String baseUrl, final String url, Map<String, RequestBody> partMap, MultipartBody.Part part, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                                   Action onComplete) {
        List<MultipartBody.Part> parts = new ArrayList<>(1);
        parts.add(part);
        doPostFileFromDatas(params, baseUrl, url, partMap, parts, onNextAtIOThread, onNext, onError, onComplete);
    }

    /**
     * 多文件上传
     *
     * @param baseUrl
     * @param url
     * @param partMap
     * @param parts
     * @param onNextAtIOThread
     * @param onNext
     * @param onError
     * @param onComplete
     */
    public void doPostFileFromDatas(String baseUrl, final String url, Map<String, RequestBody> partMap, List<MultipartBody.Part> parts, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                                    Action onComplete) {
        doPostFileFromDatas(sslParams, baseUrl, url, partMap, parts, onNextAtIOThread, onNext, onError, onComplete);
    }

    /**
     * 表单提交文件
     *
     * @param params
     * @param baseUrl
     * @param url
     * @param partMap
     * @param parts
     * @param onNextAtIOThread
     * @param onNext
     * @param onError
     * @param onComplete
     */
    public void doPostFileFromDatas(SSLParams params, String baseUrl, final String url, Map<String, RequestBody> partMap, List<MultipartBody.Part> parts, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                                    Action onComplete) {
        Retrofit retrofit = builder.baseUrl(baseUrl)
                .client(getOkHttpClient().newBuilder().sslSocketFactory(params.sSLSocketFactory, params.trustManager).build())
                .build();
        RetrofitService postFile = retrofit.create(RetrofitService.class);
//        MultipartBody.Part part = file2MultipartBody_Part(name, fileName, file);
        postFile.postFileFromData(url, partMap, parts)
                //在子线程中执行数据请求
                .subscribeOn(Schedulers.newThread())
                //标记子线程
                .observeOn(Schedulers.io())
                //返回成功在子线程中执行
                .doOnNext(onNextAtIOThread)
                //在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                //分别在主线程中执行成功，失败，完成方法
                .subscribe(onNext, onError, onComplete);
    }

    /**
     * @param baseUrl          基础的url
     * @param url              目标url
     * @param file             文件
     * @param onNextAtIOThread 在子线程执行方法
     * @param onNext           在主线程执行方法
     * @param onError          返回错误执行方法
     * @param onComplete       方法执行完成后执行方法
     */
    public void doPostFile(String baseUrl, final String url, File file, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                           Action onComplete) {
        doPostFile(sslParams, baseUrl, url, file, onNextAtIOThread, onNext, onError, onComplete);
    }

    public void doPostFile(SSLParams params, String baseUrl, final String url, File file, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                           Action onComplete) {
        Retrofit retrofit = builder.baseUrl(baseUrl)
                .client(getOkHttpClient().newBuilder().sslSocketFactory(params.sSLSocketFactory, params.trustManager).build())
                .build();
        RetrofitService post = retrofit.create(RetrofitService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        post.postFile(url, requestBody)
                //在子线程中执行数据请求
                .subscribeOn(Schedulers.newThread())
                //标记子线程
                .observeOn(Schedulers.io())
                //返回成功在子线程中执行
                .doOnNext(onNextAtIOThread)
                //在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                //分别在主线程中执行成功，失败，完成方法
                .subscribe(onNext, onError, onComplete);
    }

    /**
     * @param baseUrl          基础的url
     * @param url              目标url
     * @param part             多个文件part
     * @param onNextAtIOThread 在子线程执行方法
     * @param onNext           在主线程执行方法
     * @param onError          返回错误执行方法
     * @param onComplete       方法执行完成后执行方法
     */
    public void doPostFile(String baseUrl, final String url, MultipartBody.Part part, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                           Action onComplete) {
        doPostFile(sslParams, baseUrl, url, part, onNextAtIOThread, onNext, onError, onComplete);
    }

    public void doPostFile(SSLParams params, String baseUrl, final String url, MultipartBody.Part part, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                           Action onComplete) {
        Retrofit retrofit = builder.baseUrl(baseUrl)
                .client(getOkHttpClient().newBuilder().sslSocketFactory(params.sSLSocketFactory, params.trustManager).build())
                .build();
        RetrofitService postFile = retrofit.create(RetrofitService.class);
//        MultipartBody.Part part = file2MultipartBody_Part(name, fileName, file);
        postFile.postFile(url, part)
                //在子线程中执行数据请求
                .subscribeOn(Schedulers.newThread())
                //标记子线程
                .observeOn(Schedulers.io())
                //返回成功在子线程中执行
                .doOnNext(onNextAtIOThread)
                //在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                //分别在主线程中执行成功，失败，完成方法
                .subscribe(onNext, onError, onComplete);

    }

    /**
     * get请求
     *
     * @param baseUrl          基础的url
     * @param url              目标url
     * @param options          多参数的map
     * @param onNextAtIOThread 在子线程执行方法
     * @param onNext           在主线程执行方法
     * @param onError          返回错误执行方法
     * @param onComplete       方法执行完成后执行方法
     */
    public void doGet(String baseUrl, final String url, Map<String, Object> options, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                      Action onComplete) {
        doGet(sslParams, baseUrl, url, options, onNextAtIOThread, onNext, onError, onComplete);
    }

    public void doGet(SSLParams params, String baseUrl, final String url, Map<String, Object> options, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                      Action onComplete) {
        Retrofit retrofit = builder.baseUrl(baseUrl)
                .client(getOkHttpClient().newBuilder().sslSocketFactory(params.sSLSocketFactory, params.trustManager).build())
                .build();
        RetrofitService get = retrofit.create(RetrofitService.class);
        get.get(url, options)
                //在子线程中执行数据请求
                .subscribeOn(Schedulers.newThread())
                //标记子线程
                .observeOn(Schedulers.io())
                //返回成功在子线程中执行
                .doOnNext(onNextAtIOThread)
                //在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                //分别在主线程中执行成功，失败，完成方法
                .subscribe(onNext, onError, onComplete);
    }

    /**
     * @param baseUrl          基础的url
     * @param url              目标url
     * @param onNextAtIOThread 在子线程执行方法
     * @param onNext           在主线程执行方法
     * @param onError          返回错误执行方法
     * @param onComplete       方法执行完成后执行方法
     */
    public void doGet(String baseUrl, final String url, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                      Action onComplete) {
        doGet(sslParams, baseUrl, url, onNextAtIOThread, onNext, onError, onComplete);
    }

    public void doGet(SSLParams params, String baseUrl, final String url, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                      Action onComplete) {
        Retrofit retrofit = builder.baseUrl(baseUrl)
                .client(getOkHttpClient().newBuilder().sslSocketFactory(params.sSLSocketFactory, params.trustManager).build())

                .build();
        RetrofitService get = retrofit.create(RetrofitService.class);
        get.get(url)
                //在子线程中执行数据请求
                .subscribeOn(Schedulers.newThread())
                //标记子线程
                .observeOn(Schedulers.io())
                //返回成功在子线程中执行
                .doOnNext(onNextAtIOThread)
                //在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                //分别在主线程中执行成功，失败，完成方法
                .subscribe(onNext, onError, onComplete);
    }

    /**
     * @param baseUrl 基础的url
     * @param url     url
     * @return Response<ResponseBody>
     */
    public void downloadFileWithDynamicUrlAsync(String baseUrl, final String url, final Callback<ResponseBody> callBack) {
        downloadFileWithDynamicUrlAsync(sslParams, baseUrl, url, callBack);
    }

    public void downloadFileWithDynamicUrlAsync(SSLParams params, String baseUrl, final String url, final Callback<ResponseBody> callBack) {

        Retrofit retrofit = builder.baseUrl(baseUrl)
                .client(getOkHttpClient().newBuilder().sslSocketFactory(params.sSLSocketFactory, params.trustManager).build())
                .build();
        final RetrofitService get = retrofit.create(RetrofitService.class);
        new AsyncTask<Void, Long, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Call call = get.downloadFileWithDynamicUrlAsync(url);
                    call.enqueue(callBack);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute();
    }

    /**
     * 设置拦截器
     *
     * @param interceptors
     */
    public void addInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public void setCache(Context context) {
        this.context = context;
    }

    /**
     * 网络缓存拦截器
     */
    private class CacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (NetworkUtil.isNetworkAvailable(context)) {
                Response response = chain.proceed(request);
                // read from cache for 0 s  有网络不会使用缓存数据
                int maxAge = 10;
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                //无网络时强制使用缓存数据
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Response response = chain.proceed(request);
                //set cahe times ; value is useless at all !
                int maxStale = 60 * 60 * 24 * 3;
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    }


    public void doPut(String baseUrl, final String url, RequestBody body, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                      Action onComplete) {
        doPut(sslParams, baseUrl, url, body, onNextAtIOThread, onNext, onError, onComplete);
    }

    public void doPut(SSLParams params, String baseUrl, final String url, RequestBody body, Consumer<? super String> onNextAtIOThread, Consumer<? super String> onNext, Consumer<? super Throwable> onError,
                      Action onComplete) {

        Retrofit retrofit = builder.baseUrl(baseUrl)
                .client(getOkHttpClient().newBuilder().sslSocketFactory(params.sSLSocketFactory, params.trustManager).build())
                .build();
        RetrofitService post = retrofit.create(RetrofitService.class);
        post.putJson(url, body)
                //在子线程中执行数据请求
                .subscribeOn(Schedulers.newThread())
                //标记子线程
                .observeOn(Schedulers.io())
                //返回成功在子线程中执行
                .doOnNext(onNextAtIOThread)
                //在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                //分别在主线程中执行成功，失败，完成方法
                .subscribe(onNext, onError, onComplete);
    }
}
