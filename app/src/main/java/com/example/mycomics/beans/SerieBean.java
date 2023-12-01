package com.example.mycomics.beans;

public class SerieBean {
    private int serie_id;
    private String serie_nom;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public SerieBean(int serie_id, String serie_nom) {
        this.serie_id = serie_id;
        this.serie_nom = serie_nom;
    }
    public SerieBean() {
    }
    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public int getSerie_id() {
        return serie_id;
    }
    public void setSerie_id(int serie_id) {
        this.serie_id = serie_id;
    }
    public String getSerie_nom() {
        return serie_nom;
    }
    public void setSerie_nom(String serie_nom) {
        this.serie_nom = serie_nom;
    }
    /* -------------------------------------- */
    // ToString
    /* -------------------------------------- */

    @Override
    public String toString() {
        return "SerieBean{" +
                "serie_id=" + serie_id +
                ", serie_nom='" + serie_nom + '\'' +
                '}';
    }
}
