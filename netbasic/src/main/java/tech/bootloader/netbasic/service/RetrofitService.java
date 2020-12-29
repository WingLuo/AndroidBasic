package tech.bootloader.netbasic.service;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/7/5.
 */

public interface RetrofitService {

//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//        @POST("{method}")
    @POST
    Observable<String> postJson(
//                @Path("method") String method,
            @Url String url,
            @Body RequestBody body
    );
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//        @POST("{method}")
    @POST
    @FormUrlEncoded
    Observable<String> postMap(
            @Url String url,
            @FieldMap Map<String, String> maps
    );

    @PUT
    Observable<String> putJson(
            @Url String url,
            @Body RequestBody body
    );
//    @Headers({"Content-Type: multipart/form-data", "Accept: application/json"})
    @POST
    Observable<String> postFile(
            @Url String url,
            @Body RequestBody body
    );

    @Multipart
    @POST
    Observable<String> postFileFromData(
            @Url String url,
            @PartMap Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> files
    );
    @Multipart
//    @Headers({"Content-Type: multipart/form-data", "Accept: application/json"})
    @POST
    Observable<String> postFile(
            @Url String url,
            @Part MultipartBody.Part body


    );

//    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET
    Observable<String> get(
            @Url String url,
            @QueryMap Map<String, Object> options
    );

//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @Headers({"Content-Type: multipart/form-data", "Accept: application/json"})
    @GET
    Observable<String> get(
            @Url String url
    );

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(
            @Url String fileUrl
    );
}
