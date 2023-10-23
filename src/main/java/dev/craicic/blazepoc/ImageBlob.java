package dev.craicic.blazepoc;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Entity
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
}
