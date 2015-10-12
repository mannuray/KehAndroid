package com.dubmania.vidcraft.addvideo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoFinishEvent;
import com.dubmania.vidcraft.communicator.eventbus.addvideoevent.AddVideoInfoEvent;
import com.dubmania.vidcraft.utils.AvailableLanguage;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class AddFinishFragment extends Fragment {
    private EditText mVideoTitle;
    private String[] mLanguages;
    private String mLanguage;
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

        view.findViewById(R.id.addVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               long id = 0;
               for(AvailableLanguage language: mRealmResults) {
                    if(language.getLanguage().equals(mLanguage)){
                        id = language.getLanguageId();
                        break;
                    }
                }
                BusProvider.getInstance().post(new AddVideoFinishEvent(mVideoTitle.getText().toString(), id));
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
