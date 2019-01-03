package com.ela.wallet.sdk.didlibrary.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.bean.RecordsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordsRecyclerViewAdapter extends RecyclerView.Adapter<RecordsRecyclerViewAdapter.RecordsViewHolder> {

    private Context mContext;
    private List<RecordsModel> mList;
    private OnItemClickListener mOnItemClickListener;

    public RecordsRecyclerViewAdapter(Context context, List<RecordsModel> list) {
        this.mContext = context;
        this.mList = new ArrayList<>();
    }

    public void setData(List<RecordsModel> list) {
        if (mList != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void setData(List<RecordsModel> list, boolean b) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecordsRecyclerViewAdapter.RecordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_records, parent, false);
        return new RecordsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecordsRecyclerViewAdapter.RecordsViewHolder holder, final int position) {
        holder.tv_title.setText(mList.get(position).getType());
        if (TextUtils.isEmpty(mList.get(position).getTime())) {
            holder.tv_subtitle.setVisibility(View.GONE);
            holder.tv_next.setText(mList.get(position).getValue());
        } else {
            holder.tv_subtitle.setVisibility(View.VISIBLE);
            holder.tv_subtitle.setText(stampToDate(mList.get(position).getTime()));

            String value = mList.get(position).getValue();
            String preFix = value.substring(0,1);
            String realValue = value.substring(1);

            String text = String.format("%s%.8f%s", preFix, Long.parseLong(realValue)/100000000.0f , " ELA");
            holder.tv_next.setText(text);
        }
        if (position + 1 == getItemCount()) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
    }

    /*
    * 将时间戳转换为时间
    */
    private String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:MM");
        long lt = new Long(s)*1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class RecordsViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_subtitle;
        private TextView tv_next;
        private View line;

        public RecordsViewHolder(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_records_img);
            tv_title = itemView.findViewById(R.id.tv_records_title);
            tv_subtitle = itemView.findViewById(R.id.tv_records_subtitle);
            tv_next = itemView.findViewById(R.id.tv_records_next);
            line = itemView.findViewById(R.id.line);
        }
    }

    public interface OnItemClickListener {

        void onClick(int position);
    }

    public void setOnItemClickListener(RecordsRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
