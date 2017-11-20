package com.xingyuyou.xingyuyou.ChongGou.view;

/**
 * Created by 24002 on 2017/8/23.
 */

public interface IBindPhoneNumberView {
    String getPhoneNumber();
    String getRegisterCode();
    String getPassword();
    String getPasswordAgain();
    void  showToastMessage(String  message);
}
