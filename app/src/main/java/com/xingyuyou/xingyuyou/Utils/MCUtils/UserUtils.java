package com.xingyuyou.xingyuyou.Utils.MCUtils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;

/**
 * Created by Administrator on 2017/3/28.
 */

public class UserUtils {
    private static SPUtils user_data;
    private static final String spFileName = "app";
    private static UserUtils instance = null;

    private UserUtils() {

    }
    public static UserUtils getInstance() {
        if (instance == null) {
            synchronized (UserUtils.class) {
                if (instance == null) {
                    instance = new UserUtils();
                }
            }
        }
        return instance;
    }

    public static void clearAllData(){
        user_data = new SPUtils("user_data");
        user_data.clear();
    }

    public static boolean logined() {
        user_data = new SPUtils("user_data");
        String id = user_data.getString("id");
        String account = user_data.getString("account");
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(account)) {
            return false;
        } else {
            return true;
        }
    }

    public static void LoginOut() {
        user_data = new SPUtils("user_data");
        user_data.putString("id", null);
        user_data.putString("account", null);
        user_data.putString("nickname", null);
    }

    public static void Login(String id, String account, String nickname) {
        user_data = new SPUtils("user_data");
        user_data.putString("id", id);
        user_data.putString("account", account);
        user_data.putString("nickname", nickname);
    }
    public static void Login(String id, String account, String nickname,String userPhoto) {
        user_data = new SPUtils("user_data");
        user_data.putString("id", id);
        user_data.putString("account", account);
        user_data.putString("nickname", nickname);
        user_data.putString("userPhoto", userPhoto);
    }

    public static String getUserId() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String id = user_data.getString("id");
            return id;
        }
        return "0";
    }

    public static String getUserAccount() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String account = user_data.getString("account");
            return account;
        }
        return "未登录";
    }
    public static void setEmid(String emid) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putString("user_emid", emid);
        }
    }
    public static String getEmid() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String emid = user_data.getString("user_emid");
            return emid;
        }
        return "0";
    }

    public static void setEmchat_zh(String Emchat_id) {
            user_data = new SPUtils("user_data");
            user_data.putString("user_Emchat_id", Emchat_id);
    }

    //环信账号
    public static String getEmchat_zh() {
            user_data = new SPUtils("user_data");
            String user_Emchat_id = user_data.getString("user_Emchat_id");
            return user_Emchat_id;
    }

    public static void setEmchat_pw(String Emchat_pw) {
            user_data = new SPUtils("user_data");
            user_data.putString("user_Emchat_pw", Emchat_pw);
    }

    //环信密码
    public static String getEmchat_pw() {
            user_data = new SPUtils("user_data");
            String Emchat_pw = user_data.getString("user_Emchat_pw");
            return Emchat_pw;
    }


    public static void setNickName(String nickName) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putString("nickname", nickName);
        }
    }
    public static String getNickName() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String nickname = user_data.getString("nickname");
            return nickname;
        }
        return "请在个人信息中设置";
    }

    public static void setUserPhoto(String userPhoto) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putString("userPhoto", userPhoto);
        }
    }

    public static String getUserPhoto() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String userPhoto = user_data.getString("userPhoto","userPhoto");
            return userPhoto;
        }
        return "未登录";
    }
    public static void setAllSystemMessageCount(String count) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putString("systemMessageCount", count);
        }
    }
    public static String getAllSystemMessageCount() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String messageCount = user_data.getString("systemMessageCount","null");
            return messageCount;
        }
        return "null";
    }
    public static void setAllPostMessageCount(String count) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putString("postMessageCount", count);
        }
    }
    public static String getAllPostMessageCount() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String messageCount = user_data.getString("postMessageCount","null");
            return messageCount;
        }
        return "null";
    }
    public static void setUpdateSystemMessageCount(String count) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putString("UpdateSystemMessageCount", count);
        }
    }
    public static String getUpdateSystemMessageCount() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String messageCount = user_data.getString("UpdateSystemMessageCount","1");
            return messageCount;
        }
        return "1";
    }
    public static void setUpdatePostMessageCount(String count) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putString("UpdatePostMessageCount", count);
        }
    }
    public static String getUpdatePostMessageCount() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String messageCount = user_data.getString("UpdatePostMessageCount","1");
            return messageCount;
        }
        return "1";
    }
    public static void setAllSystemMessageStatus(Boolean b) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putBoolean("systemMessageStatus", b);
        }
    }
    public static Boolean getAllSystemMessageStatus() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            Boolean userPhoto = user_data.getBoolean("systemMessageStatus",false);
            return userPhoto;
        }
        return false;
    }
    public static void setAllPostMessageStatus(Boolean b) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putBoolean("postMessageStatus", b);
        }
    }
    public static Boolean getAllPostMessageStatus() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            Boolean userPhoto = user_data.getBoolean("postMessageStatus",false);
            return userPhoto;
        }
        return false;
    }


    public static Boolean getBoolean(Context context, String strKey) {
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        Boolean result = setPreferences.getBoolean(strKey, false);
        return result;
    }

    public static void putBoolean(Context context, String strKey,
                                  Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.commit();
    }

}
