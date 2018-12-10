package com.ela.wallet.sdk.didlibrary.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.bean.WordModel;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;

import java.util.List;
import java.util.Locale;

public class LanguageRecyclerViewAdapter extends RecyclerView.Adapter<LanguageRecyclerViewAdapter.LanguageViewHolder> {

    private Context mContext;
    private List<WordModel> mList;
    private OnItemClickListener mOnItemClickListener;

    public LanguageRecyclerViewAdapter(Context context, List<WordModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public LanguageRecyclerViewAdapter.LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_language, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
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
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public LanguageViewHolder(View itemView) {
            super(itemView);
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
