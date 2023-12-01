package com.example.mycomics.beans;

public class AuteurBean {
    private int auteur_id;
    private String auteur_nom, auteur_prenom, auteur_pseudo;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */

    public AuteurBean(int auteur_id, String auteur_nom, String auteur_prenom, String auteur_pseudo) {
        this.auteur_id = auteur_id;
        this.auteur_nom = auteur_nom;
        this.auteur_prenom = auteur_prenom;
        this.auteur_pseudo = auteur_pseudo;
    }
    public AuteurBean(int auteur_id, String auteur_pseudo) {
        this.auteur_id = auteur_id;
        this.auteur_pseudo = auteur_pseudo;
    }
    public AuteurBean() {
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public int getAuteur_id() {
        return auteur_id;
    }
    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
    }
    public String getAuteur_nom() {
        return auteur_nom;
    }
    public void setAuteur_nom(String auteur_nom) {
        this.auteur_nom = auteur_nom;
    }
    public String getAuteur_prenom() {
        return auteur_prenom;
    }
    public void setAuteur_prenom(String auteur_prenom) {
        this.auteur_prenom = auteur_prenom;
    }
    public String getAuteur_pseudo() {
        return auteur_pseudo;
    }
    public void setAuteur_pseudo(String auteur_pseudo) {
        this.auteur_pseudo = auteur_pseudo;
    }
    /* -------------------------------------- */
    // ToString
    /* -------------------------------------- */
    @Override
    public String toString() {
        return "AuteurBean{" +
                "auteur_id=" + auteur_id +
                ", auteur_nom='" + auteur_nom + '\'' +
                ", auteur_prenom='" + auteur_prenom + '\'' +
                ", auteur_pseudo='" + auteur_pseudo + '\'' +
                '}';
    }
}
