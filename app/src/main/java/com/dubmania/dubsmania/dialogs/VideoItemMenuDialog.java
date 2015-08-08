package com.dubmania.dubsmania.dialogs;

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

/**
 * Created by rat on 8/2/2015.
 */
public class VideoItemMenuDialog extends DialogFragment {
    //To Do pick it form string.xml
    ListView mylist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_item_menu_dialog_layout, container, false);
        mylist = (ListView) rootView.findViewById(R.id.video_item_menu_diaglog_list);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().setTitle("Simple Dialog");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TypedArray title = getResources()
                .obtainTypedArray(R.array.video_menu_item_list);
        String listitems[] = {title.getString(0), title.getString(1), title.getString(2), title.getString(3)};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, listitems);

        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*ArrayList<VideoListItem> mVideoItemList = new ArrayList<VideoListItem>(Arrays.asList(
                        new VideoListItem(id, "heros", "mannu", false),
                        new VideoListItem(id, "heros1", "mannu", false),
                        new VideoListItem(id, "heros2", "mannu", false),
                        new VideoListItem(id, "heros3", "prashant", false)
                ));
                BusProvider.getInstance().post(new AddDiscoverVideoItemListEvent(mVideoItemList));*/
                getDialog().dismiss();

            }
        });

    }
}
