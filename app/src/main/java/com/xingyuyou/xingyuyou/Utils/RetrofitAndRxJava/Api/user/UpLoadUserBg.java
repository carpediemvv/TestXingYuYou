package com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.user;

import com.xingyuyou.xingyuyou.bean.DefultData;
import com.xingyuyou.xingyuyou.bean.user.UserInfo;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by 24002 on 2017/7/10.
 */

public interface UpLoadUserBg {
    //单张图片上传
    @Multipart
    @POST("UserCenter/update_back_ground")
    Observable<DefultData> updateImage(@Part MultipartBody.Part file);

    //多张图片上传
    @Multipart
    @POST("UserCenter/update_back_ground")
    Observable<DefultData> updateImage(@Part MultipartBody.Part[] file, @Part("uid") RequestBody token);


}
