package dev.craicic.blazepoc;

public class PostDto {

    private Integer id;
    private String title;
    private String body;
    private final ImageDto image;

    public PostDto(Integer id, String title, String body, Integer imageId, byte[] content) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.image = new ImageDto(imageId, content);
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

    public ImageDto getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "PostDto{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", body='" + body + '\'' +
               ", image=" + image +
               '}';
    }
}
