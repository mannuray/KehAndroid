package com.dubmania.vidcraft.utils.media;

import android.util.Log;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rat on 9/11/2015.
 */
public class VideoTrimmer {
    public static void startTrim(File src, File dst, int startMs, int endMs) throws IOException {
        //RandomAccessFile randomAccessFile = new RandomAccessFile(src, "r");
        Movie movie = MovieCreator.build(src.getAbsolutePath());
        // remove all tracks we will create new tracks from the old
        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList<Track>());
        double startTime = startMs/1000;
        double endTime = endMs/1000;
        boolean timeCorrected = false;
        // Here we try to find a track that has sync samples. Since we can only start decoding
        // at such a sample we SHOULD make sure that the start of the new fragment is exactly
        // such a frame
        for (Track track : tracks) {
            Log.i("Video Trimmer", "duraton track " + String.valueOf(track.getDuration()));
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                if (timeCorrected) {
                    // This exception here could be a false positive in case we have multiple tracks
                    // with sync samples at exactly the same positions. E.g. a single movie containing
                    // multiple qualities of the same video (Microsoft Smooth Streaming file)
                    throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                }
                startTime = correctTimeToSyncSample(track, startTime, false);
                endTime = correctTimeToSyncSample(track, endTime, true);
                Log.i("Video Trimmer", "satrt and etst befor " + String.valueOf(startTime) + " " + String.valueOf(endTime));
                timeCorrected = true;
            }
        }
        for (Track track : tracks) {
            long currentSample = 0;
            double currentTime = 0;
            double totalTime = 0;
            long startSample = -1;
            long endSample = -1;
            for(long time: track.getSampleDurations())
            {
                if (currentTime <= startTime) {
                    // current sample is still before the new starttime
                    startSample = currentSample;
                }
                if (currentTime <= endTime) {
                    // current sample is after the new start time and still before the new endtime
                    endSample = currentSample;
                } else {
                    // current sample is after the end of the cropped video
                    break;
                }
                totalTime += time;
                currentTime = totalTime / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
            }
            movie.addTrack(new CroppedTrack(track, startSample, endSample));
        }

        Container mContainer =  new DefaultMp4Builder().build(movie);
        if (!dst.exists()) {
            dst.createNewFile();
        }
        WritableByteChannel wbc = new FileOutputStream(dst).getChannel();
        mContainer.writeContainer(wbc);
    }

    /*
    protected static long getDuration(Track track) {
        long duration = 0;
        for (TimeToSampleBox.Entry entry : track.getSampleDurations().length) {
            duration += entry.getCount() * entry.getDelta();
        }
        return duration;
    }*/

    private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        long totalTime = 0;
        for(long time: track.getSampleDurations()){
            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                // samples always start with 1 but we start with zero therefore +1
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
            }
            totalTime += time;
            currentTime = totalTime / (double) track.getTrackMetaData().getTimescale();
            currentSample++;
        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }
}
