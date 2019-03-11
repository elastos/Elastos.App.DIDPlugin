package com.ela.wallet.sdk.didlibrary.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.MainActivity;
import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.bean.WordModel;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;

import java.util.List;
import java.util.Locale;

public class LanguageRecyclerViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<WordModel> mList;
    private OnItemClickListener mOnItemClickListener;

    public LanguageRecyclerViewAdapter(Context context, List<WordModel> list) {
        this.mContext = context;
        this.mList = list;
    }

//    @Override
    public LanguageRecyclerViewAdapter.LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_language, parent, false);
        return new LanguageViewHolder(view);
    }

//    @Override
    public void onBindViewHolder(final LanguageRecyclerViewAdapter.LanguageViewHolder holder, final int position) {
        holder.textView.setText(mList.get(position).getWord());
        holder.textView.setTextColor(mContext.getResources().getColor(mList.get(position).isClicked() ? R.color.appColor : R.color.textBlack));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mList.get(position).setClicked(true);
                    mOnItemClickListener.onClick(position, holder.textView);
                    String language = mList.get(position).getWord().toLowerCase().contains("english") ? "english" : "chinese";
                    Utilty.setPreference(Constants.SP_KEY_APP_LANGUAGE, language);
                    Resources resources = mContext.getResources();
                    DisplayMetrics dm = resources.getDisplayMetrics();
                    Configuration config = resources.getConfiguration();
                    // 应用用户选择语言
                    if ("chinese".equals(language)) {
                        config.locale = Locale.SIMPLIFIED_CHINESE;
                    } else {
                        config.locale = Locale.ENGLISH;
                    }
                    resources.updateConfiguration(config, dm);

                    final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                    System.exit(0);
                }
            }
        });
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
        LanguageViewHolder holder;
        //判断是否有缓存
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_language, parent, false);
            holder =  new LanguageViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            //得到缓存的布局
            holder = (LanguageViewHolder) convertView.getTag();
        }
        final int fposttion = position;
        final LanguageViewHolder fholder = holder;
        holder.textView.setText(mList.get(position).getWord());
        holder.textView.setTextColor(mContext.getResources().
                getColor(mList.get(position).isClicked() ? R.color.appColor : R.color.textBlack));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mList.get(fposttion).setClicked(true);
                    mOnItemClickListener.onClick(fposttion, fholder.textView);
                    String language = mList.get(fposttion).getWord().
                            toLowerCase().contains("english") ? "english" : "chinese";
                    Utilty.setPreference(Constants.SP_KEY_APP_LANGUAGE, language);
                    Resources resources = mContext.getResources();
                    DisplayMetrics dm = resources.getDisplayMetrics();
                    Configuration config = resources.getConfiguration();
                    // 应用用户选择语言
                    if ("chinese".equals(language)) {
                        config.locale = Locale.SIMPLIFIED_CHINESE;
                    } else {
                        config.locale = Locale.ENGLISH;
                    }
                    resources.updateConfiguration(config, dm);

                    final Intent intent = mContext.getPackageManager().
                            getLaunchIntentForPackage(mContext.getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                    System.exit(0);
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

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }


    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class LanguageViewHolder {

        private View itemView;
        private TextView textView;

        public LanguageViewHolder(View itemView) {
            this.itemView = itemView;
            textView = itemView.findViewById(R.id.tv_item_language);
        }
    }

    public interface OnItemClickListener {

        void onClick(int position, TextView textView);
    }

    public void setOnItemClickListener(LanguageRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
