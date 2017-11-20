package com.xingyuyou.xingyuyou.ChongGou.model;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.user.GetUserData;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.BindPhoneNumberActivity;
import com.xingyuyou.xingyuyou.bean.user.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 24002 on 2017/8/23.
 */

public class BindPhoneNumberModel implements IBindPhoneNumberModel {
    @Override
    public String getRegisterCode(String code,OnGetRegisterCodeListener listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", String.valueOf(code));
            jsonObject.put("demand", String.valueOf(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        POST(XingYuInterface.SEND_SMS,jsonObject.toString(),listener);

        return "";
    }
    public void POST(String url, String body, final OnGetRegisterCodeListener listener) {
        Log.e("xianchen", "POST: "+Thread.currentThread().getName() );
        RequestParams params = new RequestParams(url);
        String encodeToString = Base64.encodeToString(body.toString().getBytes(), Base64.DEFAULT);
        params.setBodyContent(encodeToString);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("POST错误信息", arg0.toString());
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("xianchen", "onSuccess: "+Thread.currentThread().getName() );
                try {
                    String result = new String(Base64.decode(json, Base64.DEFAULT), "utf-8");
                    Log.e("weiwei", "onSuccess: "+result );
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        String return_msg = jsonObject.getString("msg");
                        if (jsonObject.getString("return_code").equals("success")){
                            listener.onSuccess(return_msg);
                        }
                        if (jsonObject.getString("return_code").equals("fail")){
                            listener.onRegisterCodeError(return_msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    Log.e("POST+json成功回调出错：", e.toString());
                }
            }
            @Override
            public boolean onCache(String json) {
                return true;
            }
        });
    }
    @Override
    public String BindPhoneNumber(String phoneNumber, String code, String password,OnGetRegisterCodeListener listener) {
        return null;
    }
}
