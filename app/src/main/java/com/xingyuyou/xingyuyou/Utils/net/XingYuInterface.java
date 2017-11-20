package com.xingyuyou.xingyuyou.Utils.net;


public class XingYuInterface {

    public static final String Host="http://xingyuyou.com/app.php/";
    public static final String VERSION_UPDATE=Host+"SystemHint/version_update";
    public static final String USER_REGISTER=Host+"User/user_phone_register";
    public static final String USER_LOGIN=Host+"User/user_login";
    public static final String USER_LOGIN_TEST=Host+"User/user_login_test";
    public static final String USER_INFO=Host+"User/user_info";
    public static final String USER_UPDATE_DATA=Host+"User/user_update_data";
    public static final String FORGET_PASSWORD=Host+"User/forget_password";
    public static final String SEND_SMS=Host+"User/send_sms";
    public static final String USER_DEPOSIT_RECORD=Host+"User/user_deposit_record";
    public static final String GET_GAME_DETAILS=Host+"Server/get_game_details";
    public static final String GET_GAME_LIST=Host+"Server/get_game_list";
    public static final String GET_GAME_CLASS=Host+"Community/get_game_class";
    public static final String GAME_CATEGORY=Host+"Server/game_category";
    public static final String ROTATION_IMG=Host+"Server/rotation_img";
    public static final String GET_GIFT_LIST=Host+"Server/get_gift_list";
    public static final String MY_GIFT=Host+"Server/my_gift";
    public static final String RCEIVE_GIFT=Host+"Server/receive_gift";
    public static final String ABOUT_US=Host+"Server/about_us";
    public static final String UPDATA_DOWN=Host+"Server/updata_down";
    public static final String GET_EVALUATE=Host+"Server/get_evaluate";
    public static final String GET_EVALUATE_LAUD=Host+"Server/get_evaluate_laud";
    public static final String GET_EVALUATE_LIST=Host+"Server/get_evaluate_list";
    public static final String GET_GAME_NAME_LIST=Host+"Server/get_game_name_list";
    public static final String GET_OTHERMESSAGE=Host+"UserCenter/get_user_info";
    public static final String GET_OTHERCONCERN=Host+"FansSystem/set_fans";
    public static final String GET_USER_REPORT=Host+"FansSystem/user_report";
    public static final String GET_OTHERCONCERN_LIST=Host+"FansSystem/get_follow_list";
    public static final String GAME_GIFT_LIST=Host+"Server/game_gift_list";
    public static final String GET_LABEL_CLASS=Host+"Community/get_label_class";
    public static final String GET_LABEL_CLASSS=Host+"Forum/get_forum_theme_list";
    //更换接口
    public static final String GET_POSTS_LIST=Host+"Test/get_posts_list";
    public static final String GET_POSTS_LISTT=Host+"Test/get_forum_theme_list";
    public static final String POSTS_GET_POSTS_LIST=Host+"Posts/get_posts_list";
    public static final String SEARCH_FORUM_LABEL=Host+"Test/search_forum_label";//标签页面用户搜索的标签
    public static final String GET_POSTS_INFO=Host+"Community/get_posts_info";
    public static final String GET_POSTS_CLASS_LIST=Host+"Community/get_posts_class_list";
    public static final String POST_POSTS=Host+"TextCommunity/post_posts";
    //短视频发帖
    public static final String POST_POSTS_TEST=Host+"Community/post_posts_test";
    public static final String SHARE_COMPLETE=Host+"Community/share_complete";
    public static final String FRIEND_LIST=Host+"FansSystem/user_friends";
    public static final String COMMUNITY=Host+"Theme/forum_theme_search_list";
    public static final String POPULAR_TAGS=Host+"Community/popular_tags";
    public static final String REPLIES=Host+"Community/replies";
    public static final String GET_FORUMS_LIST=Host+"Community/get_forums_list";
    public static final String LEAVING_MESSAGE=Host+"LeavingMessage/message_list";
    public static final String LEAVING_ADD=Host+"LeavingMessage/message_add";
    public static final String MESSAGE_DELETE=Host+"LeavingMessage/message_delete";
    public static final String LEAVING_CONTENT=Host+"LeavingMessage/message_list_info";
    public static final String GET_LAUD=Host+"Community/get_laud";
    public static final String GET_COLLECT=Host+"Community/get_collect";
    public static final String GET_RECOMMEND=Host+"Community/get_recommend";
    public static final String REPLIES_LAUD=Host+"Community/replies_laud";
    public static final String USER_SIGN=Host+"Community/user_sign";//用户签到
    public static final String GET_SEARCH_POPULAR_TAGS=Host+"Community/get_search_popular_tags";//社区热门标签搜索
    public static final String DELETE_MY_POSTS=Host+"Community/delete_posts";//我的帖子删除帖子
    public static final String GET_FORUMS_INFO=Host+"Community/get_forums_info";//帖子评论的详情页面
    public static final String UPDATE_BACK_GROUND=Host+"UserCenter/update_back_ground";
    public static final String GET_CHOOSE=Host+"UserCenter/boot_page";
    public static final String UPDATE_INFORMATION=Host+"UserCenter/update_information";
    public static final String GET_USER_INFO=Host+"UserCenter/get_user_info";
    public static final String OWN_POST_LIST=Host+"UserCenter/own_post_list";//我的帖子
    public static final String GET_MYNEWS_LIST=Host+"UserCenter/get_mynews_list";//我的消息
    public static final String USER_FEEDBACK=Host+"UserCenter/user_feedback";
    public static final String COLLECT_LIST=Host+"UserCenter/collect_list";//帖子收藏
    public static final String MY_REPLIES_LIST=Host+"UserCenter/my_replies_list";//我的评论
    public static final String PRIVATE_LETTER_LIST=Host+"UserCenter/private_letter_list";//私信列表
    public static final String PRIVATE_LETTER_ADD=Host+"UserCenter/private_letter_add";//用户回复私信
    public static final String PRIVATE_LETTER_READ=Host+"UserCenter/private_letter_read";//私信最新消息判断
    public static final String GET_SEARCH_FORUMS=Host+"UserCenter/get_search_forums";//社区帖子关键字搜索接口
    public static final String LABEL_SEARCH=Host+"UserCenter/label_search";//社区帖子关键字搜索接口
    public static final String RELATION_ACCOUNT=Host+"UserCenter/relation_account";//第三方绑定展示状态接口
    public static final String UPDATE_RELATION_ACCOUNT=Host+"UserCenter/update_relation_account";//（1.0）用户绑定接口
    public static final String GET_GODLIST=Host+"GodForum/get_godlist";//神社轮播图接口
    public static final String GET_GOD_FORUMS=Host+"GodForum/get_god_forums";//神社活动回帖列表
    public static final String GET_ACTIVITY_LIST=Host+"GodForum/get_activity_list";//神社活动列表
    public static final String GET_GODINFO=Host+"GodForum/get_godinfo";//神社详情接口
    public static final String GET_ACTIVITY_INFO=Host+"GodForum/get_activity_info";//神社详情接口
    public static final String SYSTEM_CONTENT_LIST=Host+"SystemHint/system_content_list";//（1.1）系统消息接口
    public static final String SYSTEM_REQUEST_STATUS=Host+"SystemHint/system_request_status";//（1.1）系统消息消息轮循接口
    public static final String MANAGE_AUTH_DELETE=Host+"ManageAuth/manage_auth_delete";//（1.1）系统消息消息轮循接口


}
