package com.example.mycomics.beans;

public class NoLinkRecordBean {
    private Integer record_id;
    private String record_label, record_type;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public NoLinkRecordBean(Integer record_id, String record_label, String record_type) {
        this.record_id = record_id;
        this.record_label = record_label;
        this.record_type = record_type;
    }
    public NoLinkRecordBean() {
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public Integer getRecord_id() {
        return record_id;
    }
    public void setRecord_id(Integer record_id) {
        this.record_id = record_id;
    }
    public String getRecord_label() {
        return record_label;
    }
    public void setRecord_label(String record_label) {
        this.record_label = record_label;
    }
    public String getRecord_type() {
        return record_type;
    }
    public void setRecord_type(String record_type) {
        this.record_type = record_type;
    }
    /* -------------------------------------- */
    // ToString
    /* -------------------------------------- */
    @Override
    public String toString() {
        return "NoLinkRecordBean{" +
                "record_id=" + record_id +
                ", record_label='" + record_label + '\'' +
                ", record_type='" + record_type + '\'' +
                '}';
    }
}
