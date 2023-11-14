package dev.craicic.blazepoc;

import java.util.ArrayList;
import java.util.List;

public class PostDto {

    private Integer id;
    private String title;
    private String body;
    private final List<ImageDto> images = new ArrayList<>();

    public PostDto() {
    }

    public PostDto(Integer id, String title, String body, Integer imageId, byte[] content) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.images.add(new ImageDto(imageId, content));
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

    public List<ImageDto> getImages() {
        return this.images;
    }

    @Override
    public String toString() {
        return "PostDto{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", body='" + body + '\'' +
               ", image=" + images +
               '}';
    }
}
