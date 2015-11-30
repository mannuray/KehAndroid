package com.dubmania.vidcraft.addvideo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoFinishEvent;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoInfoEvent;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoUploadFailed;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.SetProgressBarValue;
import com.dubmania.vidcraft.communicator.networkcommunicator.VidsCraftHttpClient;
import com.dubmania.vidcraft.utils.database.AvailableLanguage;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class AddFinishFragment extends Fragment {
    private EditText mVideoTitle;
    private String[] mLanguages;
    private String mLanguage;
    private Button mAddVideo;
    private ProgressBar mProgressBar;
    RealmResults<AvailableLanguage> mRealmResults;

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

        ArrayList<String> availableLanguages = new ArrayList<>();
        Realm realm = Realm.getInstance(getActivity().getApplicationContext());
        mRealmResults = realm.allObjects(AvailableLanguage.class).where().findAll();
        for(AvailableLanguage language: mRealmResults) {
            availableLanguages.add(language.getLanguage());
        }

        mLanguages = availableLanguages.toArray(new String[mRealmResults.size()]);


        mVideoTitle = (EditText) view.findViewById(R.id.editText);
        NumberPicker mLanguagePicker = (NumberPicker) view.findViewById(R.id.languagePicker);
        mLanguagePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mLanguagePicker.setMinValue(0);
        mLanguagePicker.setMaxValue(mRealmResults.size() - 1);
        mLanguagePicker.setDisplayedValues(mLanguages);
        mLanguage = mLanguages[mLanguagePicker.getValue()];
        mLanguagePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mLanguage = mLanguages[newVal];
            }
        });

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mAddVideo = (Button) view.findViewById(R.id.addVideo);
        mAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddVideo.setEnabled(false);
                long id = 0;
                if (mVideoTitle.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please give a title", Toast.LENGTH_SHORT).show();
                    mAddVideo.setEnabled(true);
                    return;
                }
                for (AvailableLanguage language : mRealmResults) {
                    if (language.getLanguage().equals(mLanguage)) {
                        id = language.getLanguageId();
                        break;
                    }
                }
                BusProvider.getInstance().post(new AddVideoFinishEvent(mVideoTitle.getText().toString(), id));
                mProgressBar.setVisibility(View.VISIBLE);
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

    @Subscribe
    public void onSetProgressBarValue(SetProgressBarValue event) {
        mProgressBar.setProgress(event.getPercentage());
    }

    @Subscribe
    public void onAddVideoUploadFailed(AddVideoUploadFailed event) {
        mAddVideo.setEnabled(true);
        mProgressBar.setProgress(0);
        mProgressBar.setVisibility(View.GONE);
    }
}
