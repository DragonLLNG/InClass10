package edu.uncc.inclass10.models;

import java.time.LocalDateTime;

public class Post{
    public String created_by_name, post_id, created_by_uid, post_text,created_at;

    public Post() {
    }

    public String getCreated_by_name() {
        return created_by_name;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getCreated_by_uid() {
        return created_by_uid;
    }

    public String getPost_text() {
        return post_text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setCreated_by_name(String created_by_name) {
        this.created_by_name = created_by_name;
    }

    public void setCreated_by_uid(String created_by_uid) {
        this.created_by_uid = created_by_uid;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    @Override
    public String toString() {
        return "Post{" +
                "created_by_name='" + created_by_name + '\'' +
                ", post_id='" + post_id + '\'' +
                ", created_by_uid='" + created_by_uid + '\'' +
                ", post_text='" + post_text + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
