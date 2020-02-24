package com.example.debugtest.android.application.app.debug.ad;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.i18n.business.debug.R;
import com.ss.android.framework.page.ArticleAbsFragment;
import com.ss.android.uilib.recyclerview.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ChengKai on 16/11/18.
 */

public class AdDebugPreloadFragment extends ArticleAbsFragment {

    private RecyclerView mRecyclerView;
    private AdDebugPreloadAdapter mAdapter;

    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.debug_fragment, container, false);
        List<AdPreloadItem> items = buildAdPlatformItems();
        mAdapter = new AdDebugPreloadAdapter(getContext(), items);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        return view;
    }

    private List<AdPreloadItem> buildAdPlatformItems() {
        List<AdPreloadItem> items = new ArrayList<>();
//        for (String placement : AdPlacement.ALL_PLACEMENTS_TYPE) {
//            addPlacement(items, placement);
//        }
        return items;
    }

//    private void addPlacement(List<AdPreloadItem> items, String placement) {
//        AdPreloadItem item = new AdPreloadItem();
//        item.placementType = placement;
//        Collection<IAdProvider> preloaders = AdCenter.getInstance(getContext()).getPreloaderMap().values();
//        for (IAdProvider preloader : preloaders) {
//            addPlatformIfHasAd(preloader, placement, item);
//        }
//        items.add(item);
//    }
//
//    private void addPlatformIfHasAd(IAdProvider provider, String placement, AdPreloadItem item) {
//        if (provider.hasUnClickedAd(placement)) {
//            item.availablePlatforms.add(provider.getPlatformTag());
//        }
//    }

    public void refreshData() {
        List<AdPreloadItem> items = buildAdPlatformItems();
        mAdapter.updateData(items);
    }
}
