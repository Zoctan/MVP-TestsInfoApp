package com.zoctan.solar.test.widget;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoctan.solar.R;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {

    public static final int TEST_TYPE_PHONE = 0;
    public static final int TEST_TYPE_LAPTOP = 1;
    public static final int TEST_TYPE_EARPHONE = 2;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyPagerAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_test, null);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        // 设置预加载3个page
        mViewPager.setOffscreenPageLimit(3);

        // 设置Tab
        setupViewPager(mViewPager);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.phone));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.laptop));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.earphone));

        // 绑定ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void setupViewPager(ViewPager mViewPager) {
        // Fragment中嵌套使用Fragment一定要使用getChildFragmentManager()
        myAdapter = new MyPagerAdapter(getChildFragmentManager());
        myAdapter.addFragment(TestListFragment.newInstance(TEST_TYPE_PHONE), getString(R.string.phone));
        myAdapter.addFragment(TestListFragment.newInstance(TEST_TYPE_LAPTOP), getString(R.string.laptop));
        myAdapter.addFragment(TestListFragment.newInstance(TEST_TYPE_EARPHONE), getString(R.string.earphone));
        mViewPager.setAdapter(myAdapter);
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
