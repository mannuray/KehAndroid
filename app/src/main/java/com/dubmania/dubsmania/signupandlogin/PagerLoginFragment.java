package com.dubmania.dubsmania.signupandlogin;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.FragmentChangeEvent;
import com.dubmania.dubsmania.communicator.eventbus.LoginFragmentChangeEvent;
import com.squareup.otto.Subscribe;

public class PagerLoginFragment extends Fragment {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login_pager, container, false);
        mPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        mPager = (ViewPager) view.findViewById(R.id.login_view_pager);
        mPager.setAdapter(mPagerAdapter);
        return view;
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

    private class MyPagerAdapter extends FragmentPagerAdapter {

        TypedArray title = getResources()
                .obtainTypedArray(R.array.pager_signup_titles);
        String titles[] = {title.getString(0), title.getString(1), title.getString(2), title.getString(3)};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {

            switch (i) {
                case 0:
                    return new LoginFragment();
                case 1:
                    return  new ResetPasswordFragment();
            }
            return new LoginFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Subscribe
    public void onLoginFragmentChangeEvent(LoginFragmentChangeEvent event) {
        if (event.getPosition() == -1){
            int position = mPager.getCurrentItem();
            if (position == 0) {
                BusProvider.getInstance().post(new FragmentChangeEvent(0));
                return;
            }
            mPager.setCurrentItem(position - 1);
            return;
        }
        mPager.setCurrentItem(event.getPosition());
    }
}
