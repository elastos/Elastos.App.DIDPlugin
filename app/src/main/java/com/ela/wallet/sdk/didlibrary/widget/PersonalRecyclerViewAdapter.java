package com.ela.wallet.sdk.didlibrary.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.bean.SettingModel;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;

import java.util.List;
import java.util.Locale;

public class PersonalRecyclerViewAdapter extends RecyclerView.Adapter<PersonalRecyclerViewAdapter.SettingViewHolder> {

    private Context mContext;
    private List<SettingModel> mList;
    private OnItemClickListener mOnItemClickListener;

    public PersonalRecyclerViewAdapter(Context context, List<SettingModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public PersonalRecyclerViewAdapter.SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_personal, parent, false);
        return new SettingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PersonalRecyclerViewAdapter.SettingViewHolder holder, final int position) {
        holder.tv_title.setText(mList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class SettingViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_subtitle;
        private ImageView iv_next;

        public SettingViewHolder(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_personal_img);
            tv_title = itemView.findViewById(R.id.tv_personal_title);
            tv_subtitle = itemView.findViewById(R.id.tv_personal_subtitle);
            iv_next = itemView.findViewById(R.id.iv_personal_next);
        }
    }

    public interface OnItemClickListener {

        void onClick(int position);
    }

    public void setOnItemClickListener(PersonalRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
