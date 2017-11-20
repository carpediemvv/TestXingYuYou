package com.xingyuyou.xingyuyou.Utils.utils;

import com.hyphenate.easeui.domain.EaseUser;

/**
 * Created by Administrator on 2017/8/19.
 */

public class RobotUser  extends EaseUser {
    public RobotUser(String username) {
        super(username.toLowerCase());
    }
}
