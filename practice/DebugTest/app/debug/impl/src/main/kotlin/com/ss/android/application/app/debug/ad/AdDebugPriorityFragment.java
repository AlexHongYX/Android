package com.ss.android.application.app.debug.ad;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.i18n.business.debug.R;
import com.ss.android.application.app.recyclerview.drag.OnStartDragListener;
import com.ss.android.framework.page.ArticleAbsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChengKai on 16/11/18.
 */

public class AdDebugPriorityFragment extends ArticleAbsFragment implements OnStartDragListener{

    private RecyclerView mRecyclerView;
    private AdDebugPriorityAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.debug_fragment, container, false);

//        List<Integer> priority = FillStrategyManager.getInstance(getContext()).getDebugPriority();
//        List<AdPriorityItem> items = buildAdPlatformItems(priority);
//        mAdapter = new AdDebugPriorityAdapter(getContext(), items, this);
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        return view;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onDestroyView() {
//        FillStrategyManager.getInstance(getContext()).setDebugPriority(mAdapter.getPriority());
        super.onDestroyView();
    }

    private List<AdPriorityItem> buildAdPlatformItems(List<Integer> priority) {
        List<AdPriorityItem> items = new ArrayList<>();
//        for (int providerId : priority) {
//            AdPriorityItem item = new AdPriorityItem();
//            item.adSource = providerId;
//            item.platformTag = String.valueOf(providerId);
//            items.add(item);
//        }
        return items;
    }

    public void handleResetClick() {
//        FillStrategyManager manager = FillStrategyManager.getInstance(getContext());
//        List<Integer> priority = manager.getDefaultDebugPriority();
//        mAdapter.updateData(buildAdPlatformItems(priority));
    }
}
