package dev.craicic.blazepoc;

import dev.craicic.blazepoc.domain.dto.ImageDto;
import dev.craicic.blazepoc.domain.Post;
import dev.craicic.blazepoc.domain.dto.ImageWIthPostIdDto;
import dev.craicic.blazepoc.domain.dto.PostDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SuppressWarnings({"unchecked", "deprecation"})
@SpringBootTest
public class HibernateFeaturesTests {

    Logger log = LogManager.getLogger(this.getClass().getName());

    @PersistenceUnit
    private EntityManagerFactory emf;

    private List<PostDto> resultPosts;
    private Integer lastId;
    private PostDto p;

    @BeforeEach
    public void beforeEach() {
        resultPosts = new ArrayList<>();
        lastId = null;
        p = new PostDto();
    }

    @Test
    public void getPostsWithJoinTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<PostDto> q = em.createQuery("""
                SELECT new dev.craicic.blazepoc.domain.dto.PostDto(p.id, p.title, p.body, i.id, ib.content) FROM Image i
                JOIN ImageBlob ib ON ib.id = i.id
                JOIN i.post p
                """, PostDto.class);
        List<PostDto> r = q.getResultList();
        r.forEach(e -> log.info(e));
        em.getTransaction().commit();
        em.close();

        assertEquals(25, r.size());
    }

    @Test
    public void getPostsWithJoinIITest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<PostDto> q = em.createQuery("""
                SELECT new dev.craicic.blazepoc.domain.dto.PostDto(p.id, p.title, p.body, i.id, ib.content) FROM Post p
                JOIN p.images i
                JOIN ImageBlob ib ON ib.id = i.id
                """, PostDto.class);
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

        TypedQuery<PostDto> q = em.createQuery("""
                SELECT new dev.craicic.blazepoc.domain.dto.PostDto(p.id, p.title, p.body, i.id, ib.content) FROM Image i, Post p, ImageBlob ib
                WHERE ib.id = i.id
                AND p.id = i.post.id
                """, PostDto.class);
        List<PostDto> r = q.getResultList();
        r.forEach(e -> log.info(e));
        em.getTransaction().commit();
        em.close();

        assertEquals(25, r.size());
    }

    @Test
    public void getPostsWithNPlusOneIssueTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Post> q = em.createQuery("SELECT p FROM Post p", Post.class);
        List<Post> r = q.getResultList();
        // This post.toString() will trigger several query because we didn't use a JOIN FETCH clause here.
        r.forEach(post -> log.info(post));
        em.getTransaction().commit();
        em.close();

        assertEquals(5, r.size());
    }

    @Test
    public void getPostsWithJoinFetchTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Post> q = em.createQuery("SELECT p FROM Post p JOIN FETCH p.images i", Post.class);
        List<Post> r = q.getResultList();
        // The JOIN FETCH clause will result in one query only.
        r.forEach(post -> log.info(post));
        em.getTransaction().commit();
        em.close();

        assertEquals(5, r.size());
    }


    @Test
    public void getPostsWithTupleTransformerTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Session session = em.unwrap(Session.class);
        List<PostDto> dto = (List<PostDto>) session
                .createQuery("""
                        SELECT p.id, p.title, p.body, i.id, ib.content FROM Post p
                        JOIN p.images i ON p.id = i.post.id
                        JOIN ImageBlob ib ON i.id = ib.id
                        WHERE p.id = 1
                        """)
                .setTupleTransformer((tuple, aliases) -> {
                    log.info("Transform tuple");
                    PostDto p = new PostDto();
                    p.setId((Integer) tuple[0]);
                    p.setTitle((String) tuple[1]);
                    p.setBody((String) tuple[2]);
                    p.getImages().add(new ImageDto((Integer) tuple[3], (byte[]) tuple[4]));
                    return p;
                }).getResultList();
        log.info(dto.toString());

        em.getTransaction().commit();
        em.close();

        assertEquals(10, dto.size());
    }

    @Test
    public void getPostsWithTupleTransformerThenListTransformerTest() {

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Session session = em.unwrap(Session.class);
        List<PostDto> dto = (List<PostDto>) session
                .createQuery("""
                        SELECT p.id, p.title, p.body, i.id, ib.content FROM Post p
                        JOIN p.images i ON p.id = i.post.id
                        JOIN ImageBlob ib ON i.id = ib.id
                        ORDER BY p.id
                        """)
                .setTupleTransformer((tuple, aliases) -> {
                    log.info("Transform tuple");
                    if (tuple[0] != lastId) {
                        p = new PostDto();
                        p.setId((Integer) tuple[0]);
                        p.setTitle((String) tuple[1]);
                        p.setBody((String) tuple[2]);
                        p.getImages().add(new ImageDto((Integer) tuple[3], (byte[]) tuple[4]));
                        lastId = (Integer) tuple[0];
                        resultPosts.add(p);
                    }
                    p.getImages().add(new ImageDto((Integer) tuple[3], (byte[]) tuple[4]));
                    return null;
                })
                .setResultListTransformer(list -> {
                    log.info("Transform list");
                    return  list;
                })
                .getResultList();
        log.info(dto.toString());

        em.getTransaction().commit();
        em.close();

        assertEquals(5, dto.size());
    }

    @Test
    public void getPostsWithResultListTransformerTest() {
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
                            List<Object> l = (List<Object>) list;
                            Iterator<Object> itr = l.iterator();
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
    public void getPostsWithSuccessiveDtoProjectionsTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<PostDto> q1 = em.createQuery("SELECT new dev.craicic.blazepoc.domain.dto.PostDto(p.id, p.title, p.body) FROM Post p", PostDto.class);
        List<PostDto> posts = q1.setMaxResults(4).getResultList();

        for (PostDto p : posts) {
            TypedQuery<ImageDto> q2 = em.createQuery(""" 
                    SELECT new dev.craicic.blazepoc.domain.dto.ImageDto(i.id, ib.content)
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


    // Not working
    @Test
    public void getPostsWithTwoProjectionsTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<PostDto> q1 = em.createQuery("SELECT new dev.craicic.blazepoc.domain.dto.PostDto(p.id, p.title, p.body) FROM Post p", PostDto.class);
        List<PostDto> posts = q1.setMaxResults(4).getResultList();
        List<Integer> ids = new ArrayList<>();
        posts.forEach(p -> ids.add(p.getId()));

        TypedQuery<ImageWIthPostIdDto> q2 = em.createQuery("""
                    SELECT new dev.craicic.blazepoc.domain.dto.ImageWIthPostIdDto(i.id, ib.content, i.post.id)
                    FROM Image i
                    JOIN ImageBlob ib ON i.id = ib.id
                    WHERE i.post.id IN :postIds
                    """, ImageWIthPostIdDto.class);
        q2.setParameter("postIds", ids);
        List<ImageWIthPostIdDto> images = q2.getResultList();


        em.getTransaction().commit();
        em.close();
        assertEquals(4, posts.size());
        log.info(posts);
    }
}
