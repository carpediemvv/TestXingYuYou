package com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api;


import com.xingyuyou.xingyuyou.bean.theme.ThemeCircleItemData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 24002 on 2017/7/10.
 */

public interface ChangeThemeCircle {
    @POST("Theme/theme_list_change")
    @FormUrlEncoded
    Observable<ThemeCircleItemData> changeItemThemeCircle(@Field("theme_class_id") String id);
}
