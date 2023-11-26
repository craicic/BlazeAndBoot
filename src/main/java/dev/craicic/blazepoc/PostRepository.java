package dev.craicic.blazepoc;

import dev.craicic.blazepoc.domain.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer>, PostFragmentRepository {

}
