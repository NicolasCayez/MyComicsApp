package com.example.mycomics.beans;

public class ProfileBean {

    private Integer profile_id;
    private String profile_name;
    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public ProfileBean(Integer profile_id, String profile_name) {
        this.profile_id = profile_id;
        this.profile_name = profile_name;
    }
    public ProfileBean(String profile_name) {
        this.profile_name = profile_name;
    }
    public ProfileBean() {
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
    public String getProfile_name() {
        return profile_name;
    }
    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    /* -------------------------------------- */
    // ToString
    /* -------------------------------------- */

    @Override
    public String toString() {
        return "ProfileBean{" +
                "profile_id=" + profile_id +
                ", profile_name='" + profile_name + '\'' +
                '}';

    }
}
