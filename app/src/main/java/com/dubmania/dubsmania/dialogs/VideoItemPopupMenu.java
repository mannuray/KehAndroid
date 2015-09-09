package com.dubmania.dubsmania.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.dubmania.dubsmania.R;
import com.dubmania.dubsmania.misc.AddVideoToBoardActivity;
import com.dubmania.dubsmania.report.FeedbackActivity;
import com.dubmania.dubsmania.utils.ConstantsStore;

/**
 * Created by rat on 8/2/2015.
 */
public class VideoItemPopupMenu {
    private Long mVideoId;
    private View view;
    private Activity activity;

    public VideoItemPopupMenu(Activity activity, Long mVideoId, View view) {
        this.mVideoId = mVideoId;
        this.view = view;
        this.activity = activity;
    }

    public void show() {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater().inflate(R.menu.menu_video_item_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.action_add_video_to_board)
                    addVideoToBoard();
                else if(id == R.id.action_share)
                    return false ; // write for share  
                else if(id == R.id.action_report)
                    feedback(ConstantsStore.INTENT_REPORT);
                else if(id == R.id.action_improve)
                    feedback(ConstantsStore.INTENT_IMPROVE);
                return false;
            }
        });
        popup.show();
    }

    private void addVideoToBoard() {
        Intent intent = new Intent(activity, AddVideoToBoardActivity.class);
        intent.putExtra(ConstantsStore.INTENT_VIDEO_ID, mVideoId);
        activity.startActivity(intent);
    }

    private void feedback(int code) {
        Intent intent = new Intent(activity, FeedbackActivity.class);
        intent.putExtra(ConstantsStore.INTENT_VIDEO_ID, mVideoId);
        intent.putExtra(ConstantsStore.INTENT_REPORT_ACTION, code);
        activity.startActivity(intent);
    }
}
