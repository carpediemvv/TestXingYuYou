package com.xingyuyou.xingyuyou.Utils.SoftKeyBoart;
/**
 * Created by xiaokai on 2017/02/07.
 * 字符串匹配表情
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpanStringUtils {

    /*
    * emotionNum 表情编号
    * */
    public static SpannableString getEmotionContent(final Context context, final TextView tv, String source) {
      SpannableStringBuilder multiWord = new SpannableStringBuilder();
        if(source.trim()!=null||source.length()!=0){
            String[] split = source.split(" ");
            for (int j = 0; j <split.length ; j++) {
                    SpannableStringBuilder style = new SpannableStringBuilder(split[j]);
                    if (split[j].trim().length() != 0) {
                        String substring = split[j].substring(0, 1);
                        if (substring.equals("@") || substring.equals("＠")) {
                            style.setSpan(new ForegroundColorSpan(android.graphics.Color.parseColor("#ff717c")), 0, split[j].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                           // multiWord.append(" ");
                            multiWord.append(style);
                            multiWord.append(" ");
                        } else {
                            style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, split[j].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            multiWord.append(style);
                            multiWord.append(" ");
                        }
                }else {
                        multiWord.append(" ");
                    }
            }
        }
        int emotionNum = 0;
        String smallOrBig = "";
        InputStream in = null;
        SpannableString spannableString = new SpannableString(multiWord);
        String regexEmotion = "\\[[s]:\\d+\\]|\\[[b]:\\d+\\]";//正则表达式规则 --> [d:数字]
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            emotionNum = Integer.valueOf(key.substring(3, key.indexOf("]")));//获取到表情的编号
            smallOrBig = key.substring(1, 2);
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            try {
                // 压缩表情图片
                int size = (int) tv.getTextSize() * 13 / 10;
                if (smallOrBig.equals("s")) {
                    in = context.getAssets().open("ems/small/" + String.valueOf(emotionNum) + ".png");
                    size = (int) tv.getTextSize() * 13 / 10;
                } else {
                    in = context.getAssets().open("ems/big/" + String.valueOf(emotionNum) + ".png");
                    size = (int) tv.getTextSize() * 25 / 10;
                }
                //bitmap
              /*  Bitmap bitmap = BitmapFactory.decodeStream(in);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);*/
                //drawable
                Drawable drawable = Drawable.createFromStream(in, null);
                drawable.setBounds(0, 0, size, size);
                ImageSpan span = new ImageSpan(drawable);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return spannableString;
    }


    public static SpannableString getEmotionpost(final Context context, final EditText tv, String source) {
        SpannableStringBuilder multiWord = new SpannableStringBuilder();
        if(source.trim()!=null||source.length()!=0){
            String[] split = source.split(" ");
            for (int j = 0; j <split.length ; j++) {
                SpannableStringBuilder style = new SpannableStringBuilder(split[j]);
                if (split[j].trim().length() != 0) {
                    String substring = split[j].substring(0, 1);
                    if (substring.equals("@") || substring.equals("＠")) {
                        style.setSpan(new ForegroundColorSpan(android.graphics.Color.parseColor("#ff717c")), 0, split[j].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // multiWord.append(" ");
                        multiWord.append(style);
                        multiWord.append(" ");
                    } else {
                        style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, split[j].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        multiWord.append(style);
                        multiWord.append(" ");
                    }
                }else {
                    multiWord.append(" ");
                }
            }
        }
        int emotionNum = 0;
        String smallOrBig = "";
        InputStream in = null;
        SpannableString spannableString = new SpannableString(multiWord);
        String regexEmotion = "\\[[s]:\\d+\\]|\\[[b]:\\d+\\]";//正则表达式规则 --> [d:数字]
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            emotionNum = Integer.valueOf(key.substring(3, key.indexOf("]")));//获取到表情的编号
            smallOrBig = key.substring(1, 2);
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            try {
                // 压缩表情图片
                int size = (int) tv.getTextSize() * 13 / 10;
                if (smallOrBig.equals("s")) {
                    in = context.getAssets().open("ems/small/" + String.valueOf(emotionNum) + ".png");
                    size = (int) tv.getTextSize() * 13 / 10;
                } else {
                    in = context.getAssets().open("ems/big/" + String.valueOf(emotionNum) + ".png");
                    size = (int) tv.getTextSize() * 25 / 10;
                }
                //bitmap
              /*  Bitmap bitmap = BitmapFactory.decodeStream(in);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);*/
                //drawable
                Drawable drawable = Drawable.createFromStream(in, null);
                drawable.setBounds(0, 0, size, size);
                ImageSpan span = new ImageSpan(drawable);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return spannableString;
    }
    public static SpannableStringBuilder getAdpaterText(String name,String message,String time,TextView tv,Context context){
        SpannableStringBuilder multiWord = new SpannableStringBuilder();
//        设置名字
        SpannableStringBuilder spannableString=new SpannableStringBuilder(name);
        spannableString.setSpan(new ForegroundColorSpan(android.graphics.Color.parseColor("#ff717c")),0,name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        multiWord.append(spannableString);
//设置内容表情
        SpannableStringBuilder multiWord1 = new SpannableStringBuilder();
        if(message.trim()!=null||message.length()!=0){
            String[] split = message.split(" ");
            for (int j = 0; j <split.length ; j++) {
                SpannableStringBuilder style=new SpannableStringBuilder(split[j]);
                if(split[j].trim().length()!=0){
                    String substring = split[j].substring(0, 1);
                    if(substring.equals("@")||substring.equals("＠")){
                        style.setSpan(new ForegroundColorSpan(android.graphics.Color.parseColor("#ff717c")),0,split[j].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        multiWord1.append(style);
                    }else {
                        style.setSpan(new ForegroundColorSpan(Color.BLACK),0,split[j].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        multiWord1.append(style);
                    }
                }
            }
        }
        spannableString=new SpannableStringBuilder(multiWord1);
        int emotionNum = 0;
        String smallOrBig = "";
        InputStream in = null;
        String regexEmotion = "\\[[s]:\\d+\\]|\\[[b]:\\d+\\]";//正则表达式规则 --> [d:数字]
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            emotionNum = Integer.valueOf(key.substring(3, key.indexOf("]")));//获取到表情的编号
            smallOrBig = key.substring(1, 2);
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            try {
                // 压缩表情图片
                int size = (int) tv.getTextSize() * 13 / 10;
                if (smallOrBig.equals("s")) {
                    in = context.getAssets().open("ems/small/" + String.valueOf(emotionNum) + ".png");
                    size = (int) tv.getTextSize() * 13 / 10;
                } else {
                    in = context.getAssets().open("ems/big/" + String.valueOf(emotionNum) + ".png");
                    size = (int) tv.getTextSize() * 25 / 10;
                }

                //bitmap
              /*  Bitmap bitmap = BitmapFactory.decodeStream(in);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);*/

                //drawable
                Drawable drawable = Drawable.createFromStream(in, null);
                drawable.setBounds(0, 0, size, size);
                ImageSpan span = new ImageSpan(drawable);

                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        multiWord.append(spannableString);
        spannableString=new SpannableStringBuilder(time);
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(0.6f);
        spannableString.setSpan(sizeSpan01,0,time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(android.graphics.Color.parseColor("#9e9e9e")),0,time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        multiWord.append(spannableString);
        return multiWord;
    }


    /*
   * emotionNum 表情编号
   * */
    public static SpannableString getEmotionContentEdi(final Context context, final EditText tv, String source) {
        int emotionNum = 0;
        String smallOrBig = "";
        InputStream in = null;
        SpannableString spannableString = new SpannableString(source);
        String regexEmotion = "\\[[s]:\\d+\\]|\\[[b]:\\d+\\]";//正则表达式规则 --> [d:数字]
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            emotionNum = Integer.valueOf(key.substring(3, key.indexOf("]")));//获取到表情的编号
            smallOrBig = key.substring(1, 2);
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            try {
                // 压缩表情图片
                int size = (int) tv.getTextSize() * 13 / 10;
                if (smallOrBig.equals("s")) {
                    in = context.getAssets().open("ems/small/" + String.valueOf(emotionNum) + ".png");
                    size = (int) tv.getTextSize() * 13 / 10;
                } else {
                    in = context.getAssets().open("ems/big/" + String.valueOf(emotionNum) + ".png");
                    size = (int) tv.getTextSize() * 25 / 10;
                }

                //bitmap
              /*  Bitmap bitmap = BitmapFactory.decodeStream(in);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);*/

                //drawable
                Drawable drawable = Drawable.createFromStream(in, null);
                drawable.setBounds(0, 0, size, size);
                ImageSpan span = new ImageSpan(drawable);

                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return spannableString;
    }
}
