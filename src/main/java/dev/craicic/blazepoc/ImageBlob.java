package dev.craicic.blazepoc;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Arrays;

@Entity
@Table(name = "image_blob")
public class ImageBlob {

    @Id
    private Integer id;

    @Column(name = "content", nullable = false)
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    private byte[] content;

    @OneToOne
    @MapsId
    @JoinColumn(name = "image_id")
    private Image image;

    @Override
    public String toString() {
        return "ImageBlob{" +
               "id=" + id +
               ", content=" + Arrays.toString(content) +
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
