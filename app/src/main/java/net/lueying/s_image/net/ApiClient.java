package com.gongyujia.app.api;



import com.gongyujia.app.core.App;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by jt on 2016/10/26.
 */
public class ApiClient {

    private static final int DEFAULT_TIMEOUT = 10000;
    private Retrofit retrofit;
    private ApiService apiService;

    private static ApiClient apiClient = new ApiClient();

    public static ApiClient getInstance() {
        return apiClient;
    }

    private ApiClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        httpClientBuilder.readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        //失败重连
        httpClientBuilder.retryOnConnectionFailure(true);
        //设置缓存路径
        File httpCacheDirectory = new File(App.getApplication().getCacheDir(), "HttpCache");
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        httpClientBuilder.cache(cache);
        //设置拦截器
        httpClientBuilder.addInterceptor(new HttpInterceptor());
//        CacheInterceptor cacheInterceptor = new CacheInterceptor();
//        httpClientBuilder.addNetworkInterceptor(cacheInterceptor);
//        httpClientBuilder.addInterceptor(cacheInterceptor);
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(ApiService.BASE_URL)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

}
