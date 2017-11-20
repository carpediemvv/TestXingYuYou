package com.xingyuyou.xingyuyou.weight;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xingyuyou.xingyuyou.R;

/**
 * Created by 24002 on 2017/11/14.
 */

public class CustomPopupWindow extends PopupWindow {

    private int popupWidth;
    private int popupHeight;
    private View parentView;
    public CustomPopupWindow(Context context ) {
        super(context);
        initView(context);
        setPopConfig();
        initData(context);
    }
    /**
     *   初始化控件
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/29,22:00
     * <h3>UpdateTime</h3> 2016/6/29,22:00
     * <h3>CreateAuthor</h3> vera
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     * @param context
     */
    private void initView(Context context) {
        parentView = View.inflate(context, R.layout.popup_post_review_layout, null);
        setContentView(parentView);
    }

    /**
     *   初始化数据
     * <h3>Version</h3> 1.0
     * @param context
     */
    private void initData(Context context) {

    }
    /**
     *
     * 配置弹出框属性
     * @version 1.0
     * @createAuthor
     * @updateAuthor
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     */
    private void setPopConfig() {
//        this.setContentView(mDataView);//设置要显示的视图
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);// 设置外部触摸会关闭窗口

        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();
        popupWidth = parentView.getMeasuredWidth();
    }


    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     * @param v
     */
    public void showUp(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, 50, location[1] - popupHeight-20);
    }

    /**
     * 设置显示在v上方（以v的中心位置为开始位置）
     * @param v
     */
    public void showUp2(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
    }
}
