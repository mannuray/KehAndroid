package com.dubmania.vidcraft.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dubmania.vidcraft.R;
import com.dubmania.vidcraft.communicator.eventbus.BusProvider;
import com.dubmania.vidcraft.communicator.eventbus.miscevent.VideoDeletedEvent;
import com.dubmania.vidcraft.communicator.networkcommunicator.VideoBoardVideoHandler;
import com.dubmania.vidcraft.misc.AddVideoToBoardActivity;
import com.dubmania.vidcraft.report.FeedbackActivity;
import com.dubmania.vidcraft.utils.ConstantsStore;


/**
 * Created by rat on 8/2/2015.
 */
public class VideoItemPopupMenu {
    private Long mBoardId;
    private Long mVideoId;
    private String mTitle;
    private View view;
    private boolean mUserBoard;
    private Activity activity;

    public VideoItemPopupMenu( Long mBoardId, Activity activity, Long mVideoId, String mTitle, View view, boolean mUserBoard) {
        this.mBoardId = mBoardId;
        this.mVideoId = mVideoId;
        this.mTitle = mTitle;
        this.view = view;
        this.activity = activity;
        this.mUserBoard = mUserBoard;
    }

    public void show() {
        PopupMenu popup = new PopupMenu(activity, view);
        Menu menu = popup.getMenu();

        popup.getMenuInflater().inflate(R.menu.menu_video_item_popup, menu);
        if(!mUserBoard) menu.removeItem(R.id.action_delete_video_from_board);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.action_add_video_to_board)
                    addVideoToBoard();
                else if(id == R.id.action_delete_video_from_board)
                    removeFromVideoBoard();
                else if(id == R.id.action_share)
                    shareit(); // write for share
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

    private void removeFromVideoBoard() {
        new VideoBoardVideoHandler().deleteVideoFromBoard(mBoardId, mVideoId, new VideoBoardVideoHandler.DeleteVideoFromBoardCallback() {
            @Override
            public void onDeleteVideoFromBoardSuccess() {
                BusProvider.getInstance().post(new VideoDeletedEvent(mVideoId));
            }

            @Override
            public void onDeleteVideoFromBoardFailure() {

            }
        });
    }

    private void feedback(int code) {
        Intent intent = new Intent(activity, FeedbackActivity.class);
        intent.putExtra(ConstantsStore.INTENT_VIDEO_ID, mVideoId);
        intent.putExtra(ConstantsStore.INTENT_REPORT_ACTION, code);
        activity.startActivity(intent);
    }

    private void shareit() {
        String shareBody = mTitle + " - http://www.kehbackend.appspot.com/goto/" + String.valueOf(mVideoId);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitle);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(sharingIntent, "share"));
    }
}
