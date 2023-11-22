package dev.craicic.blazepoc;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String body;

    @OneToMany(mappedBy = "post")
    private Set<Image> images = new HashSet<>();

    @Override
    public String toString() {
        return "Post{" +
               "Id=" + id +
               ", title='" + title + '\'' +
               ", body='" + body + '\'' +
               ", images=" + images +
               '}';
    }

    public Set<Image> getImages() {
        return images;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
