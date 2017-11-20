package com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api;


import com.xingyuyou.xingyuyou.bean.FenQuListBean;
import com.xingyuyou.xingyuyou.bean.theme.IsTopWellBean;
import com.xingyuyou.xingyuyou.bean.theme.NodataBean;
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

public interface Post {
    @POST("UserAdministration/Administration_posts_menu")
    @FormUrlEncoded
    Observable<FenQuListBean> getFenQulList(@Field("user_id") String uid,
                                            @Field("fid") String fid);

    @POST("UserAdministration/Administration_check_well_top")
    @FormUrlEncoded
    Observable<IsTopWellBean> IsTopOrWell(@Field("user_id") String uid,
                                          @Field("tid") String tid,
                                          @Field("fid") String fid);

    @POST("UserAdministration/Administration_posts_move")
    @FormUrlEncoded
    Observable<NodataBean> postsMove(@Field("user_id") String uid,
                                     @Field("tid") String tid,
                                     @Field("fid") String fid,
                                     @Field("menu_id") String mid);

    @POST("UserAdministration/Administration_posts_delete")
    @FormUrlEncoded
    Observable<NodataBean> postsDelete(@Field("user_id") String uid,
                                       @Field("tid") String tid,
                                       @Field("fid") String fid,
                                       @Field("type") String type);

    @POST("UserAdministration/Administration_top_well")
    @FormUrlEncoded
    Observable<NodataBean> postsTopWell(@Field("user_id") String uid,
                                        @Field("tid") String tid,
                                        @Field("fid") String fid,
                                        @Field("type") String type);



}
