package com.dubmania.dubsmania.dialogs;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.misc.AddVideoToBoardActivity;
import com.dubmania.dubsmania.utils.ConstantsStore;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoItemMenuDialog extends DialogFragment {
    //To Do pick it form string.xml
    private ListView mylist;
    public Long mVideoId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_item_menu_dialog_layout, container, false);
        mylist = (ListView) rootView.findViewById(R.id.video_item_menu_diaglog_list);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TypedArray title = getResources()
                .obtainTypedArray(R.array.video_menu_item_list);
        String listitems[] = {title.getString(0), title.getString(1), title.getString(2), title.getString(3)};
        title.recycle();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, listitems);

        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDialog().dismiss();
                switch (position) {
                    case 0:
                        addVideoToBoard();
                        break;
                    case 1:
                        // code for share
                        break;
                    case 3:
                        // code for repart
                        break;
                    case 4:
                        // code for improve
                }
            }
        });
    }

    private void addVideoToBoard() {
        Intent intent = new Intent(getActivity(), AddVideoToBoardActivity.class);
        intent.putExtra(ConstantsStore.INTENT_VIDEO_ID, mVideoId);
        startActivity(intent);
    }
}
