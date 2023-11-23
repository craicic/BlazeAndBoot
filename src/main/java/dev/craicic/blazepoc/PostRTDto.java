package dev.craicic.blazepoc;

import java.util.ArrayList;
import java.util.List;

public class PostRTDto {


    private Integer id;
    private String title;
    private String body;
    private final List<Integer> imageIds = new ArrayList<>();
    private final List<byte[]> contents = new ArrayList<>();

    public PostRTDto() {
    }

    public PostRTDto(Integer id, String title, String body, Integer imageId, byte[] content) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.imageIds.add(imageId);
        this.contents.add(content);
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

    public List<Integer> getImageIds() {
        return imageIds;
    }

    public List<byte[]> getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "PostRTDto{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", body='" + body + '\'' +
               ", imageIds=" + imageIds +
               ", contents=" + contents +
               '}';
    }
}
