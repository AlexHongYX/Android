package com.ss.android.application.app.debug.ad;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytedance.i18n.business.debug.R;
import com.ss.android.application.app.recyclerview.drag.ItemTouchHelperAdapter;
import com.ss.android.application.app.recyclerview.drag.ItemTouchHelperViewHolder;
import com.ss.android.application.app.recyclerview.drag.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ChengKai on 16/11/18.
 */

public class AdDebugPriorityAdapter extends RecyclerView.Adapter<AdDebugPriorityAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private List<AdPriorityItem> mData = new ArrayList<>();
    private OnStartDragListener mOnStartDragListener;

    public AdDebugPriorityAdapter(Context context, List<AdPriorityItem> items, OnStartDragListener listener) {
        super();
        mData.addAll(items);
        mOnStartDragListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_debug_priority_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        AdPriorityItem item = mData.get(position);
        holder.mTextView.setText(item.adSource + " (" + item.platformTag + ")");
        holder.mDragView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public List<Integer> getPriority() {
        List<Integer> priority = new ArrayList<>();
        for (AdPriorityItem item : mData) {
            priority.add(item.adSource);
        }
        return priority;
    }

    public void updateData(List<AdPriorityItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public final TextView mTextView;
        public final ImageView mDragView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
            mDragView = (ImageView) itemView.findViewById(R.id.drag_view);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.GRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
