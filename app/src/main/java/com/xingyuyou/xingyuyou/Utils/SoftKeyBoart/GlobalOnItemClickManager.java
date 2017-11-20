package com.xingyuyou.xingyuyou.Utils.SoftKeyBoart;

/**
 * Created by xiaokai on 2017/02/07.
 */

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

public class GlobalOnItemClickManager {
	int i;
	private static GlobalOnItemClickManager instance;
	private EditText mEditText;

	public static GlobalOnItemClickManager getInstance() {
		if (instance == null) {
			instance = new GlobalOnItemClickManager();
		}
		return instance;
	}

	public void attachToEditText(EditText editText) {
		mEditText = editText;
	}

	public AdapterView.OnItemClickListener getOnItemClickListener(final int emsPos, final Context context) {
		return new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/* 判断是不是“删除”按钮
				 *      是   -> 删除上一个表情
				 *      不是 -> 加载相应的表情*/
				int emotionNum = position + emsPos*20;//表情编号
				if(position != 20){
					int index = mEditText.getSelectionStart();//当前光标位置
					Log.d("qqqqq1",index+"");
					String emotionName = "[s:" + String.valueOf(emotionNum) + "]";
					String currentContent = mEditText.getText().toString();
//					if(currentContent.equals("@")||currentContent.equals("＠")){
//						index-=2;
//					}
					StringBuilder sb = new StringBuilder(currentContent);
					sb.insert(index, emotionName);
					mEditText.setText(SpanStringUtils.getEmotionContent(context, mEditText, sb.toString()));
					int dex=index + emotionName.length();
					try {
						mEditText.setSelection(dex);
					}catch (Exception e){

					}
				} else {
					mEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
				}
			}
		};
	}
}
