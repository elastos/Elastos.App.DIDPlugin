package com.ela.wallet.sdk.didlibrary.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.fragment.BlankFragment;
import com.ela.wallet.sdk.didlibrary.fragment.ChargeFragment;
import com.ela.wallet.sdk.didlibrary.fragment.HomeFragment;
import com.ela.wallet.sdk.didlibrary.fragment.PayFragment;
import com.ela.wallet.sdk.didlibrary.fragment.PersonalFragment;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.CustomViewPager;
import com.ela.wallet.sdk.didlibrary.widget.DidAlertDialog;
import com.ela.wallet.sdk.didlibrary.widget.TitleFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DidLaunchActivity extends BaseActivity implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener{

    private CustomViewPager mViewPager;
    private TabLayout mTab;

    private int[] tabIcons = {
        R.drawable.drawable_nav_home,
        R.drawable.drawable_nav_charges,
        R.drawable.drawable_nav_pay,
        R.drawable.drawable_nav_mine
    };
    private int[] tabTexts = {
        R.string.nav_home,
        R.string.nav_charges,
        R.string.nav_pay,
        R.string.nav_mine
    };

    @Override
    protected int getRootViewId() {
        return R.layout.activity_did_launch;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_launch);
        mTab = findViewById(R.id.tab_nav_bottom);
    }

    @Override
    protected void initData() {
        String language = Utilty.getPreference(Constants.SP_KEY_APP_LANGUAGE, "");
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        if ("chinese".equals(language)) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if ("english".equals(language)){
            config.locale = Locale.ENGLISH;
        } else {
            config.locale = Locale.getDefault();
        }
        resources.updateConfiguration(config, dm);
        setTitleText(getResources().getString(R.string.library_name));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new ChargeFragment());
        fragments.add(new PayFragment());
        fragments.add(new PersonalFragment());
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{getString(R.string.nav_home), getString(R.string.nav_charges), getString(R.string.nav_pay), getString(R.string.nav_mine)});
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);

        mTab.setupWithViewPager(mViewPager);
        mTab.addOnTabSelectedListener(this);
        for (int i = 0; i < mTab.getTabCount(); i++) {
            TabLayout.Tab tab = mTab.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        if (positionOffset > 0) {
//            mTab.getTabAt(position).getCustomView().findViewById(R.id.iv_tabItem_img).setAlpha(1-positionOffset/2f);
//            mTab.getTabAt(position).getCustomView().findViewById(R.id.tv_tabItem_text).setAlpha(1-positionOffset/2f);
//            mTab.getTabAt(position + 1).getCustomView().findViewById(R.id.iv_tabItem_img).setAlpha(positionOffset/2f+0.5f);
//            mTab.getTabAt(position + 1).getCustomView().findViewById(R.id.tv_tabItem_text).setAlpha(positionOffset/2f+0.5f);
//        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            showBackupDialog();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
//        for (int i = 0; i < mTab.getTabCount(); i++) {
//            mTab.getTabAt(i).getCustomView().findViewById(R.id.iv_tabItem_img).setAlpha(i == tab.getPosition() ? 1 : 1);
//            mTab.getTabAt(i).getCustomView().findViewById(R.id.tv_tabItem_text).setAlpha(i == tab.getPosition() ? 1 : 1);
//        }
        mViewPager.setCurrentItem(tab.getPosition(), false);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void onLeftNavClick(View view) {
        finish();
    }

    /**
     * get view for tab item from layout file
     * @param position:the position in the bottom navigation
     * @return LinearLayout
     */
    private View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.nav_tab_item, null);
        ImageView iv_img = view.findViewById(R.id.iv_tabItem_img);
        TextView tv_text = view.findViewById(R.id.tv_tabItem_text);
        iv_img.setImageResource(tabIcons[position]);
        tv_text.setText(tabTexts[position]);
        return view;
    }

    private void showBackupDialog() {
        new DidAlertDialog(this)
                .setTitle(getString(R.string.send_backup))
                .setMessage(getString(R.string.send_tips))
                .setMessageGravity(Gravity.LEFT)
                .setRightButton(getString(R.string.btn_backup), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(DidLaunchActivity.this, BackupTipsActivity.class);
                        DidLaunchActivity.this.startActivity(intent);
                    }
                })
                .show();
    }

}
