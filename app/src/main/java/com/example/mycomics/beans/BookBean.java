package com.example.mycomics.beans;

public class BookBean {

    private Integer book_id, book_number, serie_id, editor_id;
    private String book_title, book_isbn, book_picture, book_edition_date, book_special_edition_label;
    private double book_editor_price, book_value;
    private boolean book_autograph, book_special_edition;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public BookBean(Integer book_id, String book_title, Integer book_number, String book_isbn, String book_picture, Double book_editor_price, Double book_value, String book_edition_date, boolean book_autograph, boolean book_special_edition, String book_special_edition_label, Integer serie_id, Integer editor_id) {
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_number = book_number;
        this.book_isbn = book_isbn;
        this.book_picture = book_picture;
        this.book_editor_price = book_editor_price;
        this.book_value = book_value;
        this.book_edition_date = book_edition_date;
        this.book_autograph = book_autograph;
        this.book_special_edition = book_special_edition;
        this.book_special_edition_label = book_special_edition_label;
        this.serie_id = serie_id;
        this.editor_id = editor_id;
    }
    public BookBean(Integer book_id, String book_title) {
        this.book_id = book_id;
        this.book_title = book_title;
    }
    public BookBean(Integer book_id) {
        this.book_id = book_id;
    }
    public BookBean() {
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */

    public Integer getBook_id() {
        return book_id;
    }

    public void setBook_id(Integer book_id) {
        this.book_id = book_id;
    }

    public Integer getBook_number() {
        return book_number;
    }

    public void setBook_number(Integer book_number) {
        this.book_number = book_number;
    }

    public Integer getSerie_id() {
        return serie_id;
    }

    public void setSerie_id(Integer serie_id) {
        this.serie_id = serie_id;
    }

    public Integer getEditor_id() {
        return editor_id;
    }

    public void setEditor_id(Integer editor_id) {
        this.editor_id = editor_id;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_isbn() {
        return book_isbn;
    }

    public void setBook_isbn(String book_isbn) {
        this.book_isbn = book_isbn;
    }

    public String getBook_picture() {
        return book_picture;
    }

    public void setBook_picture(String book_picture) {
        this.book_picture = book_picture;
    }

    public String getBook_edition_date() {
        return book_edition_date;
    }

    public void setBook_edition_date(String book_edition_date) {
        this.book_edition_date = book_edition_date;
    }

    public String getBook_special_edition_label() {
        return book_special_edition_label;
    }

    public void setBook_special_edition_label(String book_special_edition_label) {
        this.book_special_edition_label = book_special_edition_label;
    }

    public double getBook_editor_price() {
        return book_editor_price;
    }

    public void setBook_editor_price(double book_editor_price) {
        this.book_editor_price = book_editor_price;
    }

    public double getBook_value() {
        return book_value;
    }

    public void setBook_value(double book_value) {
        this.book_value = book_value;
    }

    public boolean isBook_autograph() {
        return book_autograph;
    }

    public void setBook_autograph(boolean book_autograph) {
        this.book_autograph = book_autograph;
    }

    public boolean isBook_special_edition() {
        return book_special_edition;
    }

    public void setBook_special_edition(boolean book_special_edition) {
        this.book_special_edition = book_special_edition;
    }

    /* -------------------------------------- */
    // ToString
    /* -------------------------------------- */
    @Override
    public String toString() {
        return "BookBean{" +
                "book_id=" + book_id +
                ", book_number=" + book_number +
                ", serie_id=" + serie_id +
                ", editor_id=" + editor_id +
                ", book_title='" + book_title + '\'' +
                ", book_isbn='" + book_isbn + '\'' +
                ", book_picture='" + book_picture + '\'' +
                ", book_special_edition_label='" + book_special_edition_label + '\'' +
                ", book_editor_price=" + book_editor_price +
                ", book_value=" + book_value +
                ", book_edition_date=" + book_edition_date +
                ", book_autograph=" + book_autograph +
                ", book_special_edition=" + book_special_edition +
                '}';
    }
}
