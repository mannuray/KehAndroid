package com.dubmania.vidcraft.communicator.networkcommunicator;

/**
 * Created by rat on 8/23/2015.
 */
public abstract class VideoBoardCreaterCallback {
    abstract public void onVideoBoardCreateSuccess(Long boardId);
    abstract public void onVideoBoardCreateFailure();
}
