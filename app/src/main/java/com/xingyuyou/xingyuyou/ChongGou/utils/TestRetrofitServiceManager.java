package com.xingyuyou.xingyuyou.ChongGou.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 24002 on 2017/7/10.
 */

public class TestRetrofitServiceManager {
    private  Retrofit mRetrofit;

    private TestRetrofitServiceManager() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://xingyuyou.com/app.php/")
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    private static class SingletonHolder{
        private static final TestRetrofitServiceManager INSTANCE = new TestRetrofitServiceManager();
    }
    /**
     * 获取RetrofitServiceManager
     * @return
     */
    public static TestRetrofitServiceManager getInstance(){
        return SingletonHolder.INSTANCE;
    }
    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }
}
