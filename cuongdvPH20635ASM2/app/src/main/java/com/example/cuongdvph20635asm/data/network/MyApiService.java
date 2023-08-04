package com.example.cuongdvph20635asm.data.network;

import com.example.cuongdvph20635asm.data.model.Data;
import com.example.cuongdvph20635asm.data.model.GetAllImgResponse;
import com.example.cuongdvph20635asm.data.model.PostImgResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MyApiService {

    @POST("api/add")
    Observable<PostImgResponse> callAPIPostImgToMyServer(@Body Data imageRequest );

    @GET("api/")
    Observable<GetAllImgResponse> callAPIGetAllImgFromServer();

    @DELETE("api/delete/{id}")
    Observable<PostImgResponse> callAPIDeleteByID(@Path("id") String id);

    @PUT("/api/update/{id}")
    Observable<PostImgResponse> callAPIEditbyID(@Path("id") String id,@Body Data data);

    class Factory {
        private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .build();

        private static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.11:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        public static MyApiService create() {
            return retrofit.create(MyApiService.class);
        }
    }
}