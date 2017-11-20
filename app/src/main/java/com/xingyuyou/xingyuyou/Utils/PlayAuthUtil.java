package com.xingyuyou.xingyuyou.Utils;

import android.content.Context;
import android.widget.Toast;

import com.aliyun.vodplayer.utils.HttpClientUtil;

import org.json.JSONObject;

/**
 * Created by pengshuang on 31/08/2017.
 */

public class PlayAuthUtil {

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPlayAuth(Context context, String vid) {

        String strAuth = null;

        try {
            String response = HttpClientUtil.doHttpGet("http://30.27.92.84:9988/?playauth=1&vid=" + vid);
            String jsonAuthinfo = response.replaceAll("u'", "\"").replace("'", "\"");
            JSONObject authinfo = new JSONObject(jsonAuthinfo);
            strAuth = authinfo.get("PlayAuth").toString();

        } catch (Exception e) {
            if (context != null) {
                Toast.makeText(context, "通过网络实时获取authinfo解析失败", Toast.LENGTH_SHORT).show();
            }
        } finally {
            return strAuth;
        }

    }


}
