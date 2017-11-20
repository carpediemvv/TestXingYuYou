package com.xingyuyou.xingyuyou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppConstants;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.ChooseAdapter;
import com.xingyuyou.xingyuyou.bean.ChooseBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class ChooseActivity extends AppCompatActivity {
    //数据源
    List<ChooseBean> list_posion=new ArrayList<>();
    //存储 数据
    List<ChooseBean> listMySelect=new ArrayList<>();

    private int[] images = {R.drawable.choose_1,R.drawable.choose_2,R.drawable.choose_3,R.drawable.choose_4,
            R.drawable.choose_5,R.drawable.choose_6,R.drawable.choose_7,
            R.drawable.choose_8,R.drawable.choose_9,R.drawable.choose_10,R.drawable.choose_11,
            R.drawable.choose_12,R.drawable.choose_13,R.drawable.choose_14,R.drawable.choose_15};

    private int[] images_choose = {R.drawable.choosee_1,R.drawable.choosee_2,R.drawable.choosee_3,R.drawable.choosee_4,
            R.drawable.choosee_5,R.drawable.choosee_6,R.drawable.choosee_7,
            R.drawable.choosee_8,R.drawable.choosee_9,R.drawable.choosee_10,R.drawable.choosee_11,
            R.drawable.choosee_12,R.drawable.choosee_13,R.drawable.choosee_14,R.drawable.choosee_15};
    private String[] text = {"爱豆","cp","cos","逗图","番剧","恐怖","漫画","美食","日常","手办",
            "手绘","小说","影视","游戏","资讯",};
    private String[] possionname = {"39","40","8","10","9","16","37","36","11","12",
            "13","14","41","15","38",};
    private ImageView iv_gridView_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        final GridView choose_gridView= (GridView) findViewById(R.id.choose_gridView);
//list_posion
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        choose_gridView.setFocusable(false);
        ScrollView scroll_choose= (ScrollView) findViewById(R.id.scroll_choose);
        scroll_choose.smoothScrollTo(0,20);
        choose_gridView.setAdapter(new ChooseAdapter(this,images,text));
        final Button choose_button= (Button) findViewById(R.id.choose_button);
        choose_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View v=choose_gridView.getChildAt(i);
                iv_gridView_item = (ImageView)v.findViewById(R.id.iv_gridView_item);
                String s = iv_gridView_item.getTag().toString();
                if(s=="0"){
                    if(listMySelect.size()<3){
                        ChooseBean chooseBean =new ChooseBean();
                        chooseBean.setId(possionname[i]);
                        chooseBean.setForum_name(text[i]);
                        strings(chooseBean,i);
                        iv_gridView_item.setTag("1");
                    }else {
                        Toast.makeText(ChooseActivity.this, "亲，最多选3个", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    ChooseBean chooseBean =new ChooseBean();
                    chooseBean.setId(possionname[i]);
                    chooseBean.setForum_name(text[i]);
                    delString(chooseBean);
                    iv_gridView_item.setTag("0");
                    iv_gridView_item.setImageResource(images[i]);
                }
            }
        });


        choose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listMySelect.size()>=1){
                    enterMainActivity();
                    getData();
                }else {
                    Toast.makeText(ChooseActivity.this,"请最少选择一个", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //如果删除集合的值
    public void delString(ChooseBean s){
        for (int i = 0; i < listMySelect.size(); i++) {
            if(listMySelect.get(i).getForum_name().equals(s.getForum_name())){
                listMySelect.remove(i);
            }
        }

        //  Toast.makeText(this, "删除"+stringList.size(), Toast.LENGTH_SHORT).show();
    }
    //记录选中值
    public void strings(ChooseBean chooseBean,int i){
        listMySelect.add(chooseBean);
        iv_gridView_item.setImageResource(images_choose[i]);


        //   Toast.makeText(this, "存储"+stringList.size(), Toast.LENGTH_SHORT).show();
    }
    private void enterMainActivity() {
        UserUtils.putBoolean(ChooseActivity.this, AppConstants.FIRST_choose, true);
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        UserUtils.putBoolean(ChooseActivity.this, AppConstants.FIRST_choose, true);
        finish();
    }
    public void getData(){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ChooseBean tag : listMySelect) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("forum_name", tag.getForum_name());
            map.put("id", tag.getId());
            list.add(map);
        }
        JSONArray array = new JSONArray(list);
        OkHttpUtils.post()//
                .addParams("uid", UserUtils.getUserId())
                .addParams("forum",array.toString())
                .url(XingYuInterface.GET_CHOOSE)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                    }
                });
    }
}
