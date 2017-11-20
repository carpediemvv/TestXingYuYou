package com.xingyuyou.xingyuyou.login.api;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by 24002 on 2017/7/10.
 */

public interface ToLogin {
    @POST("User/user_login_test")
    Observable<String> tologin(@Body String code);
}
