package dev.craicic.blazepoc;

import jakarta.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
