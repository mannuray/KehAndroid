package com.dubmania.dubsmania.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.BusProvider;
import com.dubmania.dubsmania.misc.ImportVideoActivity;
import com.dubmania.dubsmania.misc.RecordVideoActivity;


public class AddVideoFragment extends Fragment {

    public AddVideoFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_add_video, container, false);
        ImageButton mRecord = (ImageButton) view.findViewById(R.id.add_video_record_button);
        ImageButton mImporct = (ImageButton) view.findViewById(R.id.add_video_import_button);
        mRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordVideoActivity.class);
                startActivity(intent);
            }
        });

        mImporct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImportVideoActivity.class);
                startActivity(intent);
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
}
