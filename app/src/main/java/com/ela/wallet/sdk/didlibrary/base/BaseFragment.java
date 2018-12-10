package com.ela.wallet.sdk.didlibrary.base;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ela.wallet.sdk.didlibrary.utils.LogUtil;

public abstract class BaseFragment extends Fragment {

    private String fragmentName = this.getClass().getSimpleName();
    protected FragmentActivity mActivity;
    private FrameLayout rootFragmentView;
    private View normalFragmentView;
    private boolean mFirstOpen;
    private boolean isFirstUserHint = true;
    private View netErrorView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        LogUtil.i(fragmentName + ":onAttach");
        super.onAttach(context);
        mActivity = getActivity();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        mFirstOpen = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(fragmentName + ":onCreateView");
        /** 创建根fragment view	 */
        rootFragmentView = new FrameLayout(mActivity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rootFragmentView.setLayoutParams(params);
        rootFragmentView.removeAllViews();
        normalFragmentView = inflater.inflate(getRootViewId(), container, false);
        rootFragmentView.addView(normalFragmentView);
        initView(rootFragmentView, savedInstanceState);
        return rootFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtil.i(fragmentName + ":onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.i(fragmentName + ":setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirstUserHint) {
            isFirstUserHint = !isFirstUserHint;
            return;
        }
        if (isVisibleToUser) {
            doFragmentResume();
        } else {
            doFragmentPause();
        }
    }

    @Override
    public void onPause() {
        LogUtil.i(fragmentName + ":onPause");
        super.onPause();
        if (getUserVisibleHint()) {
            doFragmentPause();
        }
    }

    @Override
    public void onResume() {
        LogUtil.i(fragmentName + ":onResume");
        super.onResume();
        if (mFirstOpen) {
            mFirstOpen = false;
            return;
        }
        if (getUserVisibleHint()) {
            doFragmentResume();
        }
    }

    private void doFragmentPause() {
        LogUtil.i(fragmentName + ":doFragmentPause");
        onFragmentPause();
    }

    private void doFragmentResume() {
        LogUtil.i(fragmentName + ":doFragmentResume");
        onFragmentResume();
    }

    protected abstract void initView(View rootView, @Nullable Bundle savedInstanceState);

    protected abstract void initData(@Nullable Bundle savedInstanceState);

    protected abstract int getRootViewId();

    protected abstract void onFragmentResume();

    protected abstract void onFragmentPause();

}
