package com.dubmania.dubsmania.addvideo;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dubmania.dubsmania.Adapters.ImportVideoAdapter;
import com.dubmania.dubsmania.Adapters.ImportVideoListItem;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;

import java.util.ArrayList;

public class SearchVideoFragment extends Fragment {
    private ImportVideoAdapter mAdapter;
    private ArrayList<ImportVideoListItem> mVideoItemList;

    public SearchVideoFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_search_video, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mVideoItemList = new ArrayList<>();
        populateVideo();

        mAdapter = new ImportVideoAdapter(mVideoItemList);
        mRecyclerView.setAdapter(mAdapter);
        EditText search = (EditText) view.findViewById(R.id.searchEdit);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.setFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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

    private void populateVideo() {
        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE };

        Cursor cursor = getActivity().managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                mVideoItemList.add(new ImportVideoListItem(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE))));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
