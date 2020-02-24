package com.example.debugtest.android.application.app.debug.ad;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bytedance.ad.symphony.AdSymphony;
import com.bytedance.ad.symphony.strategy.FillStrategy;

import com.bytedance.ad.symphony.strategy.FillStrategyManager;
import com.bytedance.i18n.business.debug.R;
import com.bytedance.i18n.claymore.ClaymoreServiceLoader;
import com.ss.android.application.app.recyclerview.drag.OnStartDragListener;
import com.ss.android.application.service.IItemTouchHelperCallbackFactory;
import com.ss.android.framework.page.ArticleAbsFragment;
import com.ss.android.uilib.recyclerview.DividerItemDecoration;
import com.ss.android.utils.GsonProvider;
import com.ss.android.utils.kit.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ChengKai on 16/11/18.
 */

public class NewAdDebugPriorityFragment extends ArticleAbsFragment implements OnStartDragListener {

    public static final String TAG = "NewAdDebugPriorityFragment";
    public static final String AD_TYPE_NATIVE = "native";
    public static final String AD_TYPE_INTERSTITIAL = "interstitial";
    public static final String AD_TYPE_BANNER = "banner";

    private RecyclerView mRecyclerView;
    private AdDebugPriorityAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private String mAdType;
    private FillStrategyManager mFillStrategyManager;

    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.debug_fragment, container, false);
        setData();
        List<Integer> priority = mFillStrategyManager.getDebugDisplaySort();
        List<AdPriorityItem> items = buildAdPlatformItems(priority);
        mAdapter = new AdDebugPriorityAdapter(getContext(), items, this);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        ItemTouchHelper.Callback callback = ClaymoreServiceLoader.
                loadFirstOrNull(IItemTouchHelperCallbackFactory.class).createItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        return view;
    }

    private void setData() {
        if (AD_TYPE_INTERSTITIAL.equals(mAdType)) {
            mFillStrategyManager = AdSymphony.getInstance().getInterstitialAdManager().getFillStrategyManager();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onDestroyView() {
        setDebugPriority(mAdapter.getPriority());
        super.onDestroyView();
    }

    private List<AdPriorityItem> buildAdPlatformItems(List<Integer> priority) {
        List<AdPriorityItem> items = new ArrayList<>();
        for (int providerId : priority) {
            AdPriorityItem item = new AdPriorityItem();
            item.adSource = providerId;
            item.platformTag = String.valueOf(providerId);
            items.add(item);
        }
        return items;
    }

    public void setAdType(String adType) {
        mAdType = adType;
    }

    private void setDebugPriority(List<Integer> priority) {
        if (priority != null) {
            List<List<Integer>> preloadSort = new ArrayList<>();
            for (Integer p : priority) {
                List list = new ArrayList();
                list.add(p);
                preloadSort.add(list);
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("display_sort", priority);
            map.put("preload_sort", preloadSort);
            String string = GsonProvider.getDefaultGson().toJson(map);
            Logger.d(TAG, "debug strategy string:" + string);
            JSONObject json = null;
            try {
                json = new JSONObject(string);
            } catch (Exception e) {

            }
            mFillStrategyManager.setDebugFillStrategy(FillStrategy.parseFillStrategy(json));
        }
    }
}
