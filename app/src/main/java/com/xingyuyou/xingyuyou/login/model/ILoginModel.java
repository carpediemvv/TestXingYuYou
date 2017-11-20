package com.xingyuyou.xingyuyou.login.model;


import io.reactivex.Observable;

/**
 * Created by 24002 on 2017/9/1.
 */

public interface ILoginModel {
    Observable<String> toLogin(String usernameText, String passwordText);
}
