package com.documentsearch.docsearchbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentModel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    public DocumentModel() {}

    public DocumentModel(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isValid() {
        if (id == null || id.trim().isEmpty()) return false;
        if (title == null || title.trim().isEmpty()) return false;
        if (content == null || content.trim().isEmpty()) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Document{id='" + id + "', title='" + title + "'}";
    }
}
