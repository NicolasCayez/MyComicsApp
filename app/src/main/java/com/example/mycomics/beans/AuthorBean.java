package com.example.mycomics.beans;

public class AuthorBean {
    private Integer author_id, nb_books;
    private String author_last_name, author_first_name, author_pseudonym;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public AuthorBean(Integer author_id, String author_pseudonym, String author_last_name, String author_first_name, Integer nb_books) {
        this.author_id = author_id;
        this.author_pseudonym = author_pseudonym;
        this.author_last_name = author_last_name;
        this.author_first_name = author_first_name;
        this.nb_books = nb_books;
    }
    public AuthorBean(Integer author_id, String author_pseudonym, String author_last_name, String author_first_name) {
        this.author_id = author_id;
        this.author_pseudonym = author_pseudonym;
        this.author_last_name = author_last_name;
        this.author_first_name = author_first_name;
    }
    public AuthorBean(Integer author_id, String author_pseudonym) {
        this.author_id = author_id;
        this.author_pseudonym = author_pseudonym;
    }
    public AuthorBean() {
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */

    public Integer getAuthor_id() {
        return author_id;
    }
    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }
    public String getAuthor_last_name() {
        return author_last_name;
    }
    public void setAuthor_last_name(String author_last_name) {
        this.author_last_name = author_last_name;
    }
    public String getAuthor_first_name() {
        return author_first_name;
    }
    public void setAuthor_first_name(String author_first_name) {
        this.author_first_name = author_first_name;
    }
    public String getAuthor_pseudonym() {
        return author_pseudonym;
    }
    public void setAuthor_pseudonym(String author_pseudonym) {
        this.author_pseudonym = author_pseudonym;
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
        return "AuthorBean{" +
                "author_id=" + author_id +
                ", author_pseudonym='" + author_pseudonym + '\'' +
                ", author_last_name='" + author_last_name + '\'' +
                ", author_first_name='" + author_first_name + '\'' +
                ", nb_books='" + nb_books + '\'' +
                '}';
    }
}
