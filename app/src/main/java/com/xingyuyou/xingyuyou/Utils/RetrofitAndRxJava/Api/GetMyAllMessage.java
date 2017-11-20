package com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api;

import com.xingyuyou.xingyuyou.bean.user.MyAllMessageBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 24002 on 2017/7/10.
 */

public interface GetMyAllMessage {
    @POST("SystemHint/system_content_list")
    @FormUrlEncoded
    Observable<MyAllMessageBean> getMyAllMessage(@Field("uid") String uid,
                                                 @Field("page") String re_uid);
}
