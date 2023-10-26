package dev.craicic.blazepoc;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlazePocTests {

    Logger log = LogManager.getLogger(this.getClass().getName());

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    public void fillData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Post post = new Post();
        post.setTitle("Blaze Persistence in Boot app");
        em.persist(post);

        Image image = new Image();
        image.setPost(post);
        em.persist(image);

        ImageBlob blob = new ImageBlob();
        blob.setContent("A huge succession of random characters !!!".getBytes());
        blob.setImage(image);
        em.persist(blob);

        Post post2 = em.find(Post.class, post.getId());

        log.info(post2);
        em.getTransaction().commit();
        em.close();

        emf.close();
    }
}
