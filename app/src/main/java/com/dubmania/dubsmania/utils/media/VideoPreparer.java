package com.dubmania.dubsmania.utils.media;

import android.util.Log;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rat on 8/10/2015.
 */
public class VideoPreparer {

    public VideoPreparer() {

    }

    public void prepareVideo(File mAudioFile, File mVideoFile, File mOutputFile) {
        try {
            Movie video = MovieCreator.build(mVideoFile.getAbsolutePath());
            Movie audio = MovieCreator.build(mAudioFile.getAbsolutePath());
            List<Track> videoTracks = video.getTracks();
            video.setTracks(new LinkedList<Track>());

            List<Track> audioTracks = audio.getTracks();

            for (Track audioTrack : audioTracks) {
                video.addTrack(new AppendTrack(audioTrack));
            }

            for (Track videoTrack : videoTracks) {
                if(videoTrack.getHandler().equals("vide"))
                    video.addTrack(new AppendTrack(videoTrack));
            }


            Container mContainer =  new DefaultMp4Builder().build(video);
            WritableByteChannel wbc = new FileOutputStream(mOutputFile).getChannel();
            mContainer.writeContainer(wbc);
        } catch (IOException e) {
            Log.e("Video Perparer", "error in preparing video");
            e.printStackTrace();
        }
    }
}
