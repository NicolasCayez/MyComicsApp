package com.example.mycomics.beans;

public class ProfilBean {

    private int profil_id;
    private String profil_nom;
    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public ProfilBean(int profil_id, String profil_nom) {
        this.profil_id = profil_id;
        this.profil_nom = profil_nom;
    }
    public ProfilBean() {
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public int getProfil_id() {
        return profil_id;
    }
    public void setProfil_id(int profil_id) {
        this.profil_id = profil_id;
    }
    public String getProfil_nom() {
        return profil_nom;
    }
    public void setProfil_nom(String profil_nom) {
        this.profil_nom = profil_nom;
    }

    /* -------------------------------------- */
    // ToString
    /* -------------------------------------- */

    @Override
    public String toString() {
        return "ProfilBean{" +
                "profil_id=" + profil_id +
                ", profil_nom='" + profil_nom + '\'' +
                '}';

    }
}
