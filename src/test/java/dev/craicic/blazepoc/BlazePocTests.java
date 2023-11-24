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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

        assertEquals(25, r.size());

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

        assertEquals(25, r.size());
    }

    @Test
    public void getPostsWithEMTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Post> q = em.createQuery("SELECT p FROM Post p", Post.class);
        List<Post> r = q.getResultList();
        em.getTransaction().commit();
        em.close();

        assertEquals(5, r.size());
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
                    Arrays.stream(tuple).toList().forEach(a -> log.info(a));
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

        List<PostDto> dto = (List<PostDto>) session
                .createNativeQuery("""
                        SELECT p.id, p.title, p.body, array_agg(i.id) as images_id, array_agg(ib.content) as images_content
                        FROM post AS p
                            JOIN image AS i ON p.id = i.post_id
                            JOIN image_blob AS ib ON i.id = ib.image_id
                        GROUP BY p.id ORDER BY p.id LIMIT 4;
                        """)
                .setResultListTransformer((list) -> {
                            List<Object> cList = (List<Object>) list;
                            Iterator<Object> itr = cList.iterator();
                            log.info("Transform list");
                            List<PostDto> posts = new ArrayList<>();
                            while (itr.hasNext()) {
                                Object[] obj = (Object[]) itr.next();
                                PostDto p = new PostDto();
                                p.setId((Integer) obj[0]);
                                p.setTitle((String) obj[1]);
                                p.setBody((String) obj[2]);
                                Integer[] imageIdList = (Integer[]) obj[3];
                                byte[][] byteArrayList = (byte[][]) obj[4];
                                for (int i = 0; i < imageIdList.length && i < byteArrayList.length; i++) {
                                    p.getImages().add(
                                            new ImageDto(
                                                    imageIdList[i],
                                                    byteArrayList[i]
                                            ));
                                }
                                posts.add(p);
                            }
                            return posts;
                        }
                ).getResultList();

        em.getTransaction().commit();
        em.close();

        assertEquals(4, dto.size());
        log.info(dto);


    }

    @Test
    public void getPostsWithSuccessiveDtoProjections() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<PostDto> q1 = em.createQuery("SELECT new dev.craicic.blazepoc.PostDto(p.id, p.title, p.body) FROM Post p", PostDto.class);
        List<PostDto> posts = q1.setMaxResults(4).getResultList();

        for (PostDto p : posts) {
            TypedQuery<ImageDto> q2 = em.createQuery(""" 
                    SELECT new dev.craicic.blazepoc.ImageDto(i.id, ib.content)
                    FROM Image i
                    JOIN ImageBlob ib ON i.id = ib.id
                    WHERE i.post.id = :postId
                    """, ImageDto.class);
            q2.setParameter("postId", p.getId());
            p.getImages().addAll(q2.getResultList());
        }
        em.getTransaction().commit();
        em.close();

        assertEquals(4, posts.size());
        log.info(posts);
    }
}
