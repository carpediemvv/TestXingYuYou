package com.hyphenate.easeui.model;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojicon.Type;
import com.hyphenate.easeui.utils.EaseSmileUtils;

public class EaseDefaultEmojiconDatas {
    
    private static String[] emojis = new String[]{
        EaseSmileUtils.ee_1,
        EaseSmileUtils.ee_2,
        EaseSmileUtils.ee_3,
        EaseSmileUtils.ee_4,
        EaseSmileUtils.ee_5,
        EaseSmileUtils.ee_6,
        EaseSmileUtils.ee_7,
        EaseSmileUtils.ee_8,
        EaseSmileUtils.ee_9,
        EaseSmileUtils.ee_10,
        EaseSmileUtils.ee_11,
        EaseSmileUtils.ee_12,
        EaseSmileUtils.ee_13,
        EaseSmileUtils.ee_14,
        EaseSmileUtils.ee_15,
        EaseSmileUtils.ee_16,
        EaseSmileUtils.ee_17,
        EaseSmileUtils.ee_18,
        EaseSmileUtils.ee_19,
        EaseSmileUtils.ee_20,
        EaseSmileUtils.ee_21,
        EaseSmileUtils.ee_22,
        EaseSmileUtils.ee_23,
        EaseSmileUtils.ee_24,
        EaseSmileUtils.ee_25,
        EaseSmileUtils.ee_26,
        EaseSmileUtils.ee_27,
        EaseSmileUtils.ee_28,
        EaseSmileUtils.ee_29,
        EaseSmileUtils.ee_30,
        EaseSmileUtils.ee_31,
        EaseSmileUtils.ee_32,
        EaseSmileUtils.ee_33,
        EaseSmileUtils.ee_34,
        EaseSmileUtils.ee_35,
        EaseSmileUtils.ee_36,
            EaseSmileUtils.ee_37,
            EaseSmileUtils.ee_38,
            EaseSmileUtils.ee_39,
            EaseSmileUtils.ee_40,
            EaseSmileUtils.ee_41,
            EaseSmileUtils.ee_42,
            EaseSmileUtils.ee_43,
            EaseSmileUtils.ee_44,
            EaseSmileUtils.ee_45,
            EaseSmileUtils.ee_46,
            EaseSmileUtils.ee_47,
            EaseSmileUtils.ee_48,
            EaseSmileUtils.ee_49,
            EaseSmileUtils.ee_50,
            EaseSmileUtils.ee_51,
            EaseSmileUtils.ee_52,
            EaseSmileUtils.ee_53,
            EaseSmileUtils.ee_54,
            EaseSmileUtils.ee_55,
            EaseSmileUtils.ee_56,
            EaseSmileUtils.ee_57,
            EaseSmileUtils.ee_58,
            EaseSmileUtils.ee_59,
            EaseSmileUtils.ee_60,
            EaseSmileUtils.ee_61,
            EaseSmileUtils.ee_62,
            EaseSmileUtils.ee_63,
            EaseSmileUtils.ee_64,
            EaseSmileUtils.ee_65,
            EaseSmileUtils.ee_66,
            EaseSmileUtils.ee_67,
            EaseSmileUtils.ee_68,
            EaseSmileUtils.ee_69,
            EaseSmileUtils.ee_70,
    };
    
    private static int[] icons = new int[]{
        R.drawable.e_1,
        R.drawable.e_2,
        R.drawable.e_3,
        R.drawable.e_4,
        R.drawable.e_5,
        R.drawable.e_6,
        R.drawable.e_7,
        R.drawable.e_8,
        R.drawable.e_9,
        R.drawable.e_10,
        R.drawable.e_11,
        R.drawable.e_12,
        R.drawable.e_13,
        R.drawable.e_14,
        R.drawable.e_15,
        R.drawable.e_16,
        R.drawable.e_17,
        R.drawable.e_18,
        R.drawable.e_19,
        R.drawable.e_20,
        R.drawable.e_21,
        R.drawable.e_22,
        R.drawable.e_23,
        R.drawable.e_24,
        R.drawable.e_25,
        R.drawable.e_26,
        R.drawable.e_27,
        R.drawable.e_28,
        R.drawable.e_29,
        R.drawable.e_30,
        R.drawable.e_31,
        R.drawable.e_32,
        R.drawable.e_33,
        R.drawable.e_34,
        R.drawable.e_35,
        R.drawable.e_36,
            R.drawable.e_37,
            R.drawable.e_38,
            R.drawable.e_39,
            R.drawable.e_40,
            R.drawable.e_41,
            R.drawable.e_42,
            R.drawable.e_43,
            R.drawable.e_44,
            R.drawable.e_45,
            R.drawable.e_46,
            R.drawable.e_47,
            R.drawable.e_48,
            R.drawable.e_49,
            R.drawable.e_50,
            R.drawable.e_51,
            R.drawable.e_52,
            R.drawable.e_53,
            R.drawable.e_54,
            R.drawable.e_55,
            R.drawable.e_56,
            R.drawable.e_57,
            R.drawable.e_58,
            R.drawable.e_59,
            R.drawable.e_60,
            R.drawable.e_61,
            R.drawable.e_62,
            R.drawable.e_63,
            R.drawable.e_64,
            R.drawable.e_65,
            R.drawable.e_66,
            R.drawable.e_67,
            R.drawable.e_68,
            R.drawable.e_69,
            R.drawable.e_70,
    };
    
    
    private static final EaseEmojicon[] DATA = createData();
    
    private static EaseEmojicon[] createData(){
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], emojis[i], Type.NORMAL);
        }
        return datas;
    }
    
    public static EaseEmojicon[] getData(){
        return DATA;
    }
}
