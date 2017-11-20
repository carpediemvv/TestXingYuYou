package com.xingyuyou.xingyuyou.bean.user;

/**
 * Created by Administrator on 2017/10/26.
 */

public class IschooseBean {
    private String brand;
    private boolean isChecked;

    public IschooseBean(boolean isChecked) {
        this.isChecked = isChecked;
    }



    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
