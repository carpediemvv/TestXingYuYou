package com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.user;

import com.xingyuyou.xingyuyou.bean.user.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by 24002 on 2017/7/10.
 */

public interface GetRegisterCode {
    @POST("User/send_sms")
    Observable<String> getRegisterCode(@Body String code);
}
