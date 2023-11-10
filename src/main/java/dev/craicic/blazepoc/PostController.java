package dev.craicic.blazepoc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController {

    PostRepository repo;

    public PostController(PostRepository postRepository) {
        this.repo = postRepository;
    }

    @GetMapping("/api/post")
    public List<PostDto> getPosts() {
        return this.repo.findAllPosts().stream().toList();
    }
}
