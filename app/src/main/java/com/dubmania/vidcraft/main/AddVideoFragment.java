package com.dubmania.vidcraft.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.addvideo.AddVideoActivity;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.utils.ConstantsStore;


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
        Button mRecord = (Button) view.findViewById(R.id.add_video_record_button);
        Button mImporct = (Button) view.findViewById(R.id.add_video_import_button);
        mRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddVideoActivity(ConstantsStore.INTENT_ADD_VIDEO_RECORD);
            }
        });

        mImporct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddVideoActivity(ConstantsStore.INTENT_ADD_VIDEO_IMPORT);
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

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Video");
    }

    private void startAddVideoActivity(int pager) {
        Intent intent = new Intent(getActivity(), AddVideoActivity.class);
        intent.putExtra(ConstantsStore.INTENT_ADD_VIDEO_ACTION, pager);
        startActivity(intent);
    }
}
