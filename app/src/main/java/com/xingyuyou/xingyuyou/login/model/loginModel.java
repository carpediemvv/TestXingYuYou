package com.xingyuyou.xingyuyou.login.model;

import android.util.Base64;
import android.util.Log;

import com.xingyuyou.xingyuyou.App;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.Api.user.GetRegisterCode;
import com.xingyuyou.xingyuyou.Utils.RetrofitAndRxJava.RetrofitServiceManager;
import com.xingyuyou.xingyuyou.login.api.ToLogin;
import com.xingyuyou.xingyuyou.login.view.ILoginView;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by 24002 on 2017/9/1.
 */

public class loginModel implements ILoginModel {

    private final ToLogin mToLogin;

    public loginModel() {
        mToLogin = RetrofitServiceManager.getInstance()
                .create(ToLogin.class);
    }

    @Override
    public Observable<String> toLogin(String usernameText, String passwordText) {
        Log.e("bindlogin", "222222toLogin: "+ usernameText+"--"+passwordText);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account", String.valueOf(usernameText));
            jsonObject.put("password", String.valueOf(passwordText));
            jsonObject.put("device_token", App.DEVICETOKEN);
            jsonObject.put("uid", "1");
            jsonObject.put("name", "1");
            jsonObject.put("gender", "1");
            jsonObject.put("iconurl", "1");
            jsonObject.put("login_type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String encodeToString = android.util.Base64.encodeToString(jsonObject.toString().getBytes(), android.util.Base64.DEFAULT);

        return mToLogin.tologin(encodeToString);
    }
}
