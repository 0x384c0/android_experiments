package com.desu.experiments;

import com.desu.experiments.model.GoogleApi.ImagesResponse;

public class Singleton {
    private static Singleton mInstance = null;
    private Singleton(){
    }
    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }


    public ImagesResponse getImagesResponse() {
        return imagesResponse;
    }
    public void setImagesResponse(ImagesResponse imagesResponse) {
        this.imagesResponse = imagesResponse;
    }
    ImagesResponse imagesResponse;
}