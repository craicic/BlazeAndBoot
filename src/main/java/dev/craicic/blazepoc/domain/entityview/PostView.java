package dev.craicic.blazepoc.domain.entityview;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import dev.craicic.blazepoc.domain.Post;

@EntityView(Post.class)
public interface PostView {

    @IdMapping
    Integer getId();
    String getTitle();
    String getBody();
    // List<Image> getImages();
}
