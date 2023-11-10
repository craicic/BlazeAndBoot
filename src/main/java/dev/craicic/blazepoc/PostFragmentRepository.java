package dev.craicic.blazepoc;

import java.util.List;


public interface PostFragmentRepository {

    List<PostDto> findAllPosts();
}
