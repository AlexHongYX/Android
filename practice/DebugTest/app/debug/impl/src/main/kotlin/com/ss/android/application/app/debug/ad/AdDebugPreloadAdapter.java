package com.ss.android.application.app.debug.ad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytedance.i18n.business.debug.R;
import com.ss.android.application.app.recyclerview.drag.OnStartDragListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ChengKai on 16/11/18.
 */

public class AdDebugPreloadAdapter extends RecyclerView.Adapter<AdDebugPreloadAdapter.ViewHolder> {
    private List<AdPreloadItem> mData = new ArrayList<>();
    private OnStartDragListener mOnStartDragListener;

    public AdDebugPreloadAdapter(Context context, List<AdPreloadItem> items) {
        super();
        mData.addAll(items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_debug_preload_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        AdPreloadItem item = mData.get(position);
        holder.mTextPlacement.setText(item.placementType);
        if (item.availablePlatforms.size() == 0) {
            holder.mTextResult.setText("no available ad");
            holder.mMarkView.setImageResource(R.drawable.mark_ad_red);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("available ad platform(s):");
            for (String s : item.availablePlatforms) {
                sb.append("  ").append(s);
            }
            holder.mTextResult.setText(sb.toString());
            holder.mMarkView.setImageResource(R.drawable.mark_ad_green);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(List<AdPreloadItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextPlacement;
        public final TextView mTextResult;
        public final ImageView mMarkView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextPlacement = (TextView) itemView.findViewById(R.id.text_placement);
            mTextResult = (TextView) itemView.findViewById(R.id.text_result);
            mMarkView = (ImageView) itemView.findViewById(R.id.mark);
        }
    }
}
