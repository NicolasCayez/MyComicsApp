package com.example.mycomics.beans;

public class SerieBean {
    private Integer serie_id, nb_books;
    private String serie_name;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public SerieBean(Integer serie_id, String serie_name, Integer nb_books) {
        this.serie_id = serie_id;
        this.serie_name = serie_name;
        this.nb_books = nb_books;
    }
    public SerieBean(Integer serie_id, String serie_name) {
        this.serie_id = serie_id;
        this.serie_name = serie_name;
    }
    public SerieBean() {
    }
    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public Integer getSerie_id() {
        return serie_id;
    }
    public void setSerie_id(int serie_id) {
        this.serie_id = serie_id;
    }
    public String getSerie_name() {
        return serie_name;
    }
    public void setSerie_name(String serie_name) {
        this.serie_name = serie_name;
    }

    public void setSerie_id(Integer serie_id) {
        this.serie_id = serie_id;
    }

    public Integer getNb_books() {
        return nb_books;
    }

    public void setNb_books(Integer nb_books) {
        this.nb_books = nb_books;
    }
    /* -------------------------------------- */
    // ToString
    /* -------------------------------------- */

    @Override
    public String toString() {
        return "SerieBean{" +
                "serie_id=" + serie_id +
                ", serie_name='" + serie_name + '\'' +
                ", nb_books='" + nb_books + '\'' +
                '}';
    }
}
