package com.example.mycomics.beans;

import static java.lang.Integer.parseInt;

public class ProfilActifBean {
    private Integer profil_id;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public ProfilActifBean(Integer profil_id) {
        this.profil_id = profil_id;
    }
    public ProfilActifBean() {
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public Integer getProfil_id() {
        return profil_id;
    }
    public void setProfil_id(Integer profil_id) {
        this.profil_id = profil_id;
    }

    /* -------------------------------------- */
    // Tostring
    /* -------------------------------------- */

    @Override
    public String toString() {
        return profil_id + "";
    }
}
