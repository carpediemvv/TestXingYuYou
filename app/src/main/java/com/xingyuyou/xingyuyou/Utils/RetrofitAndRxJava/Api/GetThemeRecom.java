package com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api;


import com.xingyuyou.xingyuyou.bean.theme.ThemeRecom;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 24002 on 2017/7/10.
 */

public interface GetThemeRecom {
    @POST("Theme/theme_recommend")
    @FormUrlEncoded
    Observable<ThemeRecom> getThemeRecomData(@Field("uid") String uid);
}
