package dev.craicic.blazepoc.domain.dto;

import java.util.Arrays;

public class ImageWIthPostIdDto {

    private Integer id;
    byte[] content;
    private Integer postId;

    public ImageWIthPostIdDto(Integer id, byte[] content, Integer postId) {
        this.id = id;
        this.content = content;
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "ImageWIthPostIdDto{" +
               "id=" + id +
               ", content=" + Arrays.toString(content) +
               ", postId=" + postId +
               '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }
}
