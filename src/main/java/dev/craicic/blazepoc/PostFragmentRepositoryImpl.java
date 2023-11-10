package dev.craicic.blazepoc;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostFragmentRepositoryImpl implements PostFragmentRepository {


    private final EntityManager em;

    @Autowired
    public PostFragmentRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<PostDto> findAllPosts() {
        TypedQuery<PostDto> q = em.createQuery("SELECT new dev.craicic.blazepoc.PostDto(p.id, p.title, p.body, i.id, ib.content) FROM Image i " +
                                            "JOIN FETCH ImageBlob ib ON ib.id = i.id " +
                                            "JOIN FETCH Post p ON p.id = i.post.id", PostDto.class);
        return q.getResultList();
    }
}
