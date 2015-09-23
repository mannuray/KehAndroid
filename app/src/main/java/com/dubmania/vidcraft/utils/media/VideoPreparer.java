package com.dubmania.vidcraft.utils.media;

import android.util.Log;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.H264TrackImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rat on 8/10/2015.
 */
public class VideoPreparer {

    public VideoPreparer() {

    }

    public void prepareVideo(File mAudioFile, File mVideoFile, File mOutputFile) throws IOException {

        Movie video = MovieCreator.build(mVideoFile.getAbsolutePath());
        Movie audio = MovieCreator.build(mAudioFile.getAbsolutePath());
        List<Track> videoTracks = video.getTracks();
        video.setTracks(new LinkedList<Track>());

        List<Track> audioTracks = audio.getTracks();

        for (Track audioTrack : audioTracks) {
            if (audioTrack.getHandler().equals("soun"))
                video.addTrack(new AppendTrack(audioTrack));
        }

        for (Track videoTrack : videoTracks) {
            if(videoTrack.getHandler().equals("vide"))
                video.addTrack(new AppendTrack(videoTrack));
        }


        Container mContainer =  new DefaultMp4Builder().build(video);
        WritableByteChannel wbc = new FileOutputStream(mOutputFile).getChannel();
        mContainer.writeContainer(wbc);
    }

    public static void prepareMovieFromH264Track(File mAudioFile,  File mH264Track, File mOutputFile, int fps) throws IOException {
        Log.d("Image", "files " + mAudioFile.getAbsolutePath() + " " + mH264Track.getAbsolutePath() + " " + mOutputFile.getAbsolutePath());
        Movie audio = MovieCreator.build(mAudioFile.getAbsolutePath());
        Movie movie = new Movie();

        List<Track> audioTracks = audio.getTracks();

        DataSource videoTrack = new FileDataSourceImpl(mH264Track);
        H264TrackImpl h264Track = new H264TrackImpl(videoTrack, "eng", fps, 1);

        for (Track audioTrack : audioTracks) {
            if (audioTrack.getHandler().equals("soun"))
                movie.addTrack(new AppendTrack(audioTrack));
        }
        movie.addTrack(h264Track);
        Container mContainer =  new DefaultMp4Builder().build(movie);
        WritableByteChannel wbc = new FileOutputStream(mOutputFile).getChannel();
        mContainer.writeContainer(wbc);
    }
}
