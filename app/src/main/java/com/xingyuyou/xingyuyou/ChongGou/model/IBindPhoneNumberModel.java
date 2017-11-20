package com.xingyuyou.xingyuyou.ChongGou.model;

/**
 * Created by 24002 on 2017/8/23.
 */

public interface IBindPhoneNumberModel {
    interface OnGetRegisterCodeListener{
        void onRegisterCodeError(String msg);
        void onSuccess(String msg);
    }

    String getRegisterCode(String code,OnGetRegisterCodeListener listener);

    String BindPhoneNumber(String phoneNumber, String code, String password,OnGetRegisterCodeListener listener);
}
