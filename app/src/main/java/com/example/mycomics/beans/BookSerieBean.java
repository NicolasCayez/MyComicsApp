package com.example.mycomics.beans;

public class BookSerieBean extends BookBean {
        private String serie_name;

    /* -------------------------------------- */
    // Constructeur
    /* -------------------------------------- */
    public BookSerieBean(Integer book_id, String book_title, Integer book_number, String book_isbn, String book_picture, double book_editor_price, double book_value, String book_edition_date, boolean book_autograph, boolean book_special_edition, String book_special_edition_label, Integer serie_id, Integer editor_id, String serie_name) {
        super(book_id, book_title, book_number, book_isbn, book_picture, book_editor_price, book_value, book_edition_date, book_autograph, book_special_edition, book_special_edition_label, serie_id, editor_id);
        this.serie_name = serie_name;
    }

    public BookSerieBean(Integer book_id, String book_title) {
        super(book_id, book_title);
    }

    /* -------------------------------------- */
    // Get/set
    /* -------------------------------------- */
    public String getSerie_name() {
        return serie_name;
    }

    public void setSerie_name(String serie_name) {
        this.serie_name = serie_name;
    }

    public BookSerieBean(Integer book_id, String book_title, Integer book_number, String book_isbn, String book_picture, double book_editor_price, double book_value, String book_edition_date, boolean book_autograph, boolean book_special_edition, String book_special_edition_label, Integer serie_id, Integer editor_id) {
        super(book_id, book_title, book_number, book_isbn, book_picture, book_editor_price, book_value, book_edition_date, book_autograph, book_special_edition, book_special_edition_label, serie_id, editor_id);
    }

    @Override
    public String toString() {
        return "BookSerieBean{" +
                "serie_name='" + serie_name + '\'' +
                '}';
    }
}
