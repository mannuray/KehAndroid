package com.dubmania.dubsmania.addvideo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.dubmania.dubsmania.Adapters.TagListAdapter;
import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;
import com.dubmania.dubsmania.communicator.eventbus.addvideoevent.AddTagsEvent;
import com.dubmania.dubsmania.communicator.networkcommunicator.TagsDownloader;
import com.dubmania.dubsmania.communicator.networkcommunicator.TagsDownloaderCallback;

import java.util.ArrayList;

public class AddTagFragment extends Fragment implements AbsListView.OnItemClickListener {
    private TagListAdapter mAdapter;
    private TextView mTagsView;
    private ArrayList<Tag> mTags;
    private ArrayList<Tag> mAddedTags;
    private TagsDownloader mTagsDownloader;

    public AddTagFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTagsDownloader = new TagsDownloader();
        mTags = new ArrayList<>();
        mTags.add(new Tag((long)123, "hello"));
        mTags.add(new Tag((long)123, "tu hi"));
        mAddedTags = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_add_tag, container, false);
        mTagsView = (TextView) view.findViewById(R.id.textView);
        mAdapter = new TagListAdapter(getActivity().getApplicationContext(), mTags);
        /*new ArrayAdapter<>(getActivity(),
        android.R.layout.simple_list_item_1, android.R.id.text1, mTags);*/

        AbsListView mListView = (AbsListView) view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        EditText search = (EditText) view.findViewById(R.id.editText);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTagsDownloader.downloadTags(s.toString(), new OnTextWatcherListener());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textview = new TextView(getActivity().getApplicationContext());
        textview.setText(mTags.get(position).getTag());
        textview.setBackgroundResource(R.drawable.oval);
        textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.abc_btn_check_material, 0);
        BitmapDrawable dd = (BitmapDrawable) getDrawableFromTExtView(textview);
        mTagsView.append(addSmily(dd));
        mAddedTags.add(mTags.get(position));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_tag, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.action_add_tag_video) {
            BusProvider.getInstance().post(new AddTagsEvent(mAddedTags));
        }
        return true;
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

    private class OnTextWatcherListener extends TagsDownloaderCallback {

        @Override
        public void onTagsDownloadSuccess(ArrayList<Tag> tags) {
            mTags = tags;
            mAdapter.notifyDataSetChanged();
            Log.d("dataset ", "change notified ");
        }

        @Override
        public void onTagsDownloadFailure() {

        }
    }

    private SpannableStringBuilder addSmily(Drawable dd) {
        dd.setBounds(0, 0, dd.getIntrinsicWidth(), dd.getIntrinsicHeight());
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(":-)");
        builder.setSpan(new ImageSpan(dd), builder.length() - ":-)".length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private Object getDrawableFromTExtView(View view) {

        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(getResources(), viewBmp);
    }

}
