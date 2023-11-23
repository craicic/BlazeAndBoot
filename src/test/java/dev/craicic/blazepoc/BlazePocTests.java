package dev.craicic.blazepoc;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings({"unchecked", "deprecation"})
@SpringBootTest
public class BlazePocTests {

    Logger log = LogManager.getLogger(this.getClass().getName());

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    public void getPostsWithJoinTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<PostDto> q = em.createQuery("SELECT new dev.craicic.blazepoc.PostDto(p.id, p.title, p.body, i.id, ib.content) FROM Image i " +
                                               "JOIN ImageBlob ib ON ib.id = i.id " +
                                               "JOIN i.post p ON p.id = i.post.id", PostDto.class);
        List<PostDto> r = q.getResultList();

        r.forEach(e -> log.info(e));

        em.getTransaction().commit();
        em.close();

        assertEquals(10, r.size());

    }

    @Test
    public void getPostsNoJoinTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<PostDto> q = em.createQuery("SELECT new dev.craicic.blazepoc.PostDto(p.id, p.title, p.body, i.id, ib.content) FROM Image i, Post p, ImageBlob ib " +
                                               "WHERE ib.id = i.id " +
                                               "AND p.id = i.post.id", PostDto.class);
        List<PostDto> r = q.getResultList();
        r.forEach(e -> log.info(e));
        em.getTransaction().commit();
        em.close();

        assertEquals(10, r.size());
    }

    @Test
    public void getPostsWithEMTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Post> q = em.createQuery("SELECT p FROM Post p", Post.class);
        List<Post> r = q.getResultList();

        r.forEach(e -> log.info(e));

        em.getTransaction().commit();
        em.close();

        assertEquals(1, r.size());
    }

    @Test
    public void getPostsWithResultTransformer() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Session session = em.unwrap(Session.class);

        List<PostRTDto> dto = (List<PostRTDto>) session
                .createQuery("SELECT p.id, p.title, p.body, i.id, ib.content FROM Post p " +
                             "JOIN p.images i ON p.id = i.post.id " +
                             "JOIN ImageBlob ib ON i.id = ib.id " +
                             "WHERE p.id = 1 ")
                .setTupleTransformer((tuple, aliases) -> {
                    log.info("Transform tuple");
                    Arrays.stream(tuple).toList().forEach(a->log.info(a));
                    PostRTDto p = new PostRTDto();
                    p.setId((Integer) tuple[0]);
                    p.setTitle((String) tuple[1]);
                    p.setBody((String) tuple[2]);
                    p.getImageIds().add((Integer) tuple[3]);
                    p.getContents().add((byte[]) tuple[4]);
                    return List.of(p);
                }).getResultList();
        log.info(dto.toString());

        em.getTransaction().commit();
        em.close();

        assertEquals(10, dto.size());


    }

    @Test
    public void getPostsWithResultTransformerII() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Session session = em.unwrap(Session.class);

        List<PostRTDto> dto = (List<PostRTDto>) session
                .createQuery("SELECT p.id, p.title, p.body, i.id, ib.content FROM Post p " +
                             "JOIN p.images i ON p.id = i.post.id " +
                             "JOIN ImageBlob ib ON i.id = ib.id ")
                .setTupleTransformer((tuple, aliases) -> {
                    log.info("Transform tuple");
                    Arrays.stream(tuple).toList().forEach(a->log.info(a));
                    PostRTDto p = new PostRTDto();
                    p.setId((Integer) tuple[0]);
                    p.setTitle((String) tuple[1]);
                    p.setBody((String) tuple[2]);
                    p.getImageIds().add((Integer) tuple[3]);
                    p.getContents().add((byte[]) tuple[4]);
                    return List.of(p);
                }).getResultList();
        log.info(dto.toString());

        em.getTransaction().commit();
        em.close();

        assertEquals(10, dto.size());


    }
}
