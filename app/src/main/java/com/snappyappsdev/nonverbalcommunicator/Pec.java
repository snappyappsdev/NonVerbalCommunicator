package com.snappyappsdev.nonverbalcommunicator;

import java.util.UUID;

/**
 * Created by lrocha on 12/25/17.
 */

public class Pec {

    private UUID mId;
    private String mTitle;

    public Pec() {
        this(UUID.randomUUID());
    }

    public Pec(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPhotoFileName(){
        return "IMG_" + mId.toString() + ".jpg";
    }

    public String getSoundFileName(){
        return mId.toString() + ".3gp";
    }
}
