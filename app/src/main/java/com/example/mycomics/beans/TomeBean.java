package com.example.mycomics.beans;

import java.util.Date;

public class TomeBean {

    private Integer tome_id, tome_numero, serie_id, editeur_id;
    private String tome_titre, tome_isbn, tome_image, tome_date_edition, tome_edition_speciale_libelle;
    private double tome_prix_editeur, tome_valeur_connue;
    private boolean tome_dedicace, tome_edition_speciale;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public TomeBean(Integer tome_id, String tome_titre, Integer tome_numero, String tome_isbn,  String tome_image, Double tome_prix_editeur, Double tome_valeur_connue, String tome_date_edition, boolean tome_dedicace, boolean tome_edition_speciale, String tome_edition_speciale_libelle, Integer serie_id, Integer editeur_id) {
        this.tome_id = tome_id;
        this.tome_titre = tome_titre;
        this.tome_numero = tome_numero;
        this.tome_isbn = tome_isbn;
        this.tome_image = tome_image;
        this.tome_prix_editeur = tome_prix_editeur;
        this.tome_valeur_connue = tome_valeur_connue;
        this.tome_date_edition = tome_date_edition;
        this.tome_dedicace = tome_dedicace;
        this.tome_edition_speciale = tome_edition_speciale;
        this.tome_edition_speciale_libelle = tome_edition_speciale_libelle;
        this.serie_id = serie_id;
        this.editeur_id = editeur_id;
    }
    public TomeBean(Integer tome_id, String tome_titre) {
        this.tome_id = tome_id;
        this.tome_titre = tome_titre;
    }
    public TomeBean(Integer tome_id) {
        this.tome_id = tome_id;
    }
    public TomeBean() {
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public Integer getTome_id() {
        return tome_id;
    }
    public void setTome_id(Integer tome_id) {
        this.tome_id = tome_id;
    }
    public Integer getTome_numero() {
        return tome_numero;
    }
    public void setTome_numero(Integer tome_numero) {
        this.tome_numero = tome_numero;
    }
    public Integer getSerie_id() {
        return serie_id;
    }
    public void setSerie_id(Integer serie_id) {
        this.serie_id = serie_id;
    }
    public String getTome_titre() {
        return tome_titre;
    }
    public void setTome_titre(String tome_titre) {
        this.tome_titre = tome_titre;
    }
    public String getTome_isbn() {
        return tome_isbn;
    }
    public void setTome_isbn(String tome_isbn) {
        this.tome_isbn = tome_isbn;
    }
    public String getTome_image() {
        return tome_image;
    }
    public void setTome_image(String tome_image) {
        this.tome_image = tome_image;
    }
    public String getTome_edition_speciale_libelle() {
        return tome_edition_speciale_libelle;
    }
    public void setTome_edition_speciale_libelle(String tome_edition_speciale_libelle) {
        this.tome_edition_speciale_libelle = tome_edition_speciale_libelle;
    }
    public Double getTome_prix_editeur() {
        return tome_prix_editeur;
    }
    public void setTome_prix_editeur(Double tome_prix_editeur) {
        this.tome_prix_editeur = tome_prix_editeur;
    }
    public Double getTome_valeur_connue() {
        return tome_valeur_connue;
    }
    public void setTome_valeur_connue(Double tome_valeur_connue) {
        this.tome_valeur_connue = tome_valeur_connue;
    }
    public String getTome_date_edition() {
        return tome_date_edition;
    }
    public void setTome_date_edition(String tome_date_edition) {
        this.tome_date_edition = tome_date_edition;
    }
    public boolean isTome_dedicace() {
        return tome_dedicace;
    }
    public void setTome_dedicace(boolean tome_dedicace) {
        this.tome_dedicace = tome_dedicace;
    }
    public boolean isTome_edition_speciale() {
        return tome_edition_speciale;
    }
    public void setTome_edition_speciale(boolean tome_edition_speciale) {
        this.tome_edition_speciale = tome_edition_speciale;
    }

    public Integer getEditeur_id() {
        return editeur_id;
    }

    public void setEditeur_id(Integer editeur_id) {
        this.editeur_id = editeur_id;
    }

    /* -------------------------------------- */
    // ToString
    /* -------------------------------------- */
    @Override
    public String toString() {
        return "TomeBean{" +
                "tome_id=" + tome_id +
                ", tome_numero=" + tome_numero +
                ", serie_id=" + serie_id +
                ", editeur_id=" + editeur_id +
                ", tome_titre='" + tome_titre + '\'' +
                ", tome_isbn='" + tome_isbn + '\'' +
                ", tome_image='" + tome_image + '\'' +
                ", tome_edition_speciale_libelle='" + tome_edition_speciale_libelle + '\'' +
                ", tome_prix_achat=" + tome_prix_editeur +
                ", tome_valeur_connue=" + tome_valeur_connue +
                ", tome_date_edition=" + tome_date_edition +
                ", tome_dedicace=" + tome_dedicace +
                ", tome_edition_speciale=" + tome_edition_speciale +
                '}';
    }
}
