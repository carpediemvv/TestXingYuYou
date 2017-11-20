package com.xingyuyou.xingyuyou.login.presenter;

import android.util.Log;

import com.xingyuyou.xingyuyou.login.model.ILoginModel;
import com.xingyuyou.xingyuyou.login.model.loginModel;
import com.xingyuyou.xingyuyou.login.view.ILoginView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 24002 on 2017/9/1.
 */

public class LoginPresenter {
    ILoginView mILoginView;
    private final ILoginModel mILoginModel;

    public LoginPresenter(ILoginView view) {
        mILoginView=view;
        mILoginModel = new loginModel();
    }

    public void toLogin(String phoneNumber,String password) {
        Log.e("bindlogin", "111111toLogin: "+ phoneNumber+"--"+password);
        mILoginModel.toLogin(phoneNumber,password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("bindlogin", "onSubscribe: " );
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.e("bindlogin", "onNext: "+s );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("bindlogin", "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("bindlogin", "onComplete: " );
                    }
                });
    }
}
