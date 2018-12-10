package com.ela.wallet.sdk.didlibrary.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private TextView mTextView;

    public GridViewAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
//        convertView = layoutInflater.inflate(R.layout.grid_item, null);
//        mTextView = (ImageView) convertView.findViewById(R.id.tv);
//        mTextView.setText(mList.get(i));
//        return convertView;
        return null;
    }
}
