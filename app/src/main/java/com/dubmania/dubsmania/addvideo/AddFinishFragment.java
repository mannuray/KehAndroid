package com.dubmania.dubsmania.addvideo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.AddVideoInfoEvent;
import com.squareup.otto.Subscribe;


public class AddFinishFragment extends Fragment {
    private EditText mVideoTitle;
    private String[] mLanguages;
    private String mLanguage;

    public AddFinishFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_finish, container, false);
        mLanguages = new  String[]{"Belgium", "France", "United Kingdom"};
        mVideoTitle = (EditText) view.findViewById(R.id.editText);
        NumberPicker mLanguagePicker = (NumberPicker) view.findViewById(R.id.languagePicker);
        mLanguagePicker.setMinValue(0);
        mLanguagePicker.setMaxValue(2);
        mLanguagePicker.setDisplayedValues(mLanguages); // get real values
        mLanguagePicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                mLanguage = mLanguages[scrollState];
            }
        });

        view.findViewById(R.id.addVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new AddVideoFinishEvent(mVideoTitle.getText().toString(), mLanguage));
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

    @Subscribe
    public void onAddVideoInfoEvent(AddVideoInfoEvent event) {
        mVideoTitle.setText(event.getTitle());
    }
}
