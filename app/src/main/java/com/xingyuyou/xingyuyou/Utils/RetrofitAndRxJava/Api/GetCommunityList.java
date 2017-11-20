package com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api;


import com.xingyuyou.xingyuyou.bean.theme.CommunityListBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by 24002 on 2017/7/10.
 */

public interface GetCommunityList {
    @GET("Forum/get_forum_list")
    Observable<CommunityListBean> getCommunityList();
}
