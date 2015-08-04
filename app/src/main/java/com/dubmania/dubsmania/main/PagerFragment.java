package com.dubmania.dubsmania.main;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.BusProvider;

public class PagerFragment extends Fragment {
    PagerAdapter mPagerAdapter;
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(
                R.layout.fragment_pager, container, false);
        mPagerAdapter = new MyPagerAdapter(
                getChildFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_pager_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        TrendingFragment mTrendingFragment = null;
        DiscoverFragment mDiscoverFragment = null;
        VideoBoardFragment mVideoBoardFragment = null;

        TypedArray title = getResources()
                .obtainTypedArray(R.array.pager_titles);
        String titles[] = {title.getString(0), title.getString(1), title.getString(2)};
        //title.recycle();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {

            /*switch (i) {
                case 0:
                    return new TrendingFragment();
                case 1:
                    if(mDiscoverFragment != null){
                        return mDiscoverFragment;
                    }
                    else {
                        mDiscoverFragment = new DiscoverFragment();
                        return mTrendingFragment;
                    }
                case 2:
                    if(mVideoBoardFragment != null){
                        return mVideoBoardFragment;
                    }
                    else {
                        mVideoBoardFragment = new VideoBoardFragment();
                        return mVideoBoardFragment;
                    }
            }*/
            switch (i) {
                case 0:
                    return new TrendingFragment();
                case 1:
                    return new DiscoverFragment();
                case 2:
                    return new VideoBoardFragment();
            }
            return new TrendingFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}


