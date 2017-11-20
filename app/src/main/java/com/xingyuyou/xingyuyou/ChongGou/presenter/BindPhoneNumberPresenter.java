package com.xingyuyou.xingyuyou.ChongGou.presenter;

import android.util.Log;

import com.xingyuyou.xingyuyou.ChongGou.model.BindPhoneNumberModel;
import com.xingyuyou.xingyuyou.ChongGou.model.IBindPhoneNumberModel;
import com.xingyuyou.xingyuyou.ChongGou.view.IBindPhoneNumberView;

/**
 * Created by 24002 on 2017/8/23.
 */

public class BindPhoneNumberPresenter implements IBindPhoneNumberModel.OnGetRegisterCodeListener {
    private IBindPhoneNumberView mIBindPhoneNumberView;
    private IBindPhoneNumberModel mBindPhoneNumberModel;

    public BindPhoneNumberPresenter(IBindPhoneNumberView View) {
        mIBindPhoneNumberView = View;
        mBindPhoneNumberModel = new BindPhoneNumberModel();
    }

    public void getRegisterCode(String phoneNumber) {
        mBindPhoneNumberModel.getRegisterCode(phoneNumber, this);
    }

    public void BindPhoneNumberCode(String phoneNumber, String code, String password) {

    }

    @Override
    public void onRegisterCodeError(String msg) {
        if (mIBindPhoneNumberView != null) {
            mIBindPhoneNumberView.showToastMessage(msg);
        }
    }

    @Override
    public void onSuccess(String msg) {
        if (mIBindPhoneNumberView != null) {
            mIBindPhoneNumberView.showToastMessage(msg);
        }
    }
    public void onDestroy() {
        mIBindPhoneNumberView=null;
    }
}
