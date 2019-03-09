package com.ela.wallet.sdk.didlibrary.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.bean.SettingModel;

import java.util.List;

public class PersonalRecyclerViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<SettingModel> mList;
    private OnItemClickListener mOnItemClickListener;

    public PersonalRecyclerViewAdapter(Context context, List<SettingModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    //@Override
    public PersonalRecyclerViewAdapter.SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_personal, parent, false);
        return new SettingViewHolder(view);
    }

    //@Override
    public void onBindViewHolder(final PersonalRecyclerViewAdapter.SettingViewHolder holder, final int position) {
        holder.iv_img.setImageResource(mList.get(position).getImg());
        holder.tv_title.setText(mList.get(position).getTitle());
        if (position + 1 == getItemCount()) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
		//TODO houhong
		
        holder.iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            }
        });
    }

//    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingViewHolder holder;
        //判断是否有缓存
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_personal, parent, false);
            holder =  new SettingViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            //得到缓存的布局
            holder = (SettingViewHolder) convertView.getTag();
        }


        holder.iv_img.setImageResource(mList.get(position).getImg());
        holder.tv_title.setText(mList.get(position).getTitle());
        if (position + 1 == getItemCount()) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        final int fposition = position;
        holder.iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(fposition);
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled(){
        return true;
    }

    @Override
    public boolean isEnabled(int var1){
        return true;
    }

    class SettingViewHolder {

        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_subtitle;
        private ImageView iv_next;
        private View line;

        public SettingViewHolder(View itemView) {
//            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_personal_img);
            tv_title = itemView.findViewById(R.id.tv_personal_title);
            tv_subtitle = itemView.findViewById(R.id.tv_personal_subtitle);
            iv_next = itemView.findViewById(R.id.iv_personal_next);
            line = itemView.findViewById(R.id.iv_personal_line);
        }
    }

    public interface OnItemClickListener {

        void onClick(int position);
    }

    public void setOnItemClickListener(PersonalRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
