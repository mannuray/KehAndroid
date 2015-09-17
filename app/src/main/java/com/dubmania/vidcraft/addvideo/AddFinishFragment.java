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
import com.dubmania.vidcraft.utils.LanguageStore;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import io.realm.RealmResults;


public class AddFinishFragment extends Fragment {
    private EditText mVideoTitle;
    private String[] mLanguages;
    private String mLanguage;
    RealmResults<LanguageStore> mRealmResults;

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
        ArrayList<String> languages = new ArrayList<>();

/*        Realm realm = Realm.getInstance(getActivity().getApplicationContext());
        mRealmResults = realm.allObjects(LanguageStore.class).where().equalTo("supported", true).findAll();
        for(LanguageStore language: mRealmResults) {
            languages.add(language.getLanguage());
        } */
        // this is test data till we dant have data for langages
        languages.add("Hindi");
        languages.add("Nepali");
        languages.add("Bangladeshi");
        languages.add("Sri Lankan");


        mVideoTitle = (EditText) view.findViewById(R.id.editText);
        NumberPicker mLanguagePicker = (NumberPicker) view.findViewById(R.id.languagePicker);
        mLanguagePicker.setMinValue(0);
        mLanguagePicker.setMaxValue(3); // mRealmResults.size());
        mLanguages = languages.toArray(new String[4]); //mRealmResults.size()]);
        mLanguagePicker.setDisplayedValues(mLanguages);
        mLanguagePicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                mLanguage = mLanguages[scrollState];
            }
        });

        view.findViewById(R.id.addVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long id = 6554664749236224L;
               /* for(LanguageStore language: mRealmResults) {
                    if(language.getLanguage().equals(mLanguage)){
                        id = language.getId();
                        break;
                    }
                }*/
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
