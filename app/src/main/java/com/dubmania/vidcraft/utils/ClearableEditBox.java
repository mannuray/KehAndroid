package com.dubmania.vidcraft.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by rat on 9/12/2015.
 */
public class ClearableEditBox {
    private EditText mEditText;
    private ImageView mCross;

    public ClearableEditBox(final EditText mEditText, final ImageView mCross) {
        this.mEditText = mEditText;
        this.mCross = mCross;

        this.mCross.setVisibility(View.GONE);
        this.mCross.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEditText.clearComposingText();
                mEditText.setText("");
                return false;
            }
        });

        this.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditText.getText().toString().equals("")) {
                    mCross.setVisibility(View.GONE);
                }
                else {
                    mCross.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
