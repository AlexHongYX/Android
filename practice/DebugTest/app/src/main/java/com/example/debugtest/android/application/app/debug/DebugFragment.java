package com.example.debugtest.android.application.app.debug;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.i18n.business.debug.R;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.ss.android.application.social.account.client.google.GoogleAccountClient;
import com.ss.android.framework.page.ArticleAbsFragment;
import com.ss.android.uilib.recyclerview.DividerItemDecoration;
import com.ss.android.utils.kit.Logger;

/**
 * Created by yihuaqi on 10/26/16.
 */

public class DebugFragment extends ArticleAbsFragment {

    private View mRootView;
    private RecyclerView mRecyclerView;
    private DebugAdapter mAdapter;
    private GoogleAccountClient mGoogleAccountClient;
    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.debug_fragment, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new DebugAdapter(getContext());
        mGoogleAccountClient = new GoogleAccountClient(getContext());
        try {
            mGoogleAccountClient.initWithAutoManage(getActivity(), null, null);
            mGoogleAccountClient.setFragment(this);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        mAdapter.setGoogleAccountClient(mGoogleAccountClient);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        return mRootView;
    }

    public void scrollBottom(boolean bottom){
        if(bottom){
            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        }else {
            mRecyclerView.scrollToPosition(0);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            mGoogleAccountClient.onActivityResult(requestCode, resultCode, data, new GoogleAccountClient.Callback() {
                @Override
                public void onFinish(GoogleSignInResult result) {
                    if (result.isSuccess()) {
                        Logger.d(GoogleAccountClient.TAG, result.getSignInAccount().getEmail() + " login in google");
                    } else {
                        Logger.w(GoogleAccountClient.TAG, "Login Failed: "+result.getStatus().toString());
                    }
                }

                @Override
                public void onCanceled() {

                }
            });
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mGoogleAccountClient.stopAutoManage(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
