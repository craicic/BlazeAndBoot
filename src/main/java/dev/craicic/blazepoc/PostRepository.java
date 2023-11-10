package dev.craicic.blazepoc;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer>, PostFragmentRepository {

}
