package dev.craicic.blazepoc;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BlazePocTests {

    Logger log = LogManager.getLogger(this.getClass().getName());

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    public void getPostsTests() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<PostDto> q = em.createQuery("SELECT new dev.craicic.blazepoc.PostDto(p.id, p.title, p.body, i.id, ib.content) FROM Image i " +
                                               "JOIN FETCH ImageBlob ib ON ib.id = i.id " +
                                               "JOIN FETCH Post p ON p.id = i.post.id", PostDto.class);
        List<PostDto> r = q.getResultList();

        r.forEach(e -> log.info(e));

        em.getTransaction().commit();
        em.close();

        emf.close();
    }

    @Test
    public void getPostsTestsNoJoin() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<PostDto> q = em.createQuery("SELECT new dev.craicic.blazepoc.PostDto(p.id, p.title, p.body, i.id, ib.content) FROM Image i, Post p, ImageBlob ib " +
                                               "WHERE ib.id = i.id " +
                                               "AND p.id = i.post.id", PostDto.class);
        List<PostDto> r = q.getResultList();

        r.forEach(e -> log.info(e));

        em.getTransaction().commit();
        em.close();

        emf.close();
    }
}
