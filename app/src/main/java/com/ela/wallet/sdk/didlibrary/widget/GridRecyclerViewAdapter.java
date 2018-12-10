package com.ela.wallet.sdk.didlibrary.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.bean.WordModel;

import java.util.List;

public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.WordViewHolder> {

    public static int TYPE_SHOW = 1;
    public static int TYPE_INPUT = 2;

    private Context mContext;
    private List<WordModel> mList;
    private int mType;
    private OnItemClickListener mOnItemClickListener;

    public GridRecyclerViewAdapter(Context context, List<WordModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setType(int type) {
        mType = type;
    }

    @Override
    public GridRecyclerViewAdapter.WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GridRecyclerViewAdapter.WordViewHolder holder, final int position) {
        holder.textView.setText(mList.get(position).getWord());
        if (mType == TYPE_INPUT) {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.appColor));
            holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_roundcorner_trans));
        } else {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.textBlack));
            holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_roundcorner_gray));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null && !mList.get(position).isClicked()) {
                    mOnItemClickListener.onClick(position, holder.textView);
                    mList.get(position).setClicked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public WordViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_word_show);
        }
    }

    public interface OnItemClickListener {

        void onClick(int position, TextView textView);
    }

    public void setOnItemClickListener(GridRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
