package com.example.mycomics.beans;

import androidx.camera.core.processing.SurfaceProcessorNode;

public class ProfileActiveBean {
    private Integer profile_active_id;
    private Integer profile_id;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */

    public ProfileActiveBean(Integer profile_id) {
        this.profile_active_id = 1;
        this.profile_id = profile_id;
    }
    public ProfileActiveBean() {
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public Integer getProfile_id() {
        return profile_id;
    }
    public void setProfile_id(Integer profile_id) {
        this.profile_id = profile_id;
    }

    /* -------------------------------------- */
    // Tostring
    /* -------------------------------------- */

    @Override
    public String toString() {
        return profile_id + "";
    }
}
