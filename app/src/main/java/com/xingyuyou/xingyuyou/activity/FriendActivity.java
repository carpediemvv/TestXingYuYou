package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.xingyuyou.xingyuyou.Dao.APPConfig;
import com.xingyuyou.xingyuyou.Dao.HxEaseuiHelper;
import com.xingyuyou.xingyuyou.Dao.SharedPreferencesUtils;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.PinyinUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.FriendAdapter;
import com.xingyuyou.xingyuyou.views.ContactSortModel;
import com.xingyuyou.xingyuyou.views.EditTextWithDel;
import com.xingyuyou.xingyuyou.views.PinyinComparator;
import com.xingyuyou.xingyuyou.views.SideBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class FriendActivity extends AppCompatActivity {
    private ArrayList<ContactSortModel.DataBean> FansListBean= new ArrayList<>();
    private ArrayList<ContactSortModel.DataBean> SourceDateList= new ArrayList<>();
    private int PAGENUM = 1;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private FriendAdapter adapter;
    private EditTextWithDel mEtSearchName;
    private Toolbar mToolbar;
    private ArrayList<ContactSortModel.DataBean> SourceDateListt;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    //Toast.makeText(FriendActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    FansListBean = gson.fromJson(ja.toString(),
                            new TypeToken<List<ContactSortModel.DataBean>>() {
                            }.getType());
                    SourceDateList.addAll(FansListBean);
                    setAdapter();
                    adapter.imageSetOnclick(new FriendAdapter.ImageInterface() {
                        @Override
                        public void onclick(int position) {
                            Intent intent = new Intent(FriendActivity.this, OtherPageActivity.class);
                         //  Toast.makeText(FriendActivity.this,SourceDateListt.get(position).getNickname()+SourceDateListt.get(position).getId(),Toast.LENGTH_SHORT).show();
                            //Toast.makeText(FriendActivity.this,((ContactSortModel.DataBean) adapter.getItem(position)).getNickname()+"id"+((ContactSortModel.DataBean) adapter.getItem(position)).emchat_id+"imag"+((ContactSortModel.DataBean) adapter.getItem(position)).getHead_image(),Toast.LENGTH_LONG).show();
                            intent.putExtra("re_uid", ((ContactSortModel.DataBean) adapter.getItem(position)).getId());
                            startActivity(intent);
                        }
                    });
                    //根据输入框输入值的改变来过滤搜索
                    mEtSearchName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                            filterData(s.toString(),SourceDateListt);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    if (SourceDateList.size()<20) {
                       // Toast.makeText(FriendActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                        mPbNodata.setVisibility(View.GONE);
                        mTvNodata.setText("已经没有更多数据");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //更新UI
                if (adapter != null)
                    adapter.notifyDataSetChanged();

                //ListView的点击事件
                sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //    Toast.makeText(getApplication(),SourceDateList.get(position).getNickname(),Toast.LENGTH_SHORT).show();
                        List<EaseUser> users=new ArrayList<EaseUser>();
                        Map<String, EaseUser> contactLists = HxEaseuiHelper.getInstance().demoModel.getContactList();
                        if(contactLists.size()!=0||contactLists!=null){
                            for(String str:contactLists.keySet()){
                                EaseUser easeUser= contactLists.get(str);
                                users.add(easeUser);
                            }
                        }
                        //存入数据库
                        EaseUser easeUser=new EaseUser(((ContactSortModel.DataBean) adapter.getItem(position)).emchat_id);
                        easeUser.setAvatar(((ContactSortModel.DataBean) adapter.getItem(position)).head_image);
                        easeUser.setNick(((ContactSortModel.DataBean) adapter.getItem(position)).nickname);
                        easeUser.setNickname(((ContactSortModel.DataBean) adapter.getItem(position)).nickname);
                        users.add(easeUser);
//                            dao.saveContactList(users);
                        HxEaseuiHelper.getInstance().demoModel.saveContactList(users);
                        HxEaseuiHelper.getInstance().demoModel.setContactSynced(true);
                        Map<String, EaseUser> contactList = HxEaseuiHelper.getInstance().demoModel.getContactList();
                        Intent intent = new Intent(FriendActivity.this, ECChatActivity.class);
                        // EaseUI封装的聊天界面需要这两个参数，聊天者的username，以及聊天类型，单聊还是群聊
                        SharedPreferencesUtils.setParam(FriendActivity.this, APPConfig.USER_NAME, UserUtils.getNickName());
                        //设置要发送出去的头像
                        SharedPreferencesUtils.setParam(FriendActivity.this,APPConfig.USER_HEAD_IMG,UserUtils.getUserPhoto());
                        intent.putExtra("userId", ((ContactSortModel.DataBean) adapter.getItem(position)).emchat_id);
                        intent.putExtra("name",((ContactSortModel.DataBean) adapter.getItem(position)).nickname);
                        intent.putExtra("headImage",((ContactSortModel.DataBean) adapter.getItem(position)).head_image);
                        intent.putExtra("chatType", EMMessage.ChatType.Chat);
                        intent.putExtra("re_uid",((ContactSortModel.DataBean) adapter.getItem(position)).emchat_id);
                        startActivity(intent);
                    }
                });
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        initToolBar();
        initDatas();
        initViews();
    }

    private void initViews() {
        mEtSearchName = (EditTextWithDel) findViewById(R.id.et_search);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sortListView = (ListView) findViewById(R.id.lv_contact);
        View loadingData = View.inflate(FriendActivity.this, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        TextView textView = new TextView(FriendActivity.this);
        textView.setText("");
        sortListView.setDividerHeight(0);
      //  sortListView.addFooterView(loadingData);
        initEvents();
    }
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setAdapter() {
        SourceDateListt=filledData(SourceDateList);
        Collections.sort(SourceDateListt, new PinyinComparator());
        adapter = new FriendAdapter(FriendActivity.this, SourceDateListt);
        sortListView.setAdapter(adapter);

    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if(adapter.getCount()!=0||adapter!=null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        sortListView.setSelection(position);
                    }
                }
            }
        });



    }

    private void initDatas() {
       //sideBar.setTextView(dialog);
        OkHttpUtils.post()//
                .addParams("page","1")
                .addParams("uid",UserUtils.getUserId())
                .url(XingYuInterface.FRIEND_LIST)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr,ArrayList<ContactSortModel.DataBean> date) {
        List<ContactSortModel.DataBean> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = date;
        } else {
           mSortList.clear();
            for (ContactSortModel.DataBean sortModel : date) {
                String name = sortModel.getNickname();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<ContactSortModel.DataBean> filledData(ArrayList<ContactSortModel.DataBean> date) {
        ArrayList<ContactSortModel.DataBean> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();
        if (date != null || date.size() > 0) {
        for (int i = 0; i < date.size(); i++) {
                ContactSortModel.DataBean sortModel = new ContactSortModel.DataBean();
                sortModel.setNickname(date.get(i).nickname);
                sortModel.setHead_image(date.get(i).head_image);
                sortModel.setId(date.get(i).id);
                sortModel.setEmchat_id(date.get(i).emchat_id);
                String pinyin = PinyinUtils.getPingYin(date.get(i).nickname);
                String sortString = pinyin.substring(0, 1).toUpperCase();
                if (!sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters("#");
                    if (!indexString.contains("#")) {
                        indexString.add("#");
                    }
                } else if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString);
                    if (!indexString.contains(sortString)) {
                        indexString.add(sortString);
                    }
                }
                mSortList.add(sortModel);
            }
            Collections.sort(indexString);
            sideBar.setIndexText(indexString);
            sideBar.setVisibility(View.VISIBLE);
        }
        return mSortList;
    }

}
