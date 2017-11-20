package com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api;


import com.xingyuyou.xingyuyou.bean.theme.NodataBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 24002 on 2017/7/10.
 */

public interface AddThemeCircle {
    @POST("FansSystem/set_fans")
    @FormUrlEncoded
    Observable<NodataBean> addThemeCircle(@Field("uid") String uid,
                                          @Field("re_uid") String re_uid,
                                          @Field("relation") String relation,
                                          @Field("type") String type);
}
