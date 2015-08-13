package com.dubmania.dubsmania.addvideo;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.communicator.eventbus.BusProvider;

public class AddTagFragment extends Fragment implements AbsListView.OnItemClickListener {
    private ListAdapter mAdapter;
    private TextView mTags;
    private String[] values;
    public AddTagFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_tag, container, false);
        mTags = (TextView) view.findViewById(R.id.textView);
        values = new String[] { "hello"}; // TO DO get it from real list
        mAdapter = new ArrayAdapter<>(getActivity(),
        android.R.layout.simple_list_item_1, android.R.id.text1, values);

        AbsListView mListView = (AbsListView) view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textview = new TextView(getActivity().getApplicationContext());
        textview.setText(values[position]);
        textview.setBackgroundResource(R.drawable.oval);
        textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.abc_btn_check_material, 0);
        BitmapDrawable dd = (BitmapDrawable) getDrawableFromTExtView(textview);
        mTags.setText(addSmily(dd));
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
