package com.matrix.yukun.matrix.video_module.entity;

/**
 * Created by yukun on 18-1-5.
 */

public class EventVideo {
    public String videoUrl;
    public String videoTitle;
    public String videoImage;
    public int type;

    public EventVideo(String videoUrl, String videoImage,String videoTitle,int type) {
        this.videoUrl = videoUrl;
        this.videoTitle = videoTitle;
        this.videoImage = videoImage;
        this.type=type;
    }

    public EventVideo() {

    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }
}
