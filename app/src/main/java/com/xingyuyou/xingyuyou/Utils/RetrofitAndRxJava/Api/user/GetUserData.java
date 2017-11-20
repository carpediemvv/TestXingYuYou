package com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.user;

import com.xingyuyou.xingyuyou.bean.user.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 24002 on 2017/7/10.
 */

public interface GetUserData {
    @POST("UserCenter/get_user_info")
    @FormUrlEncoded
    Observable<UserInfo> getUserData(@Field("uid") String uid,
                                 @Field("re_uid") String re_uid);
}
