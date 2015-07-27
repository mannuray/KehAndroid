package com.dubmania.dubsmania;

import android.app.Activity;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagerFragment extends Fragment
           /* implements TrendingFragment.OnFragmentInteractionListener,
                        DiscoverFragment.OnFragmentInteractionListener,
                        VideoBoardFragment.OnFragmentInteractionListener*/ {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBE

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    PagerAdapter mPagerAdapter;
    ViewPager mViewPager;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagerFragment newInstance() {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onDiscoverFragmentInteraction(String id) {

    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onPagerFragmentInteraction(Uri uri);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        TypedArray title = getResources()
                .obtainTypedArray(R.array.pager_titles);
        String titles[] = {title.getString(0), title.getString(1), title.getString(2)};
        //title.recycle();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {

            switch ( i ) {
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


