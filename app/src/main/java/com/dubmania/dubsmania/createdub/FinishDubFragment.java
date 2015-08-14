package com.dubmania.dubsmania.createdub;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.createdubevent.CreateDubShareEvent;
import com.dubmania.dubsmania.communicator.eventbus.miscevent.OnClickListnerEvent;
import com.dubmania.dubsmania.utils.ConstantsStore;

public class FinishDubFragment extends Fragment {

    public FinishDubFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish_dub, container, false);
        view.findViewById(R.id.shareVideoMessenger).setOnClickListener(new OnClickListnerEvent<>(new CreateDubShareEvent(ConstantsStore.SHARE_APP_ID_MESSENGER)));
        view.findViewById(R.id.shareVideoWhatsapp).setOnClickListener(new OnClickListnerEvent<>(new CreateDubShareEvent(ConstantsStore.SHARE_APP_ID_WHATSAPP)));
        view.findViewById(R.id.shareVideoSaveToGallery).setOnClickListener(new OnClickListnerEvent<>(new CreateDubShareEvent(ConstantsStore.SHARE_APP_ID_SAVE_GALLERY)));
        return view;
    }
}
