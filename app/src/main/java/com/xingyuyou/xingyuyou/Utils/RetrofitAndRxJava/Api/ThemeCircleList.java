package com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api;


import com.xingyuyou.xingyuyou.bean.theme.ThemeCircleDetailBean;
import com.xingyuyou.xingyuyou.bean.theme.ThemeCircleListBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by 24002 on 2017/7/10.
 */

public interface ThemeCircleList {
    @GET("Theme/theme_list")
    Observable<ThemeCircleListBean> getThemeCircleList();
    @POST("Theme/theme_info_list")
    @FormUrlEncoded
    Observable<ThemeCircleDetailBean> getThemeCircleDetailList(@Field("theme_class_id") String id);
}
