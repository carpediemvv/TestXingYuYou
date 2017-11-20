package com.hyphenate.easeui.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseTitleBar;

public abstract class EaseBaseFragment extends Fragment{
    protected EaseTitleBar titleBar;
    protected InputMethodManager inputMethodManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
       titleBar = (EaseTitleBar) getView().findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(getResources().getColor(R.color.other_pink));
       //titleBar.setLeftImageResource(R.drawable.ic_action_back);
        initView();
        setUpView();
        back();
    }
    
    public void showTitleBar(){
        if(titleBar != null){
            titleBar.setVisibility(View.VISIBLE);
        }
    }
    
    public void hideTitleBar(){
        if(titleBar != null){
            titleBar.setVisibility(View.GONE);
        }
    }
    public void back(){
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected abstract void initView();
    
    protected abstract void setUpView();

}
