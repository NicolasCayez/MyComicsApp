package com.example.mycomics.beans;

public class EditorBean {

    private Integer editor_id;
    private String editor_name;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public EditorBean(Integer editor_id, String editor_name) {
        this.editor_id = editor_id;
        this.editor_name = editor_name;
    }
    public EditorBean() {
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public Integer getEditor_id() {
        return editor_id;
    }
    public void setEditor_id(Integer editor_id) {
        this.editor_id = editor_id;
    }
    public String getEditor_name() {
        return editor_name;
    }
    public void setEditor_name(String editor_name) {
        this.editor_name = editor_name;
    }

    /* -------------------------------------- */
    // ToString
    /* -------------------------------------- */
    @Override
    public String toString() {
        return "EditorBean{" +
                "editor_id=" + editor_id +
                ", editor_name='" + editor_name + '\'' +
                '}';
    }
}
