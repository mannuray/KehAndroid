package com.dubmania.vidcraft.addvideo;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoChangeFragmentEvent;
import com.squareup.otto.Subscribe;

public class RecordVideoPagerFragment extends Fragment {
    private ViewPager mPager;

    public RecordVideoPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_video_pager, container, false);
        PagerAdapter mPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        mPager = (ViewPager) view.findViewById(R.id.viewPager);
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
                    return new RecordVideoFragment();
                case 1:
                    return new EditVideoFragment();
                case 2:
                    return new AddTagFragment();
                case 3:
                    return new AddFinishFragment();
            }
            return new RecordVideoFragment();
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Subscribe
    public void onAddVideoChangeFragmentEvent(AddVideoChangeFragmentEvent event) {
        Log.i("Change", " fragment change event");
        if (event.getPosition() == -1){
            int position = mPager.getCurrentItem();
            if (position == 0) {
                getActivity().finish();
            }
            mPager.setCurrentItem(position - 1);
            return;
        }
        mPager.setCurrentItem(event.getPosition());
    }
}
