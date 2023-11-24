package dev.craicic.blazepoc;

public class ImageDto {

    private Integer id;
    byte[] content;


    public ImageDto() {
    }

    public ImageDto(Integer id, byte[] content) {
        this.id = id;
        this.content = content;
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

    @Override
    public String toString() {
        return "ImageDto{" +
               "id=" + id +
               ", content=" + content +
               '}';
    }
}
