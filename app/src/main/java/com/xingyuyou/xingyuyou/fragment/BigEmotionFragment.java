package com.xingyuyou.xingyuyou.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.SpanStringUtils;


/**
 * Created by xiaokai on 2017/02/07.
 */
public class BigEmotionFragment extends Fragment {

    private Context mContext;
    private GridView emotionGrid;
    private int startPosition;
    private EditText mEditText;
    public static BigEmotionFragment newInstance(int position) {
        BigEmotionFragment fragment = new BigEmotionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("emotion_start_position", position*20);
        bundle.putInt("fragment_number", position);
        fragment.setArguments(bundle);
        return fragment;
    }
    public void BindEditText(EditText editText){
        mEditText=editText;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 先确定每页第一个表情的position */
        if(getArguments() != null){
            startPosition = getArguments().getInt("emotion_start_position", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emotion_big_gird_classic, container, false);
        emotionGrid = (GridView) view.findViewById(R.id.grid);
        emotionGrid.setAdapter(new MyAdapter());
        emotionGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    int index = mEditText.getSelectionStart();//当前光标位置
                    String emotionName = "[b:" + position + "]";
                    String currentContent = mEditText.getText().toString();
                    StringBuilder sb = new StringBuilder(currentContent);
                    sb.insert(index, emotionName);
                    mEditText.setText(SpanStringUtils.getEmotionContent(getActivity(), mEditText, sb.toString()));
                    mEditText.setSelection(index + emotionName.length());
            }
        });
        
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.emotion_big_gird_item_classic, parent, false);
                holder = new ViewHolder();
                holder.img = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String path = "file:///android_asset/ems/big/" + (position + startPosition) + ".png";
            Glide.with(mContext).load(path).into(holder.img);
            return convertView;
        }

        class ViewHolder{
            public ImageView img;
        }
    }
}
