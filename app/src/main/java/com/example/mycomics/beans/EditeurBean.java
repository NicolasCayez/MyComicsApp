package com.example.mycomics.beans;

public class EditeurBean {

    private Integer editeur_id;
    private String editeur_nom;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public EditeurBean(Integer editeur_id, String editeur_nom) {
        this.editeur_id = editeur_id;
        this.editeur_nom = editeur_nom;
    }
    public EditeurBean() {
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public Integer getEditeur_id() {
        return editeur_id;
    }
    public void setEditeur_id(Integer editeur_id) {
        this.editeur_id = editeur_id;
    }
    public String getEditeur_nom() {
        return editeur_nom;
    }
    public void setEditeur_nom(String editeur_nom) {
        this.editeur_nom = editeur_nom;
    }

    /* -------------------------------------- */
    // ToString
    /* -------------------------------------- */
    @Override
    public String toString() {
        return "EditeurBean{" +
                "editeur_id=" + editeur_id +
                ", editeur_nom='" + editeur_nom + '\'' +
                '}';
    }
}
